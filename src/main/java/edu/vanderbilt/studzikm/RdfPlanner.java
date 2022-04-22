package edu.vanderbilt.studzikm;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RdfPlanner {

    private final Model model;
    private final Resource transfer;
    private final Property atTime;
    private final Resource goal;
    private final Property obtain;
    private final Property hasOutput;
    private final Resource transform;
    private final InfModel infModel;
    private Integer time = 0;

    private final Map<String, Resource> resourceMap = new HashMap<>();
    private String aiPrefix;

    public RdfPlanner(String rdfInputFilename) {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(rdfInputFilename, "N3");
        this.infModel = ModelFactory.createInfModel(
                ReasonerRegistry.getOWLReasoner(),
                this.model
        );

        aiPrefix = this.infModel.getNsPrefixURI("ai");

        goal = this.model.getResource(aiPrefix +"Goal");
        transfer = this.model.getResource(aiPrefix + "Transfer");
        transform = this.model.getResource(aiPrefix + "Transform");
        atTime = this.model.getProperty(aiPrefix + "atTime");
        obtain = this.model.getProperty(aiPrefix + "obtain");
        hasOutput = this.model.getProperty(aiPrefix + "hasOutput");

        resourceMap.put("R1", model.getResource(aiPrefix + "Population"));
        resourceMap.put("R2", model.getResource(aiPrefix + "MetallicElement"));
        resourceMap.put("R3", model.getResource(aiPrefix + "Timber"));
        resourceMap.put("R21", model.getResource(aiPrefix + "MetallicAlloy"));
        resourceMap.put("R22", model.getResource(aiPrefix + "Electronics"));
        resourceMap.put("R23", model.getResource(aiPrefix +"House"));
        resourceMap.put("R21'", model.getResource(aiPrefix + "AlloyWaste"));
        resourceMap.put("R22'", model.getResource(aiPrefix + "ElectronicsWaste"));
        resourceMap.put("R23'", model.getResource(aiPrefix + "HouseWaste"));
    }

    public  Double score(ActionResult<?> result) {
        Action.Type type = result.getAction().getType();
        if (type == Action.Type.TRANSFER &&
                ((TransferResult) result).getRole() == TransferResult.Role.SENDER) {
            return 0.5;
        }

        updateKnowledgeBase(result, type);
        printStatements();

        String queryTemplate = String.format(
                "PREFIX ai: <%s> " +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                "ASK { " +
                "    ?a a ai:Action ; " +
                "        ai:atTime \"%d\"^^xsd:int ; " +
                "        ai:hasOutput ?ar . " +
                "    ?g a ai:Goal ; " +
                "        ai:obtain ?gr ; " +
                "        ai:atTime \"%d\"^^xsd:int . " +
                "    ?gr a ai:Resource ; " +
                "        ai:requires ?ar . " +
                "}", aiPrefix, time, time);
        System.out.println(queryTemplate);
        Query qry = QueryFactory.create(queryTemplate);
        QueryExecution qe = QueryExecutionFactory.create(qry, infModel);
        boolean queryResult = qe.execAsk();
        Double score = queryResult ? 0.85 : 0.15;

        if (time > 0) {
            queryTemplate = String.format(
                    "PREFIX ai: <%s> " +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                            "ASK { " +
                            "    ?a1 a ai:Action ; " +
                            "        ai:atTime \"%d\"^^xsd:int ; " +
                            "        ai:hasOutput ?ar1 . " +
                            "    ?g1 a ai:Goal ; " +
                            "        ai:obtain ?gr ; " +
                            "        ai:atTime \"%d\"^^xsd:int . " +
                            "    ?a2 a ai:Action ; " +
                            "        ai:atTime \"%d\"^^xsd:int ; " +
                            "        ai:hasOutput ?ar2 . " +
                            "    ?g2 a ai:Goal ; " +
                            "        ai:obtain ?gr ; " +
                            "        ai:atTime \"%d\"^^xsd:int . " +
                            "    ?gr a ai:Resource ; " +
                            "        ai:requires ?ar1 ; " +
                            "        ai:requires ?ar2 . " +
                            "}", aiPrefix, time, time, time - 1, time - 1);
            System.out.println(queryTemplate);
            qry = QueryFactory.create(queryTemplate);
            qe = QueryExecutionFactory.create(qry, infModel);
            queryResult = qe.execAsk();
            score = queryResult ? 0.9 : 0.1;
        }

        time++;
        return score;
    }

    private void updateKnowledgeBase(ActionResult<?> result, Action.Type type) {
        Resource resultResource = resourceMap.get(result.getAction().getName());
        Resource goalResource = selectGoal(result.getSelf());
        Resource actionType = type == Action.Type.TRANSFER ? transfer : transform;

        Literal timeLiteral = infModel.createTypedLiteral(time);
        Resource action = infModel.getResource(aiPrefix + "action" + time);
        action.addProperty(RDF.type, actionType)
                .addProperty(hasOutput, resultResource)
                .addLiteral(atTime, timeLiteral);

        Resource currGoal = infModel.createResource(aiPrefix + "goal" + time);
        currGoal.addProperty(RDF.type, goal)
                .addProperty(obtain, goalResource)
                .addLiteral(atTime, timeLiteral);
    }

    private Resource selectGoal(Country self) {
        Integer requiredHouses = self.getTargetAmount("R23");
        Integer requiredElectronics = self.getTargetAmount("R22");
        if (requiredHouses > 0) {
            return resourceMap.get("R23");
        } else if (requiredElectronics > 0) {
            return resourceMap.get("R22");
        }
        return infModel.getResource(aiPrefix + "NullResource");
    }

    private void printStatements() {
        RDFDataMgr.write(System.out, infModel, Lang.N3);
    }
}

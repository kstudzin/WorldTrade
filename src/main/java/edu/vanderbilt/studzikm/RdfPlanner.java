package edu.vanderbilt.studzikm;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.util.HashMap;
import java.util.Map;

public class RdfPlanner {

    private final Model model;
    private final Resource transfer;
    private final Property atTime;
    private final Resource goal;
    private final Property obtain;
    private final Property hasOutput;
    private final Resource transform;
    private Integer time = 0;

    private final Map<String, Resource> resourceMap = new HashMap<>();

    public RdfPlanner(String rdfInputFilename) {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(rdfInputFilename, "N3");
        goal = this.model.getResource("ai:Goal");
        transfer = this.model.getResource("ai:Transfer");
        transform = this.model.getResource("ai:Transform");
        atTime = this.model.getProperty("ai:atTime");
        obtain = this.model.getProperty("ai:obtain");
        hasOutput = this.model.getProperty("ai:hasOutput");

        resourceMap.put("R1", model.getResource("ai:Population"));
        resourceMap.put("R2", model.getResource("ai:MetallicElement"));
        resourceMap.put("R3", model.getResource("ai:Timber"));
        resourceMap.put("R21", model.getResource("ai:MetallicAlloy"));
        resourceMap.put("R22", model.getResource("ai:Electronics"));
        resourceMap.put("R23", model.getResource("ai:House"));
        resourceMap.put("R21'", model.getResource("ai:AlloyWaste"));
        resourceMap.put("R22'", model.getResource("ai:ElectronicsWaste"));
        resourceMap.put("R23'", model.getResource("ai:HouseWaste"));
    }

    public  Double score(ActionResult<?> result) {
        Action.Type type = result.getAction().getType();
        if (type == Action.Type.TRANSFER &&
                ((TransferResult) result).getRole() == TransferResult.Role.SENDER) {
            return 0.5;
        }

        updateKnowledgeBase(result, type);


        printStatements();

        time++;
        return null;
    }

    private void updateKnowledgeBase(ActionResult<?> result, Action.Type type) {
        Resource resultResource = resourceMap.get(result.getAction().getName());
        Resource goalResource = selectGoal(result.getSelf());
        Resource actionType = type == Action.Type.TRANSFER ? transfer : transform;

        Literal timeLiteral = model.createLiteral(String.format("\"%d\"^^xsd:int", time));
        Resource action = model.getResource("ai:action" + time);
        action.addProperty(RDF.type, actionType)
                .addProperty(hasOutput, resultResource)
                .addLiteral(atTime, timeLiteral);

        Resource currGoal = model.createResource("ai:goal" + time);
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
        return model.getResource("ai:NullResource");
    }

    private void printStatements() {
        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            System.out.println(statements.nextStatement());
        }
    }
}

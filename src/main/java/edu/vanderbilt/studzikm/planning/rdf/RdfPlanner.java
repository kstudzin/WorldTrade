package edu.vanderbilt.studzikm.planning.rdf;

import edu.vanderbilt.studzikm.Action;
import edu.vanderbilt.studzikm.ActionResult;
import edu.vanderbilt.studzikm.Country;
import edu.vanderbilt.studzikm.TransferResult;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class RdfPlanner {

    private final Model model;
    private final Resource transfer;
    private final Property atTime;
    private final Resource goal;
    private final Property obtain;
    private final Property hasOutput;
    private final Resource transform;
    private final InfModel infModel;
    private final Querior querior;
    private Integer time = 0;

    private final Map<String, Resource> resourceMap = new HashMap<>();
    private String aiPrefix;
    private Double[] scores = new Double[]{ 0.80, 0.85, 0.90, 0.95};

    private static final IntPredicate hasNext = i -> i >= 0;
    private static final IntUnaryOperator next = i -> i - 1;

    public RdfPlanner(String rdfInputFilename) {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(rdfInputFilename, "N3");
        this.infModel = ModelFactory.createInfModel(
                ReasonerRegistry.getOWLReasoner(),
                this.model
        );

        this.aiPrefix = this.infModel.getNsPrefixURI("ai");
        this.querior = new Querior(this.infModel);

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

        double score = IntStream.iterate(Math.min(4, time), hasNext, next)
                .peek(System.out::println)
                .mapToObj(this::findScore)
                .findFirst()
                .orElse(0.01);

        time++;
        return score;
    }

    private Double findScore(int depth) {
        boolean match = querior.historyAlignsWithGoal(depth, time);
        System.out.println(depth + " " + time);
        return match ? scores[depth] : 1 - scores[depth];
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

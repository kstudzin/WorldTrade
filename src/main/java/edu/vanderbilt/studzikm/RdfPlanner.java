package edu.vanderbilt.studzikm;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

public class RdfPlanner {

    private final Model model;
    private final Resource timberTransfer;
    private final Property atTime;
    private final Resource goal;
    private final Property obtain;
    private final Resource house;
    private Integer time = 0;

    public RdfPlanner(String rdfInputFilename) {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(rdfInputFilename, "N3");
        goal = this.model.getResource("ai:Goal");
        timberTransfer = this.model.getResource("ai:TimberTransfer");
        atTime = this.model.getProperty("ai:atTime");
        obtain = this.model.getProperty("ai:obtain");
        house = this.model.getResource("ai:House");
    }

    public  Double score(ActionResult<?> result) {
        Literal timeLiteral = model.createLiteral(String.format("\"%d\"^^xsd:int", time));

        Resource action = model.getResource("ai:action" + time);
        action.addProperty(RDF.type, timberTransfer)
                        .addLiteral(atTime, timeLiteral);

        Resource currGoal = model.createResource("ai:goal" + time);
        currGoal.addProperty(RDF.type, goal)
                .addProperty(obtain, house)
                .addLiteral(atTime, timeLiteral);

        printStatements();

        time++;
        return null;
    }

    private void printStatements() {
        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            System.out.println(statements.nextStatement());
        }
    }
}

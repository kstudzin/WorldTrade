package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.rdf.RdfPlanner;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RdfPlannerTest {

    RdfPlanner setupPlanner() {
        return new RdfPlanner("src/main/resources/planning.ttl");
    }

    @Test
    void testTransferReceiverHighScore() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();

        TransferResult transferResult = Mockito.mock(TransferResult.class);
        Transfer transfer = Mockito.mock(Transfer.class);
        Country country = Mockito.mock(Country.class);
        when(transferResult.getAction()).thenReturn(transfer);
        when(transfer.getName()).thenReturn("R2");
        when(transferResult.getSelf()).thenReturn(country);
        when(country.getTargetAmount("R23")).thenReturn(5);
        when(transfer.getType()).thenReturn(Action.Type.TRANSFER);
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECEIVER);

        Double score = planner.score(transferResult);
        assertEquals(0.80, score);
    }

    @Test
    void testTransferReceiverLowScore() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();

        TransferResult transferResult = Mockito.mock(TransferResult.class);
        Transfer transfer = Mockito.mock(Transfer.class);
        Country country = Mockito.mock(Country.class);
        when(transferResult.getAction()).thenReturn(transfer);
        when(transfer.getName()).thenReturn("R3");
        when(transferResult.getSelf()).thenReturn(country);
        when(country.getTargetAmount("R22")).thenReturn(5);
        when(transfer.getType()).thenReturn(Action.Type.TRANSFER);
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECEIVER);

        Double score = planner.score(transferResult);
        assertEquals(0.2, score, 0.0001);
    }

    @Test
    void testTwoActionsLowScore() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();

        TransferResult transferResult = Mockito.mock(TransferResult.class);
        Transfer transfer = Mockito.mock(Transfer.class);
        Country country = Mockito.mock(Country.class);
        when(transferResult.getAction()).thenReturn(transfer);
        when(transfer.getName()).thenReturn("R3");
        when(transferResult.getSelf()).thenReturn(country);
        when(country.getTargetAmount("R22")).thenReturn(5);
        when(transfer.getType()).thenReturn(Action.Type.TRANSFER);
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECEIVER);

        Double score = planner.score(transferResult);
        assertEquals(0.2, score, .0001);

        score = planner.score(transferResult);
        assertEquals(0.15, score, .0001);
    }

    @Test
    void testTwoActionsHighScore() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();

        TransferResult transferResult = Mockito.mock(TransferResult.class);
        Transfer transfer = Mockito.mock(Transfer.class);
        Country country = Mockito.mock(Country.class);
        when(transferResult.getAction()).thenReturn(transfer);
        when(transfer.getName()).thenReturn("R3");
        when(transferResult.getSelf()).thenReturn(country);
        when(country.getTargetAmount("R23")).thenReturn(5);
        when(transfer.getType()).thenReturn(Action.Type.TRANSFER);
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECEIVER);

        Double score = planner.score(transferResult);
        assertEquals(0.80, score);

        score = planner.score(transferResult);
        assertEquals(0.85, score);
    }

    @Test
    void testFiveActionsHighScore() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();

        TransferResult transferResult = Mockito.mock(TransferResult.class);
        Transfer transfer = Mockito.mock(Transfer.class);
        Country country = Mockito.mock(Country.class);
        when(transferResult.getAction()).thenReturn(transfer);
        when(transfer.getName()).thenReturn("R3");
        when(transferResult.getSelf()).thenReturn(country);
        when(country.getTargetAmount("R23")).thenReturn(5);
        when(transfer.getType()).thenReturn(Action.Type.TRANSFER);
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECEIVER);

        Double score = planner.score(transferResult);
        assertEquals(0.80, score);

        score = planner.score(transferResult);
        assertEquals(0.85, score);

        score = planner.score(transferResult);
        assertEquals(0.90, score);

        score = planner.score(transferResult);
        assertEquals(0.95, score);

        score = planner.score(transferResult);
        assertEquals(0.95, score);
    }

    @Test
    void testAcquireGoal() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();

        TransferResult transferResult = Mockito.mock(TransferResult.class);
        Transfer transfer = Mockito.mock(Transfer.class);
        Country country = Mockito.mock(Country.class);
        when(transferResult.getAction()).thenReturn(transfer);
        when(transfer.getName()).thenReturn("R23");
        when(transferResult.getSelf()).thenReturn(country);
        when(country.getTargetAmount("R23")).thenReturn(5);
        when(transfer.getType()).thenReturn(Action.Type.TRANSFER);
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECEIVER);

        Double score = planner.score(transferResult);
        assertEquals(0.80, score);
    }

    void testGoalChanges() {

    }

    void testTransitivity() {

    }

    // Note: this test is to understand how inference works
    @Test
    void testReflexiveProperty() {
        Model model = ModelFactory.createDefaultModel();
        Resource s = model.getResource(":s");
        Property p = model.getProperty(":p");
        Resource o = model.getResource(":o");
        model.add(s, p, o);
        model.add(p, RDF.type, OWL2.ReflexiveProperty);

        InfModel infModel =
                ModelFactory.createInfModel(ReasonerRegistry.getOWLReasoner(), model);

        System.out.println(infModel.contains(s, p, s));
    }

    // Note: this test is to understand how inference works
    @Test
    void testOwlReasoner() {
        OntModel ont = ModelFactory.createOntologyModel();
        Property p = ont.getProperty(":p");
        ont.add(p, RDF.type, OWL2.ReflexiveProperty);
        Resource s = ont.getResource(":s");
        Resource o = ont.getResource(":o");
        ont.add(s, p, o);

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(ont);

        OntModelSpec ontModelSpec = OntModelSpec.OWL_DL_MEM;
        ontModelSpec.setReasoner(reasoner);
        OntModel model = ModelFactory.createOntologyModel(ontModelSpec, ont);

        System.out.println(model.contains(s, p, s));
        RDFDataMgr.write(System.out, model, Lang.N3);

    }
}

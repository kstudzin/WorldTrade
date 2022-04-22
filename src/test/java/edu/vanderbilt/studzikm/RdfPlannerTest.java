package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.rdf.Querior;
import edu.vanderbilt.studzikm.planning.rdf.RdfPlanner;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RdfPlannerTest {

    RdfPlanner setupPlanner() throws FileNotFoundException {
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
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECIEVER);

        Double score = planner.score(transferResult);
        assertEquals(0.85, score);
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
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECIEVER);

        Double score = planner.score(transferResult);
        assertEquals(0.15, score);
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
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECIEVER);

        Double score = planner.score(transferResult);
        assertEquals(0.15, score);

        score = planner.score(transferResult);
        assertEquals(0.1, score);
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
        when(transferResult.getRole()).thenReturn(TransferResult.Role.RECIEVER);

        Double score = planner.score(transferResult);
        assertEquals(0.80, score);

        score = planner.score(transferResult);
        assertEquals(0.85, score);
    }

    @Test
    void testQuerior() {
        Model model = Mockito.mock(Model.class);
        when(model.getNsPrefixURI("ai")).thenReturn("urn:edu:vanderbilt:studzikm:");

        Querior q = new Querior(model);
        q.historyAlignsWithGoal(5, 10);
    }

    @Test
    void testFormat() {
        System.out.println(String.format("a%2$d a%1$d", 1, 2));
    }
}

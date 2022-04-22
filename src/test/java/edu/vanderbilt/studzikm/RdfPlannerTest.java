package edu.vanderbilt.studzikm;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;

import static org.mockito.Mockito.when;

public class RdfPlannerTest {

    RdfPlanner setupPlanner() throws FileNotFoundException {
        return new RdfPlanner("src/main/resources/planning.ttl");
    }

    @Test
    void testTransferReceiver() throws FileNotFoundException {
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

        planner.score(transferResult);
    }
}

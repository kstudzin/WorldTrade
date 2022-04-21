package edu.vanderbilt.studzikm;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class RdfPlannerTest {

    RdfPlanner setupPlanner() throws FileNotFoundException {
        return new RdfPlanner("src/main/resources/planning.ttl");
    }

    @Test
    void testBasic() throws FileNotFoundException {
        RdfPlanner planner = setupPlanner();
    }
}

package edu.vanderbilt.studzikm;

import com.microsoft.z3.Context;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class LogicalCountryModelTest {

    Map<String, Resource> setupResources() {
        return new ResourcesBuilder()
                .addResource("R1", .166)
                .addResource("R2", .166)
                .addResource("R3", .166)
                .addResource("R21", .166)
                .addResource("R22", .166)
                .addResource("R23", .166)
                .addResource("R21'", 0.0)
                .addResource("R22'", 0.0)
                .addResource("R23'", 0.0)
                .build();
    }

    @Test
    void testBasic() {
        Map<String, Resource> resources = setupResources();
        Country country = new Country("Self", null);
        country.addResource(resources.get("R1"), 100);
        country.addResource(resources.get("R2"), 100);
        country.addResource(resources.get("R3"), 100);
        country.addResource(resources.get("R21"), 100);
        country.addResource(resources.get("R22"), 25000);
        country.addResource(resources.get("R23"), 10);
        country.addResource(resources.get("R21'"), 0);
        country.addResource(resources.get("R22'"), 0);
        country.addResource(resources.get("R23'"), 0);

        LogicalCountryModel model = new LogicalCountryModel(new Context(), country);
//        model.score(null);
    }
}

package edu.vanderbilt.studzikm;

import com.microsoft.z3.Context;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    String setupAssertions() throws IOException {
        List<String> asserts = Files.readAllLines(new File("src/main/resources/PlanningModel.smt2").toPath());
        return asserts.stream().collect(Collectors.joining());
    }

    private TargetResourceAmountComputation setupComp(TransformFactory factory, Context ctx) {
        return new TargetResourceAmountComputation(factory, ctx);
    }

    TransformFactory setupTransformFactory(Map<String, Resource> resources) {
        return new DefaultTransforms(resources);
    }

    @Test
    void testBasic() throws IOException {
        Map<String, Resource> resources = setupResources();
        TransformFactory factory = setupTransformFactory(resources);
        TargetResourceAmountComputation comp = setupComp(factory, new Context());
        Country country = new Country("Self", null, comp);
        country.addResource(resources.get("R1"), 100);
        country.addResource(resources.get("R2"), 100);
        country.addResource(resources.get("R3"), 100);
        country.addResource(resources.get("R21"), 100);
        country.addResource(resources.get("R22"), 25000);
        country.addResource(resources.get("R23"), 10);
        country.addResource(resources.get("R21'"), 0);
        country.addResource(resources.get("R22'"), 0);
        country.addResource(resources.get("R23'"), 0);

        String assertions = setupAssertions();

        LogicalCountryModel model = new LogicalCountryModel(new Context(), country, assertions);
//        model.score(null);
    }
}

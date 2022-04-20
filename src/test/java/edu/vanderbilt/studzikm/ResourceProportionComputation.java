package edu.vanderbilt.studzikm;

import com.microsoft.z3.Context;
import org.junit.jupiter.api.Test;


import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProportionComputation {

    Double[] setupProportions() {
        return DoubleStream.of(.1, .5)
                .boxed()
                .toArray(Double[]::new);
    }

    TransformFactory setupTransforms(Map<String, Resource> resources) {
        return new DefaultTransforms(resources, setupProportions());
    }

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

    Country setupCountry(Map<String, Resource> resources, Consumer<Map.Entry<String, Resource>> calculation) {
        Country country = new Country("Self", null);
        resources.entrySet()
                .forEach(calculation::accept);
        return country;
    }

    @Test
    void testBasic() {
        Map<String, Resource> resources = setupResources();
        TransformFactory transformFact = setupTransforms(resources);
        Country country = new Country("Self", null);
        resources.entrySet()
                .forEach(r -> country.addResource(r.getValue(), 100));
        Context context = new Context();
        TargetResourceProportionComputor computor =
                new TargetResourceProportionComputor(transformFact, context);
        Map<String, Integer> proportions = computor.compute(country);

        assertEquals(100, proportions.get("R1"));
        assertEquals(3600, proportions.get("R2"));
        assertEquals(0, proportions.get("R3"));
        assertEquals(2400, proportions.get("R21"));
        assertEquals(2400, proportions.get("R22"));
        assertEquals(0, proportions.get("R23"));
        assertEquals(2400, proportions.get("R21'"));
        assertEquals(1200, proportions.get("R22'"));
        assertEquals(0, proportions.get("R23'"));
    }

    @Test
    void testHousingRequired() {
        Map<String, Resource> resources = setupResources();
        TransformFactory transformFact = setupTransforms(resources);
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

        Context context = new Context();
        TargetResourceProportionComputor computor =
                new TargetResourceProportionComputor(transformFact, context);
        Map<String, Integer> proportions = computor.compute(country);

        assertEquals(100, proportions.get("R1"));
        assertEquals(15, proportions.get("R2"));
        assertEquals(75, proportions.get("R3"));
        assertEquals(45, proportions.get("R21"));
        assertEquals(0, proportions.get("R22"));
        assertEquals(15, proportions.get("R23"));
        assertEquals(45, proportions.get("R21'"));
        assertEquals(0, proportions.get("R22'"));
        assertEquals(15, proportions.get("R23'"));
    }
}

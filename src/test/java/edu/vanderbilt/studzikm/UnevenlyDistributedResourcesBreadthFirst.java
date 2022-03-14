package edu.vanderbilt.studzikm;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnevenlyDistributedResourcesBreadthFirst {

    BiFunction<Double, Double, Double> resourceQuality =(target, actual) ->
            (actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))));
    Supplier<QualityComputation> qualityComputation = () -> new FunctionQualityCompuation(resourceQuality);
    Comparator<ScheduleItem> scheduleItemComparator = (x, y) -> Double.compare(x.getExpectedUtility(), y.getExpectedUtility());

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

    World setupWorldDistributedResources(Map<String, Resource> resources) {

        return new WorldBuilder(resources)

                .addCountry("Atlantis", qualityComputation)
                .addResource("Atlantis", "R1", 1000)
                .addResource("Atlantis", "R2", 500000) // More than 240250
                .addResource("Atlantis", "R3", 1000)

                .addCountry("Brobdingnag", qualityComputation)
                .addResource("Brobdingnag", "R1", 1000)
                .addResource("Brobdingnag", "R2", 1000)
                .addResource("Brobdingnag", "R3", 10000) // More than 1250

                .addCountry("Carpania", qualityComputation)
                .addResource("Carpania", "R1", 1000)
                .addResource("Carpania", "R2", 1000)
                .addResource("Carpania", "R3", 1000)
                .addResource("Carpania", "R21", 150000) // More than 40750

                .addCountry("Dinotopia", qualityComputation)
                .addResource("Dinotopia", "R1", 1000)
                .addResource("Dinotopia", "R2", 1000)
                .addResource("Dinotopia", "R3", 1000)
                .addResource("Dinotopia", "R22", 100000) // More than 20000

                .addCountry("Erewhon", qualityComputation)
                .addResource("Erewhon", "R1", 1000)
                .addResource("Erewhon", "R2", 1000)
                .addResource("Erewhon", "R3", 1000)
                .addResource("Erewhon", "R23", 5000)

                .addCountry("Self", qualityComputation)
                .addResource("Self", "R1", 100)
                .addResource("Self", "R2", 100)
                .addResource("Self", "R3", 100)

                .build();
    }

    private SearchBuilder setupDefaultSearchBuilder(Map<String, Resource> resources, World world) {
        Supplier<Frontier> frontierSupplier = () -> new BreadthFirstFrontier(100);
        return new SearchBuilder()
                .setGamma(1.0)
                .setFailurePenalty(0.0)
                .setLogisticGrowthRate(1.0)
                .setSigmoidMidpoint(0.0)
                .setResources(resources)
                .setInitialQualities(world)
                .setFrontierSupplier(frontierSupplier);
    }

    @Test
    void testQualityDistributeResources4Proportions() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        Search search = setupDefaultSearchBuilder(resources, world)
                .setTransferProportion(0.20)
                .setTransferProportion(0.80)
                .setTransformProportion(0.25)
                .setTransformProportion(0.75)
                .build();

        Schedule schedule = search.search(world, world.getCountry("Self"), 150);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.5316839337082582, item.getExpectedUtility());
        assertEquals(121, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources4Proportions2() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.1,
                0.5,
                175);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6558438344404257, item.getExpectedUtility());
        assertEquals(143, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources4Proportions3() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.4,
                0.5,
                150);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18388975618926362, item.getExpectedUtility());
        assertEquals(43, item.getSchedulePostion());

    }

    @Test
    void testQualityDistributeResources4Proportions4() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.3,
                0.5,
                150);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18397224949111213, item.getExpectedUtility());
        assertEquals(58, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.05,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6696008146879522, item.getExpectedUtility());
        assertEquals(13, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions2() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.025,
                0.05,
                100);


        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6663017022589491, item.getExpectedUtility());
        assertEquals(20, item.getSchedulePostion());
    }

    // Different from Heuristic depth first
    @Test
    void testQualityDistributeResources40Proportions4() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.001,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6697227923111191, item.getExpectedUtility());
        assertEquals(17, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources80Proportions() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.025,
                0.025,
                40
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.669812275237756, item.getExpectedUtility());
        assertEquals(22, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources80Proportions2() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.0125,
                0.025,
                40
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6695575726002279, item.getExpectedUtility());
        assertEquals(21, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources80Proportions3() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.001,
                0.025,
                40
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6698412082666765, item.getExpectedUtility());
        assertEquals(14, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources160ProportionsGenerated() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.0125,
                0.0125,
                40
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6694295747877037, item.getExpectedUtility());
        assertEquals(20, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources160ProportionsGenerated2() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.01,
                0.0125,
                30
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6697736202458654, item.getExpectedUtility());
        assertEquals(19, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources160ProportionsGenerated3() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.001,
                0.0125,
                30
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6698699734234771, item.getExpectedUtility());
        assertEquals(22, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions3() {
        Map<String, Resource> resources = setupResources();
        World world = setupWorldDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.035,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.6519932624303375, item.getExpectedUtility());
        assertEquals(10, item.getSchedulePostion());
    }

    private Schedule executeTest(World world,
                                 SearchBuilder builder,
                                 double prop,
                                 double step,
                                 int depth) {
        int count = 0;
        while (prop <= 1) {
            builder.setTransferProportion(prop)
                    .setTransformProportion(prop);
            prop += step;
            count++;
        }

        Search search = builder.build();
        return search.search(world, world.getCountry("Self"), depth);
    }
}

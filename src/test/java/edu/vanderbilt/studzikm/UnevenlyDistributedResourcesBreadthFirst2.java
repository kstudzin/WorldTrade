package edu.vanderbilt.studzikm;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnevenlyDistributedResourcesBreadthFirst2 {

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
                .addResource("Atlantis", "R1", 1523)
                .addResource("Atlantis", "R2", 658622) // 80% more than required
                .addResource("Atlantis", "R3", 2856)
                .addResource("Atlantis", "R21", 62063) // 50% more than required
                .addResource("Atlantis", "R22", 30460)
                .addResource("Atlantis", "R23", 381)

                .addCountry("Brobdingnag", qualityComputation)
                .addResource("Brobdingnag", "R1", 7824)
                .addResource("Brobdingnag", "R2", 1879716)
                .addResource("Brobdingnag", "R3", 17604) // 80% more than required
                .addResource("Brobdingnag", "R21", 478242)
                .addResource("Brobdingnag", "R22", 156480) // 50% more than required
                .addResource("Brobdingnag", "R23", 1956)

                .addCountry("Carpania", qualityComputation)
                .addResource("Carpania", "R1", 24928)
                .addResource("Carpania", "R2", 5988952)
                .addResource("Carpania", "R3", 31160)
                .addResource("Carpania", "R21", 1828469) // 80% more than required
                .addResource("Carpania", "R22", 747840)
                .addResource("Carpania", "R23", 6232) // 50% more than required

                .addCountry("Dinotopia", qualityComputation)
                .addResource("Dinotopia", "R1", 823729) // 50% more than required
                .addResource("Dinotopia", "R2", 1979008923)
                .addResource("Dinotopia", "R3", 1029663)
                .addResource("Dinotopia", "R21", 33566957)
                .addResource("Dinotopia", "R22", 29654244) // 80% more than required
                .addResource("Dinotopia", "R23", 308899)

                .addCountry("Erewhon", qualityComputation)
                .addResource("Erewhon", "R1", 2345682)
                .addResource("Erewhon", "R2", 845325151) // 50% more than required
                .addResource("Erewhon", "R3", 2932103)
                .addResource("Erewhon", "R21", 95586542)
                .addResource("Erewhon", "R22", 46913640)
                .addResource("Erewhon", "R23", 1055557) // 80% more than required

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

        assertEquals(0.4229296918121439, item.getExpectedUtility());
        assertEquals(10, item.getSchedulePostion());
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

        assertEquals(0.6628343776477429, item.getExpectedUtility());
        assertEquals(20, item.getSchedulePostion());
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

        assertEquals(0.09064185469673969, item.getExpectedUtility());
        assertEquals(5, item.getSchedulePostion());

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

        assertEquals(0.04302715541572055, item.getExpectedUtility());
        assertEquals(4, item.getSchedulePostion());
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

        assertEquals(0.6506685257246263, item.getExpectedUtility());
        assertEquals(25, item.getSchedulePostion());
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

        assertEquals(0.6456245934002134, item.getExpectedUtility());
        assertEquals(10, item.getSchedulePostion());
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

        assertEquals(0.6698697974875831, item.getExpectedUtility());
        assertEquals(18, item.getSchedulePostion());
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

        assertEquals(0.6456245934002134, item.getExpectedUtility());
        assertEquals(9, item.getSchedulePostion());
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

        assertEquals(0.6696360219475668, item.getExpectedUtility());
        assertEquals(14, item.getSchedulePostion());
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
                50
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.335497343614354, item.getExpectedUtility());
        assertEquals(33, item.getSchedulePostion());
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

        assertEquals(0.6693703305219072, item.getExpectedUtility());
        assertEquals(13, item.getSchedulePostion());
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

        assertEquals(0.6696433370333517, item.getExpectedUtility());
        assertEquals(18, item.getSchedulePostion());
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

        assertEquals(0.6698634127386845, item.getExpectedUtility());
        assertEquals(15, item.getSchedulePostion());
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

        assertEquals(0.6626074522351263, item.getExpectedUtility());
        assertEquals(8, item.getSchedulePostion());
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

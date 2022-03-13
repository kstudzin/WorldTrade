package edu.vanderbilt.studzikm;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EvenlyDistributedResourcesTest {

    Comparator<ScheduleItem> scheduleItemComparator = (x,y) -> Double.compare(x.getExpectedUtility(), y.getExpectedUtility());

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

    private World setupEvenlyDistributedResources(Map<String, Resource> resources) {
        Supplier<QualityComputation> qualityComputation = () -> new FunctionQualityCompuation();
        return new WorldBuilder(resources)

                .addCountry("Self", qualityComputation)
                .addResource("Self", "R1", 1000)
                .addResource("Self", "R2", 1000)
                .addResource("Self", "R3", 1000)

                .addCountry("Atlantis", qualityComputation)
                .addResource("Atlantis", "R1", 1000)
                .addResource("Atlantis", "R2", 1000)
                .addResource("Atlantis", "R3", 1000)

                .addCountry("Brobdingnag", qualityComputation)
                .addResource("Brobdingnag", "R1", 1000)
                .addResource("Brobdingnag", "R2", 1000)
                .addResource("Brobdingnag", "R3", 1000)

                .addCountry("Carpania", qualityComputation)
                .addResource("Carpania", "R1", 1000)
                .addResource("Carpania", "R2", 1000)
                .addResource("Carpania", "R3", 1000)

                .addCountry("Dinotopia", qualityComputation)
                .addResource("Dinotopia", "R1", 1000)
                .addResource("Dinotopia", "R2", 1000)
                .addResource("Dinotopia", "R3", 1000)

                .addCountry("Erewhon", qualityComputation)
                .addResource("Erewhon", "R1", 1000)
                .addResource("Erewhon", "R2", 1000)
                .addResource("Erewhon", "R3", 1000)
                .build();
    }

    private SearchBuilder setupDefaultSearchBuilder(Map<String, Resource> resources, World world) {
        return new SearchBuilder()
                .setGamma(1.0)
                .setFailurePenalty(0.0)
                .setLogisticGrowthRate(1.0)
                .setSigmoidMidpoint(0.0)
                .setResources(resources)
                .setInitialQualities(world)
                .setFrontierSupplier(HeuristicDepthFirstFrontier::new);
    }

    @Test
    void testQualityDistributeResources20Proportions() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.1,
                0.1,
                100
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18478548621168678, item.getExpectedUtility());
        assertEquals(20, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources20Proportions2() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.05,
                0.1,
                100
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18849417081074527, item.getExpectedUtility());
        assertEquals(52, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources20Proportions3() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.025,
                0.1,
                100
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18846424196685008, item.getExpectedUtility());
        assertEquals(52, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources20Proportions4() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.0125,
                0.1,
                100
        );


        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18847449570487929, item.getExpectedUtility());
        assertEquals(55, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.05,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18847653535490638, item.getExpectedUtility());
        assertEquals(51, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions2() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.025,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.1884924419481553, item.getExpectedUtility());
        assertEquals(43, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions3() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.035,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18846209543688652, item.getExpectedUtility());
        assertEquals(43, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources40Proportions4() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.001,
                0.05,
                100);

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18848906164257517, item.getExpectedUtility());
        assertEquals(52, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources80Proportions() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.025,
                0.025,
                75
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.18847471512970937, item.getExpectedUtility());
        assertEquals(40, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources80Proportions2() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.0125,
                0.025,
                75
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.1884606003257404, item.getExpectedUtility());
        assertEquals(44, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources80Proportions3() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.001,
                0.025,
                75
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.1884775986359174, item.getExpectedUtility());
        assertEquals(42, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources160ProportionsGenerated() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.0125,
                0.0125,
                50
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.1875151374757017, item.getExpectedUtility());
        assertEquals(22, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources160ProportionsGenerated2() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.01,
                0.0125,
                75
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.1882940671106511, item.getExpectedUtility());
        assertEquals(39, item.getSchedulePostion());
    }

    @Test
    void testQualityDistributeResources160ProportionsGenerated3() {
        Map<String, Resource> resources = setupResources();
        World world = setupEvenlyDistributedResources(resources);
        SearchBuilder builder = setupDefaultSearchBuilder(resources, world);

        Schedule schedule = executeTest(world,
                builder,
                0.001,
                0.0125,
                75
        );

        ScheduleItem item = schedule.stream()
                .max(scheduleItemComparator)
                .orElse(null);

        assertEquals(0.1884606003257404, item.getExpectedUtility());
        assertEquals(45, item.getSchedulePostion());
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

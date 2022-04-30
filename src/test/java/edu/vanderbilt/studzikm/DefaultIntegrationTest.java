package edu.vanderbilt.studzikm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.vanderbilt.studzikm.ScheduleItem.Type;

public class DefaultIntegrationTest {

	Map<String, Resource> resources;
	World world;
	Search search;

	@BeforeEach
	void setUp() throws Exception {
		resources = setUpResources();
		world = setUpWorld();
		search = setUpSearch();
	}

	private Search setUpSearch() {
		SearchBuilder builder = new SearchBuilder()
				.setTransferProportion(0.5)
				.setTransferProportion(1.0)
				.setTransformProportion(0.33)
				.setTransformProportion(0.66)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.setReachedSupplier(NullReached::new);
		return builder.build();
	}

	private World setUpWorld() {
		Supplier<QualityComputation> qualityComputation = LinearQualityComputation::new;

		return new WorldBuilder(resources)

				.addCountry("Atlantis", qualityComputation)
				.addResource("Atlantis", "R1", 100)
				.addResource("Atlantis", "R2", 700)
				.addResource("Atlantis", "R3", 2000)

				.addCountry("Brobdingnag", qualityComputation)
				.addResource("Brobdingnag", "R1", 50)
				.addResource("Brobdingnag", "R2", 300)
				.addResource("Brobdingnag", "R3", 1200)

				.addCountry("Carpania", qualityComputation)
				.addResource("Carpania", "R1", 25)
				.addResource("Carpania", "R2",	100)
				.addResource("Carpania", "R3", 300)

				.addCountry("Dinotopia", qualityComputation)
				.addResource("Dinotopia", "R1", 30)
				.addResource("Dinotopia", "R2", 200)
				.addResource("Dinotopia", "R3", 200)

				.addCountry("Erewhon", qualityComputation)
				.addResource("Erewhon", "R1", 70)
				.addResource("Erewhon", "R2", 500)
				.addResource("Erewhon", "R3", 1700)
				.build();
	}

	private Map<String, Resource> setUpResources() {
		return new ResourcesBuilder()
				.addResource("R1", 1.0)
				.addResource("R2", 1.0)
				.addResource("R3", 1.0)
				.addResource("R21", 0.2)
				.addResource("R22", 0.5)
				.addResource("R23", 0.8)
				.addResource("R21'", -0.5)
				.addResource("R22'", -0.8)
				.addResource("R23'", -0.4)
				.build();
	}

	@Test
	void testBasicSetup() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 1);

		assertEquals(1, searchResult.size());

		ScheduleItem item = searchResult.get(0);
		assertEquals("Erewhon", item.getFirstName());
		assertEquals(4500, item.getSelfQuality());
		assertEquals(1700, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		// TODO check inputs and expected utility
		assertEquals("Atlantis", item.getSecondName());
	}

	@Test
	void testBasicSetupDepth2() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 2);

		assertEquals(2, searchResult.size());

		ScheduleItem item = searchResult.get(1);
		assertEquals("Brobdingnag", item.getFirstName());
		assertEquals(5700, item.getSelfQuality());
		assertEquals(2900, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		// TODO check inputs and expected utility
		assertEquals("Atlantis", item.getSecondName());
		assertEquals(0, item.getExpectedUtility());
	}

	@Test
	void testBasicSetupDepth3() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 3);

		assertEquals(3, searchResult.size());

		ScheduleItem actionResult = searchResult.get(2);
		assertEquals("Erewhon", actionResult.getFirstName());
		assertEquals(6200, actionResult.getSelfQuality());
		assertEquals(3400, actionResult.getSelfReward());
		assertEquals(Type.TRANSFER, actionResult.getType());
		assertEquals("Atlantis", actionResult.getSecondName());
	}

	@Test
	void testBasicSetupDepth4() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 4);

		assertEquals(4, searchResult.size());

		ScheduleItem item = searchResult.get(3);
		assertEquals("Carpania", item.getFirstName());
		assertEquals(6500, item.getSelfQuality());
		assertEquals(3700, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		assertEquals("Atlantis", item.getSecondName());
	}

}

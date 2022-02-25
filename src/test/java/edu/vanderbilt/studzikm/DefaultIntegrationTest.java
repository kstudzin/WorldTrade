package edu.vanderbilt.studzikm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.vanderbilt.studzikm.ScheduleItem.Type;

public class DefaultIntegrationTest {

	Map<String, Resource> resources;
	World world;
	TransferFactory transfers;
	StateGenerator stateGenerator;
	SearchNodeFactory nodeFactory;
	ScheduleFactory scheduleFactory;
	Search search;

	@BeforeEach
	void setUp() throws Exception {
		resources = setUpResources();
		world = setUpWorld();
		transfers = setUpTransfers();
		stateGenerator = setUpStateGenerator();
		nodeFactory = setUpNodeFactory();
		scheduleFactory = setUpScheduleFactory();
		search = setUpSearch();
	}

	private ScheduleFactory setUpScheduleFactory() {
		SuccessProbabilityComputation successProbabilityComputation = new SuccessProbabilityComputation(1, 0);
		ExpectedUtilityComputation expectedUtilityComputation = new ExpectedUtilityComputation(0, successProbabilityComputation);
		ScheduleFactory scheduleFactory = new ScheduleFactory(expectedUtilityComputation);
		return scheduleFactory;
	}

	private SearchNodeFactory setUpNodeFactory() {
		return new SearchNodeFactory();
	}

	private Search setUpSearch() {
		return new Search(stateGenerator, nodeFactory, scheduleFactory);
	}

	private StateGenerator setUpStateGenerator() {
		Map<String, Double> initialQualities = world.stream()
				.collect(Collectors.toMap(Country::getName, Country::computeQuality));

		RewardComputation rewardComp = 
				new DiscountedRewardComputationBuilder()
				.setGamma(1)
				.setInitialQualities(initialQualities)
				.build();

		return new DefaultStateGenerator(resources, transfers, rewardComp);
	}

	private TransferFactory setUpTransfers() {
		Double[] allowedTradePercent = {0.5, 1.0};
		return new DefaultTransfers(resources, allowedTradePercent);
	}

	private World setUpWorld() {
		World world = new World();

		Country atlantis = new Country("Atlantis", new DefaultQualityComputation());
		atlantis.addResource(resources.get("R1"), 100);
		atlantis.addResource(resources.get("R2"), 700);
		atlantis.addResource(resources.get("R3"), 2000);
		world.addCountry(atlantis);

		Country brobdingnag = new Country("Brobdingnag", new DefaultQualityComputation());
		brobdingnag.addResource(resources.get("R1"), 50);
		brobdingnag.addResource(resources.get("R2"), 300);
		brobdingnag.addResource(resources.get("R3"), 1200);
		world.addCountry(brobdingnag);

		Country carpania = new Country("Carpania", new DefaultQualityComputation());
		carpania.addResource(resources.get("R1"), 25);
		carpania.addResource(resources.get("R2"), 100);
		carpania.addResource(resources.get("R3"), 300);
		world.addCountry(carpania);

		Country dinotopia = new Country("Dinotopia", new DefaultQualityComputation());
		dinotopia.addResource(resources.get("R1"), 30);
		dinotopia.addResource(resources.get("R2"), 200);
		dinotopia.addResource(resources.get("R3"), 200);
		world.addCountry(dinotopia);

		Country erewhon = new Country("Erewhon", new DefaultQualityComputation());
		erewhon.addResource(resources.get("R1"), 70);
		erewhon.addResource(resources.get("R2"), 500);
		erewhon.addResource(resources.get("R3"), 1700);
		world.addCountry(erewhon);

		return world;
	}

	private Map<String, Resource> setUpResources() {
		Map<String, Resource> resources = new HashMap<>();

		resources.put("R1", new Resource("R1", 1.0));
		resources.put("R2", new Resource("R2", 1.0));
		resources.put("R3", new Resource("R3", 1.0));
		resources.put("R21", new Resource("R21", 0.2));
		resources.put("R22", new Resource("R22", 0.5));
		resources.put("R23", new Resource("R23", 0.8));
		resources.put("R21'", new Resource("R21'", -0.5));
		resources.put("R22'", new Resource("R22'", -0.8));
		resources.put("R23'", new Resource("R23'", -0.4));

		return resources;
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

		ScheduleItem item = searchResult.get(0);
		assertEquals("Brobdingnag", item.getFirstName());
		assertEquals(5700, item.getSelfQuality());
		assertEquals(2900, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		// TODO check inputs and expected utility
		assertEquals("Atlantis", item.getSecondName());
	}

	@Test
	void testBasicSetupDepth3() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 3);

		assertEquals(3, searchResult.size());

		ScheduleItem actionResult = searchResult.get(0);
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

		ScheduleItem item = searchResult.get(0);
		assertEquals("Carpania", item.getFirstName());
		assertEquals(6500, item.getSelfQuality());
		assertEquals(3700, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		assertEquals("Atlantis", item.getSecondName());
	}

}

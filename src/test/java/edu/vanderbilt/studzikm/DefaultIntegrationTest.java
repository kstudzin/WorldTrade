package edu.vanderbilt.studzikm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultIntegrationTest {

	Map<String, Resource> resources;
	World world;
	TransferFactory transfers;
	StateGenerator stateGenerator;
	SearchNodeFactory nodeFactory;
	Search search;

	@BeforeEach
	void setUp() throws Exception {
		resources = setUpResources();
		world = setUpWorld();
		transfers = setUpTransfers();
		stateGenerator = setUpStateGenerator();
		nodeFactory = setUpNodeFactory();
		search = setUpSearch();
	}

	private SearchNodeFactory setUpNodeFactory() {
		return new SearchNodeFactory();
	}

	private Search setUpSearch() {
		return new Search(stateGenerator, nodeFactory);
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
		List<ActionResult<?>> searchResult = search.search(world, world.getCountry("Atlantis"), 1);

		assertEquals(1, searchResult.size());

		ActionResult<?> actionResult = searchResult.get(0);
		assertEquals("Atlantis", actionResult.self.getName());
		assertEquals(4500, actionResult.getQuality());
		assertEquals(1700, actionResult.getReward());
		assertEquals("Transfer [resource=Resource [name=R3, weight=1.0], percent=1.0]", actionResult.action.toString());
		assertEquals("Erewhon", ((TransferResult)actionResult).getOther().getName());
	}

	@Test
	void testBasicSetupDepth2() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		List<ActionResult<?>> searchResult = search.search(world, world.getCountry("Atlantis"), 2);

		assertEquals(2, searchResult.size());

		ActionResult<?> actionResult = searchResult.get(0);
		assertEquals("Atlantis", actionResult.self.getName());
		assertEquals(5700, actionResult.getQuality());
		assertEquals(2900, actionResult.getReward());
		assertEquals("Transfer [resource=Resource [name=R3, weight=1.0], percent=1.0]", actionResult.action.toString());
		assertEquals("Brobdingnag", ((TransferResult)actionResult).getOther().getName());
	}

	@Test
	void testBasicSetupDepth3() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		List<ActionResult<?>> searchResult = search.search(world, world.getCountry("Atlantis"), 3);

		assertEquals(3, searchResult.size());

		ActionResult<?> actionResult = searchResult.get(0);
		assertEquals("Atlantis", actionResult.self.getName());
		assertEquals(6200, actionResult.getQuality());
		assertEquals(3400, actionResult.getReward());
		assertEquals("Transfer [resource=Resource [name=R2, weight=1.0], percent=1.0]", actionResult.action.toString());
		assertEquals("Erewhon", ((TransferResult)actionResult).getOther().getName());
	}

	@Test
	void testBasicSetupDepth4() {
		assertEquals(2800, world.getCountry("Atlantis").computeQuality());
		List<ActionResult<?>> searchResult = search.search(world, world.getCountry("Atlantis"), 4);

		assertEquals(4, searchResult.size());

		ActionResult<?> actionResult = searchResult.get(0);
		assertEquals("Atlantis", actionResult.self.getName());
		assertEquals(6500, actionResult.getQuality());
		assertEquals(3700, actionResult.getReward());
		assertEquals("Transfer [resource=Resource [name=R2, weight=1.0], percent=1.0]", actionResult.action.toString());
		assertEquals("Brobdingnag", ((TransferResult)actionResult).getOther().getName());
	}

}

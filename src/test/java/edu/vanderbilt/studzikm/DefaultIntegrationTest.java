package edu.vanderbilt.studzikm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		DiscountedRewardComputationBuilder rewardCompBuilder = new DiscountedRewardComputationBuilder()
				.setGamma(.9);
		return new SearchNodeFactory(rewardCompBuilder);
	}

	private Search setUpSearch() {
		return new Search(stateGenerator, nodeFactory);
	}

	private StateGenerator setUpStateGenerator() {
		return new DefaultStateGenerator(resources, transfers);
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
		resources.put("R21", new Resource("R21", 1.0));
		resources.put("R22", new Resource("R22", 1.0));
		resources.put("R23", new Resource("R23", 1.0));
		resources.put("R21'", new Resource("R21'", 1.0));
		resources.put("R22'", new Resource("R22'", 1.0));
		resources.put("R23'", new Resource("R23'", 1.0));

		return resources;
	}

	@Test
	void testBasicSetup() {
		List<ActionResult<?>> searchResult = search.search(world, world.getCountry("Atlantis"), 1);

		assertEquals(1, searchResult.size());

		ActionResult<?> actionResult = searchResult.get(0);
		assertEquals("Atlantis", actionResult.performer.getName());
		assertEquals(2800, actionResult.getQuality());
		assertEquals(-280, actionResult.getReward());
		assertEquals("Transform [name=alloys]", actionResult.transform.toString());
	}
}

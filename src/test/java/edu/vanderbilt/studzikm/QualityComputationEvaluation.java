package edu.vanderbilt.studzikm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import edu.vanderbilt.studzikm.ScheduleItem.Type;

public class QualityComputationEvaluation {

	@Test
	void testEqualResourceWeights() {
		Map<String, Resource> resources = new ResourcesBuilder()
				.addResource("R1", .11)
				.addResource("R2", .11)
				.addResource("R3", .11)
				.addResource("R21", .11)
				.addResource("R22", .11)
				.addResource("R23", .11)
				.addResource("R21'", .11)
				.addResource("R22'", .11)
				.addResource("R23'", .11)
				.build();

		Supplier<QualityComputation> qualityComputation = DefaultQualityComputation::new;
		World world = new WorldBuilder(resources)

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

		Search search = new SearchBuilder()
				.setTransferProportion(0.05)
				.setTransferProportion(0.10)
				.setTransformProportion(0.125)
				.setTransformProportion(0.250)
				.setTransformProportion(0.500)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.build();

		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 1);

		assertEquals(1, searchResult.size());

		ScheduleItem item = searchResult.get(0);
		assertEquals("Erewhon", item.getFirstName());
		assertEquals(341, item.getSelfQuality());
		assertEquals(11, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		// TODO check inputs and expected utility
		assertEquals("Atlantis", item.getSecondName());
	}

	@Test
	void testUnweightedWasteResourceWeights() {
		Map<String, Resource> resources = new ResourcesBuilder()
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

		Supplier<QualityComputation> qualityComputation = DefaultQualityComputation::new;
		World world = new WorldBuilder(resources)

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

		Search search = new SearchBuilder()
				.setTransferProportion(0.05)
				.setTransferProportion(0.10)
				.setTransformProportion(0.125)
				.setTransformProportion(0.250)
				.setTransformProportion(0.500)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.build();

		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 1);

		assertEquals(1, searchResult.size());

		ScheduleItem item = searchResult.get(0);
		assertEquals("Brobdingnag", item.getFirstName());
		assertEquals(341, item.getSelfQuality());
		assertEquals(11, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		// TODO check inputs and expected utility
		assertEquals("Atlantis", item.getSecondName());
	}

	@Test
	void testFunctionQuality() {
		Map<String, Resource> resources = new ResourcesBuilder()
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

		BiFunction<Double, Double, Double> resourceQuality = (target, actual) -> {
			return (actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))));
		};

		Supplier<QualityComputation> qualityComputation = () -> new FunctionQualityCompuation(resourceQuality);
		World world = new WorldBuilder(resources)

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

		Search search = new SearchBuilder()
				.setTransferProportion(0.10)
				.setTransferProportion(0.20)
				.setTransferProportion(0.50)
				.setTransferProportion(0.80)		
				.setTransformProportion(0.250)
				.setTransformProportion(0.500)
				.setTransformProportion(0.750)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.build();

		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 30);

		assertEquals(30, searchResult.size());

		for (ScheduleItem item : searchResult) {
			System.out.println(item);
		}

		ScheduleItem item = searchResult.get(29);
		assertEquals("Dinotopia", item.getFirstName());
		assertEquals("Atlantis", item.getSecondName());
		assertEquals(0.5087535899052975, item.getSelfQuality());
		assertEquals(0.18262395504993556, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		assertEquals(0.0882204864203834, item.getExpectedUtility());
		
		item = searchResult.get(0);
		assertEquals("Erewhon", item.getFirstName());
		assertEquals("Atlantis", item.getSecondName());
		assertEquals(0.332870136186307, item.getSelfQuality());
		assertEquals(0.006740501330945059, item.getSelfReward());
		assertEquals(Type.TRANSFER, item.getType());
		assertEquals(0.0033428373480349476, item.getExpectedUtility());
		// TODO check inputs

	}

	@Test
	void testFunctionQualityOnlyTransforms() {
		Map<String, Resource> resources = new ResourcesBuilder()
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

		BiFunction<Double, Double, Double> resourceQuality = (target, actual) -> {
			return (actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))));
		};

		Supplier<QualityComputation> qualityComputation = () -> new FunctionQualityCompuation(resourceQuality);
		World world = new WorldBuilder(resources)

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

		Search search = new SearchBuilder()
				.setTransferProportion(0.00)
				.setTransformProportion(0.250)
				.setTransformProportion(0.500)
				.setTransformProportion(0.750)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.build();

		Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 30);

		assertEquals(9, searchResult.size());

		for (ScheduleItem item : searchResult) {
			System.out.println(item.toExpectedUtilityString());
		}

		assertEquals("0.0016641176474809227,TRANSFORM", searchResult.get(0).toExpectedUtilityString());
		assertEquals("0.042585527503448906,TRANSFORM", searchResult.get(1).toExpectedUtilityString());
		assertEquals("0.042964196467033866,TRANSFORM", searchResult.get(2).toExpectedUtilityString());
		assertEquals("0.04306266094737954,TRANSFORM", searchResult.get(3).toExpectedUtilityString());
		assertEquals("0.04313366739590396,TRANSFORM", searchResult.get(4).toExpectedUtilityString());
		assertEquals("0.04315470370749519,TRANSFORM", searchResult.get(5).toExpectedUtilityString());
		assertEquals("0.04316522181509902,TRANSFORM", searchResult.get(6).toExpectedUtilityString());
		assertEquals("0.04316689240632182,TRANSFORM", searchResult.get(7).toExpectedUtilityString());
		assertEquals("0.043159380269112924,TRANSFORM", searchResult.get(8).toExpectedUtilityString());

	}

	@Test
	void testFunctionQualityDistributeResourcesFewStatesGenerated() {
		Map<String, Resource> resources = new ResourcesBuilder()
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

		BiFunction<Double, Double, Double> resourceQuality = (target, actual) -> {
			return (actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))));
		};

		Supplier<QualityComputation> qualityComputation = () -> new FunctionQualityCompuation(resourceQuality);
		World world = new WorldBuilder(resources)

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

		Search search = new SearchBuilder()
				.setTransferProportion(0.20)
				.setTransferProportion(0.80)
				.setTransformProportion(0.25)
				.setTransformProportion(0.75)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.build();

		Schedule searchResult = search.search(world, world.getCountry("Self"), 150);

		for (ScheduleItem item : searchResult) {
			System.out.println(item);
		}

	}

	@Test
	void testFunctionQualityDistributeResourcesManyStatesGenerated() {
		Map<String, Resource> resources = new ResourcesBuilder()
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

		BiFunction<Double, Double, Double> resourceQuality = (target, actual) -> {
			return (actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))));
		};

		Supplier<QualityComputation> qualityComputation = () -> new FunctionQualityCompuation(resourceQuality);
		World world = new WorldBuilder(resources)

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

		Search search = new SearchBuilder()
				.setTransferProportion(0.05)
				.setTransferProportion(0.10)
				.setTransferProportion(0.15)
				.setTransferProportion(0.20)
				.setTransferProportion(0.25)
				.setTransferProportion(0.30)
				.setTransferProportion(0.35)
				.setTransferProportion(0.40)
				.setTransferProportion(0.45)
				.setTransferProportion(0.50)
				.setTransferProportion(0.55)
				.setTransferProportion(0.60)
				.setTransferProportion(0.65)
				.setTransferProportion(0.70)
				.setTransferProportion(0.75)
				.setTransferProportion(0.80)
				.setTransferProportion(0.85)
				.setTransferProportion(0.90)
				.setTransferProportion(0.95)
				.setTransferProportion(1.00)
				.setTransformProportion(0.05)
				.setTransformProportion(0.10)
				.setTransformProportion(0.15)
				.setTransformProportion(0.20)
				.setTransformProportion(0.25)
				.setTransformProportion(0.30)
				.setTransformProportion(0.35)
				.setTransformProportion(0.40)
				.setTransformProportion(0.45)
				.setTransformProportion(0.50)
				.setTransformProportion(0.55)
				.setTransformProportion(0.60)
				.setTransformProportion(0.65)
				.setTransformProportion(0.70)
				.setTransformProportion(0.75)
				.setTransformProportion(0.80)
				.setTransformProportion(0.85)
				.setTransformProportion(0.90)
				.setTransformProportion(0.95)
				.setTransformProportion(1.00)
				.setGamma(1.0)
				.setFailurePenalty(0.0)
				.setLogisticGrowthRate(1.0)
				.setSigmoidMidpoint(0.0)
				.setResources(resources)
				.setInitialQualities(world)
				.setFrontierSupplier(HeuristicDepthFirstFrontier::new)
				.build();

		Schedule searchResult = search.search(world, world.getCountry("Self"), 100);

		for (ScheduleItem item : searchResult) {
			System.out.println(item);
		}

	}
}

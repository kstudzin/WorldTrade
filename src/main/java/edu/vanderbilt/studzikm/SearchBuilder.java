package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.Planner;
import edu.vanderbilt.studzikm.planning.rdf.RdfPlanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SearchBuilder {

	Supplier<Frontier> frontierSupplier;
	Supplier<Reached> reachedSupplier = NullReached::new;
	List<Double> transferProportions = new ArrayList<>();
	List<Double> transformProportions = new ArrayList<>();
	Map<String, Double> initialQualities = new HashMap<>();
	Map<String, Resource> resources;
	Double gamma;
	Double failurePenalty;
	Double logisticGrowthRate;
	Double sigmoidMidpoint;
	Planner planner;

	public SearchBuilder() {
		
	}

	public SearchBuilder setFrontierSupplier(Supplier<Frontier> frontierSupplier) {
		this.frontierSupplier = frontierSupplier;
		return this;
	}

	public SearchBuilder setReachedSupplier(Supplier<Reached> reachedSupplier) {
		this.reachedSupplier = reachedSupplier;
		return this;
	}

	public SearchBuilder setTransferProportion(double proportion) {
		this.transferProportions.add(proportion);
		return this;
	}

	public SearchBuilder setTransformProportion(double proportion) {
		this.transformProportions.add(proportion);
		return this;
	}

	public SearchBuilder setGamma(Double gamma) {
		this.gamma = gamma;
		return this;
	}

	public SearchBuilder addInitialQuality(Country country) {
		initialQualities.put(country.getName(), country.computeQuality());
		return this;
	}

	public SearchBuilder setInitialQualities(World world) {
		initialQualities = world.stream()
				.collect(Collectors.toMap(Country::getName, Country::computeQuality));
		return this;
	}

	public SearchBuilder setResources(Map<String, Resource> resources) {
		this.resources = resources;
		return this;
	}

	public SearchBuilder setFailurePenalty(Double failurePenalty) {
		this.failurePenalty = failurePenalty;
		return this;
	}

	public SearchBuilder setLogisticGrowthRate(Double logisticGrowthRate) {
		this.logisticGrowthRate = logisticGrowthRate;
		return this;
	}

	public SearchBuilder setSigmoidMidpoint(Double sigmoidMidpoint) {
		this.sigmoidMidpoint = sigmoidMidpoint;
		return this;
	}

	public SearchBuilder setRdfPlannerOntology(String ontologyFilename) {
		this.planner = new RdfPlanner(ontologyFilename);
		return this;
	}

	public Search build() {

		Double[] transferProportions = new Double[this.transferProportions.size()];
		this.transferProportions.toArray(transferProportions);
		TransferFactory transferFactory = new DefaultTransfers(resources, transferProportions);

		Double[] transformProportions = new Double[this.transformProportions.size()];
		this.transformProportions.toArray(transformProportions);
		TransformFactory transformFactory = new DefaultTransforms(resources, transformProportions);

		RewardComputation rewardComputation = new DiscountedRewardComputation(gamma, initialQualities);
		StateGenerator stateGenerator = new DefaultStateGenerator(transferFactory, transformFactory, rewardComputation);
		SearchNodeFactory nodeFactory = new SearchNodeFactory();

		SuccessProbabilityComputation successProbabilityComputation = new SuccessProbabilityComputation(
				logisticGrowthRate, 
				sigmoidMidpoint);
		ExpectedUtilityComputation expectedUtilityComputation = new ExpectedUtilityComputation(
				failurePenalty, 
				successProbabilityComputation,
				planner);
		ScheduleFactory scheduleFactory = new ScheduleFactory(expectedUtilityComputation);

		return new Search(stateGenerator, nodeFactory, frontierSupplier.get(), reachedSupplier.get(), scheduleFactory);
	}
}

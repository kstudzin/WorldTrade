package edu.vanderbilt.studzikm;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class Driver {

	public static void main(String[] args) {
		File countryFile = new File(args[0]);
		File resourceFile = new File(args[1]);

		System.out.printf("Resource file name: %s\n", resourceFile);
		System.out.printf("Country file name:  %s\n", countryFile);

		try {
			Map<String, Resource> resources = ResourceFactory.importResources(resourceFile);
			System.out.printf("\nResource list: \n%s\n", resources);

			World world = CountryParser.createWorld(countryFile, resources);
			System.out.printf("\nInitial World State: %s\n", world);

			System.out.printf("\nInitial country utility: \n%s\n", world.stream()
					.map(country -> new StringBuilder("\t")
							.append(country.getName())
							.append(" ")
							.append(country.computeQuality()))
					.collect(Collectors.joining("\n")));

			Double[] allowedTradePercent = {0.5, 1.0};
			TransferFactory transferFactory = new DefaultTransfers(resources, allowedTradePercent);

			Map<String, Double> initialQualitites = world.stream()
					.collect(Collectors.toMap(Country::getName, Country::computeQuality));
			RewardComputation rewardComputation = 
					new DiscountedRewardComputationBuilder()
					.setGamma(1)
					.setInitialQualities(initialQualitites)
					.build();
			StateGenerator generator = new DefaultStateGenerator(resources, 
					transferFactory, 
					rewardComputation);

			SearchNodeFactory nodeFactory = new SearchNodeFactory();

			SuccessProbabilityComputation successProbabilityComputation = new SuccessProbabilityComputation(1, 0);
			ExpectedUtilityComputation expectedUtilityComputation = new ExpectedUtilityComputation(-0.2, 
					successProbabilityComputation);
			ScheduleFactory scheduleFactory = new ScheduleFactory(expectedUtilityComputation);

			Search search = new Search(generator, nodeFactory, scheduleFactory);
			Schedule searchResult = search.search(world, world.getCountry("Atlantis"), 7);

			System.out.println(searchResult);


		} catch (IOException e) {
			System.out.printf("\nCould not parse resource file %s%n", resourceFile);
		}

		System.out.printf("\n\nExiting.\n");

	}
}

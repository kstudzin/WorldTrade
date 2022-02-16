package edu.vanderbilt.studzikm;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
							.append(country.computeUtility(world)))
					.collect(Collectors.joining("\n")));

			Double[] allowedTradePercent = {0.5, 1.0};
			TransferFactory transferFactory = new DefaultTransfers(resources, allowedTradePercent);
			StateGenerator generator = new DefaultStateGenerator(resources, transferFactory);

			System.out.printf("\nBefore: \n%s\n", world.getCountry("Atlantis"));
			Search search = new Search(generator, world);

			List<ActionResult<?>> searchResult = search.search(world.getCountry("Atlantis"), 1);
			System.out.printf("\nAfter:  \n%s\n", searchResult.get(0).getWorld().getCountry("Atlantis"));

			System.out.printf("\nFound best state: \n%s\n", searchResult);

		} catch (IOException e) {
			System.out.printf("\nCould not parse resource file %s%n", resourceFile);
		}

		System.out.printf("\n\nExiting.\n");

	}
}

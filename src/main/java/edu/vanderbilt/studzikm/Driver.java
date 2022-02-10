package edu.vanderbilt.studzikm;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Driver {

	public static void main(String[] args) {
		File countryFile = new File(args[0]);
		File resourceFile = new File(args[1]);

		System.out.println("Resource file name: " + resourceFile);
		System.out.println("Country file name: " + countryFile);

		try {
			Map<String, Resource> resources = ResourceFactory.importResources(resourceFile);
			System.out.println(resources);

			World world = CountryParser.createWorld(countryFile, resources);

			System.out.println("\nInitial World State: ");
			System.out.println(world);

			System.out.println("\nInitial country utility: ");
			world.stream()
			.forEach(c -> System.out.println("\t" + c.getName() + " " + c.computeUtility(world)));

			Double[] allowedTradePercent = {0.5, 1.0};
			TransferFactory transferFactory = new DefaultTransfers(resources, allowedTradePercent);
			StateGenerator generator = new DefaultStateGenerator(resources, transferFactory);
			Search search = new Search(generator, world);

			System.out.println("\nBefore: " + world.getCountry("Atlantis"));
			List<ActionResult<?>> searchResult = search.search(world.getCountry("Atlantis"), 1000, 1);
			System.out.println("\nAfter:  " + searchResult.get(0).getWorld().getCountry("Atlantis"));

			System.out.println("Found best state: " + searchResult);

		} catch (IOException e) {
			System.out.printf("Could not parse resource file %s%n", resourceFile);
		}

	}
}

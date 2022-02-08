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
			System.out.println(world);

			Double[] allowedTradePercent = {0.5, 1.0};
			TransferFactory transferFactory = new DefaultTransfers(resources, allowedTradePercent);
			StateGenerator generator = new DefaultStateGenerator(resources, transferFactory);
			Search search = new Search(generator, world);

			System.out.println("Before: " + world.getCountry("Atlantis"));
			List<Action> searchResult = search.search(world.getCountry("Atlantis"), 1000, 1);
			System.out.println("After:  " + world.getCountry("Atlantis"));

			System.out.println("Found best state: " + searchResult);

		} catch (IOException e) {
			System.out.printf("Could not parse resource file %s%n", resourceFile);
		}

	}
}

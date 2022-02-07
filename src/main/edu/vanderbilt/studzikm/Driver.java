package edu.vanderbilt.studzikm;

import java.io.File;
import java.io.IOException;
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

			for (Country country : world) {
				System.out.println(country);
				boolean result = DefaultTransforms.HOUSING_TRANSFORM.transform(country);
				if (result) {
					System.out.println(country);
				} else {
					
				}
			}

		} catch (IOException e) {
			System.out.printf("Could not parse resource file %s%n", resourceFile);
		}
		
	}
}

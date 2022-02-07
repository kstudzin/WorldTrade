package edu.vanderbilt.studzikm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class CountryParser {

	public static World createWorld(File countryFile, Map<String, Resource> resources) throws IOException {
		World world = new World();
		try (BufferedReader reader = new BufferedReader(new FileReader(countryFile))) {
			String line = reader.readLine(); //  header line
			String[] headers = line.split(",");
			
			while((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				
				Country country = new Country(values[0]);
				world.addCountry(country);
				
				for (int i = 1; i < values.length; i++) {
					Resource resource = resources.get(headers[i]);
					country.addResource(resource, Integer.valueOf(values[i]));
				}
			}
		}

		return world;
	}
}

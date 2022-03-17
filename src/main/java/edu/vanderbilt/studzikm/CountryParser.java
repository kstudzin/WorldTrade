package edu.vanderbilt.studzikm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

public class CountryParser {

	public static World createWorld(File countryFile,
									Map<String, Resource> resources,
									Supplier<QualityComputation> qualityComputation,
									double gamma) throws IOException {
		WorldBuilder worldBuilder = new WorldBuilder(resources);
		try (BufferedReader reader = new BufferedReader(new FileReader(countryFile))) {
			String line = reader.readLine(); //  header line
			String[] headers = line.split(",");
			
			while((line = reader.readLine()) != null) {
				String[] values = line.split(",");

				worldBuilder.addCountry(values[0], qualityComputation);
				
				for (int i = 1; i < values.length; i++) {
					worldBuilder.addResource(values[0], headers[i], Integer.valueOf(values[i]));
				}
			}
		}

		return worldBuilder.build();
	}
}

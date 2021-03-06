package edu.vanderbilt.studzikm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads resource input file and creates appropriate resources objects
 */
public class ResourceFactory {

	/**
	 * Creates resources from a CSV file
	 * @param resourcesFile name of the file
	 * @return a map of resource names to resource objects
	 * @throws IOException
	 */
	public static Map<String, Resource> importResources(File resourcesFile) throws IOException {
		Map<String, Resource> resources = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(resourcesFile))) {
			String line = reader.readLine(); // Skip header

			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");

				String name = values[0];
				Double weight = Double.valueOf(values[1]);
				resources.put(name, new Resource(name, weight));
			}
		}

		return resources;	
	}
}

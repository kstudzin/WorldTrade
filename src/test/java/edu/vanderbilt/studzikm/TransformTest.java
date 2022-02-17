package edu.vanderbilt.studzikm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TransformTest {

	static Map<String, Resource> defaultResources = new HashMap<>();
	static Resource r1 = new Resource("R1", 1.0);
	static Resource r2 = new Resource("R2", 1.0);
	static Resource r3 = new Resource("R3", 1.0);
	static Resource r21 = new Resource("R21", 1.0);
	static Resource r22 = new Resource("R22", 1.0);
	static Resource r23 = new Resource("R23", 1.0);
	static Resource r21p = new Resource("R21'", 1.0);
	static Resource r22p = new Resource("R22'", 1.0);
	static Resource r23p = new Resource("R23'", 1.0);
	DefaultTransforms transforms = new DefaultTransforms(defaultResources);

	static {
		defaultResources.put("R1", r1);
		defaultResources.put("R2", r2);
		defaultResources.put("R3", r3);
		defaultResources.put("R21", r21);
		defaultResources.put("R22", r22);
		defaultResources.put("R23", r23);
		defaultResources.put("R21'", r21p);
		defaultResources.put("R22'", r22p);
		defaultResources.put("R23'", r23p);
	}

	@Test
	void testHousingTransform() {
		Country country = new Country("TestCountry", new DefaultQualityComputation());
		country.addResource(r1, 10);
		country.addResource(r2, 5);
		country.addResource(r3, 10);
		country.addResource(r21, 10);

		boolean result = transforms.HOUSING_TRANSFORM.transform(country);

		assertTrue(result);
		assertEquals(10, country.getResource(r1));
		assertEquals(4, country.getResource(r2));
		assertEquals(5, country.getResource(r3));
		assertEquals(7, country.getResource(r21));
		assertEquals(1, country.getResource(r23));
		assertEquals(1, country.getResource(r23p));
	}

	@Test
	void testElectronicsTransform() {
		Country country = new Country("TestCountry", new DefaultQualityComputation());
		country.addResource(r1, 10);
		country.addResource(r2, 6);
		country.addResource(r21, 4);

		boolean result = transforms.ELECTRONICS_TRANSFORM.transform(country);

		assertTrue(result);
		assertEquals(10, country.getResource(r1));
		assertEquals(3, country.getResource(r2));
		assertEquals(2, country.getResource(r21));
		assertEquals(2, country.getResource(r22));
		assertEquals(1, country.getResource(r22p));
	}

}

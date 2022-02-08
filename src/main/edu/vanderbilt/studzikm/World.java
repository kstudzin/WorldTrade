package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class World implements Iterable<Country> {
	private Map<String, Country> countries = new HashMap<>();

	public World() {
		// Ensure default constructor in addition to copy
	}

	public World(World copy) {
		this.countries.putAll(copy.countries);
	}

	public void addCountry(Country country) {
		countries.put(country.getName(), country);
	}

	public Country getCountry(String countryName) {
		return countries.get(countryName);
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countries == null) ? 0 : countries.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		World other = (World) obj;
		if (countries == null) {
			if (other.countries != null)
				return false;
		} else if (!countries.equals(other.countries))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "World [countries=" + countries + "]";
	}

	@Override
	public Iterator<Country> iterator() {
		return countries.values().iterator();
	}

}

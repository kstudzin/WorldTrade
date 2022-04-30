package edu.vanderbilt.studzikm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for creating instances of Transfers
 */
public class DefaultTransferFactory implements TransferFactory {

	private Collection<Transfer> transfers = new ArrayList<>();

	/**
	 * Creates transforms for each combination of resource and allowable transfer percents
	 * @param resources
	 * @param allowableTransferPercents
	 */
	public DefaultTransferFactory(Map<String, Resource> resources, Double[] allowableTransferPercents) {
		transfers = resources.entrySet()
		.stream()
		.filter(r -> !r.getKey().equals("R1")) // Don't allow trading people
		.flatMap(r -> createTransfers(r.getValue(), allowableTransferPercents).stream())
		.collect(Collectors.toSet());
	}
	
	private Collection<Transfer> createTransfers(Resource resource, Double[] allowableTransferPercents) {
		return Arrays.stream(allowableTransferPercents)
		.map(percent -> new Transfer(resource, percent))
		.collect(Collectors.toSet());
	}

	/**
	 * Gets the transfers
	 * @return a collection of transfers
	 */
	public Collection<Transfer> getTransfers() {
		return transfers;
	}

}

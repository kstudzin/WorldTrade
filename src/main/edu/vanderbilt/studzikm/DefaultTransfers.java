package edu.vanderbilt.studzikm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultTransfers implements TransferFactory {

	private Collection<Transfer> transfers = new ArrayList<>();

	public DefaultTransfers(Map<String, Resource> resources, Double[] allowableTransferPercents) {
		transfers = resources.entrySet()
		.stream()
		.flatMap(r -> createTransfers(r.getValue(), allowableTransferPercents).stream())
		.collect(Collectors.toSet());
	}
	
	private Collection<Transfer> createTransfers(Resource resource, Double[] allowableTransferPercents) {
		return Arrays.stream(allowableTransferPercents)
		.map(percent -> new Transfer(resource, percent))
		.collect(Collectors.toSet());
	}

	public Collection<Transfer> getTransfers() {
		return transfers;
	}

}

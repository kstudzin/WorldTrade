package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultStateGenerator implements StateGenerator {

	private DefaultTransforms transforms;
	private Collection<Transfer> transfers;

	public DefaultStateGenerator(Map<String, Resource> resources, TransferFactory transferFactory) {
		this.transforms = new DefaultTransforms(resources);
		this.transfers = transferFactory.getTransfers();
	}

	public <T extends Action> Collection<ActionResult<? extends Action>> generateStates(World initialState, Country self) {


		Collection<ActionResult<? extends Action>> result = new HashSet<>();

		result.addAll(generateTransformations(initialState, self));
		result.addAll(generateTransferAsSender(initialState, self));

		return result;
	}

	private Collection<ActionResult<Transfer>> generateTransferAsSender(World initialState, Country self) {
		return transfers.stream()
		.filter(t -> self.getResource(t.getResource()) > 0)
		.flatMap(transfer -> performTransfer(transfer, initialState, self).stream())
		.collect(Collectors.toSet());
	}

	private Collection<ActionResult<Transform>> generateTransformations(World initialState, Country self) {

		return transforms.ALL_TRANSFORMS.stream()
		.map(transform -> performTransformation(transform, new World(initialState), new Country(self)))
		.filter(ar -> ar != null)
		.collect(Collectors.toSet());

	}

	private Collection<ActionResult<Transfer>> performTransfer(Transfer transfer, World world, Country sender) {
		return world.stream()
		.filter(reciever -> reciever != sender)
		.map(orig -> performTransfer(transfer, new World(world), new Country(orig), new Country(sender)))
		.filter(ar -> ar != null)
		.collect(Collectors.toSet());
	}
	
	private ActionResult<Transfer> performTransfer(Transfer transfer, World world, Country reciever, Country sender) {
		boolean success = transfer.trade(sender, reciever);
		if (!success) {
			return null;
		}

		world.addCountry(sender);
		world.addCountry(reciever);
		return new TransferResult(world, transfer, sender, reciever);
	}

	private ActionResult<Transform> performTransformation(Transform transform, World world, Country country){
		boolean success = transform.transform(country);
		if (!success) {
			return null;
		}

		world.addCountry(country);
		return new ActionResult<>(world, transform, country);
	}

}

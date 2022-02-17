package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultStateGenerator implements StateGenerator {

	private DefaultTransforms transforms;
	private Collection<Transfer> transfers;
	private RewardComputation rewardComputation;

	public DefaultStateGenerator(Map<String, Resource> resources, 
			TransferFactory transferFactory, 
			RewardComputation rewardComputation) {
		this.transforms = new DefaultTransforms(resources);
		this.transfers = transferFactory.getTransfers();
		this.rewardComputation = rewardComputation;
	}

	public <T extends Action> Collection<ActionResult<? extends Action>> generateStates(World initialState, Country self, int depth) {
		Collection<ActionResult<? extends Action>> result = new HashSet<>();

		result.addAll(generateTransformations(initialState, self, depth));
		result.addAll(generateTransferAsSender(initialState, self, depth));

		return result;
	}

	private Collection<ActionResult<Transfer>> generateTransferAsSender(World initialState, Country self, int depth) {
		return transfers.stream()
		.filter(t -> self.getResource(t.getResource()) > 0)
		.flatMap(transfer -> performTransfer(transfer, initialState, self, depth).stream())
		.collect(Collectors.toSet());
	}

	private Collection<ActionResult<Transform>> generateTransformations(World initialState, Country self, int depth) {

		return transforms.ALL_TRANSFORMS.stream()
		.map(transform -> performTransformation(transform, new World(initialState), new Country(self), depth))
		.filter(ar -> ar != null)
		.collect(Collectors.toSet());

	}

	private Collection<ActionResult<Transfer>> performTransfer(Transfer transfer, World world, Country sender, int depth) {
		return world.stream()
		.filter(reciever -> reciever != sender)
		.map(orig -> performTransfer(transfer, new World(world), new Country(orig), new Country(sender), depth))
		.filter(ar -> ar != null)
		.collect(Collectors.toSet());
	}
	
	private ActionResult<Transfer> performTransfer(Transfer transfer, World world, Country reciever, Country sender, int depth) {
		boolean success = transfer.trade(sender, reciever);
		if (!success) {
			return null;
		}

		world.addCountry(sender);
		world.addCountry(reciever);
		return new TransferResult(world, transfer, sender, reciever, rewardComputation, depth);
	}

	private ActionResult<Transform> performTransformation(Transform transform, World world, Country country, int depth){
		boolean success = transform.transform(country);
		if (!success) {
			return null;
		}

		world.addCountry(country);
		return new TransformResult(world, transform, country, rewardComputation, depth);
	}

}

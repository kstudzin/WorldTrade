package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import edu.vanderbilt.studzikm.TransferResult.Role;

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
		result.addAll(generateTransferAsReciever(initialState, self, depth));

		return result;
	}

	private Collection<ActionResult<? extends Action>> generateTransferAsReciever(World initialState, Country self, int depth) {

		return transfers.stream()
		.flatMap(transfer -> performTransferAsReciever(transfer, initialState, self, depth).stream())
		.collect(Collectors.toSet());
	}

	private Collection<ActionResult<Transfer>> performTransferAsReciever(Transfer transfer, World world, Country self, int depth) {

		return world.stream()
		.filter(sender -> sender != self)
		.map(sender -> performTransfer(transfer, new World(world), new Country(self), new Country(sender), depth, Role.RECIEVER))
		.collect(Collectors.toSet());
	}

	private Collection<ActionResult<Transfer>> generateTransferAsSender(World initialState, Country self, int depth) {
		return transfers.stream()
		.filter(t -> self.getResource(t.getResource()) > 0)
		.flatMap(transfer -> performTransferAsSender(transfer, initialState, self, depth, Role.SENDER).stream())
		.collect(Collectors.toSet());
	}

	private Collection<ActionResult<Transform>> generateTransformations(World initialState, Country self, int depth) {

		return transforms.ALL_TRANSFORMS.stream()
		.map(transform -> performTransformation(transform, new World(initialState), new Country(self), depth))
		.filter(ar -> ar != null)
		.collect(Collectors.toSet());

	}

	private Collection<ActionResult<Transfer>> performTransferAsSender(Transfer transfer, World world, Country sender, int depth, Role selfRole) {
		return world.stream()
		.filter(reciever -> reciever != sender)
		.map(orig -> performTransfer(transfer, new World(world), new Country(orig), new Country(sender), depth, selfRole))
		.filter(ar -> ar != null)
		.collect(Collectors.toSet());
	}
	
	private ActionResult<Transfer> performTransfer(Transfer transfer, World world, Country reciever, Country sender, int depth, Role selfRole) {
		boolean success = transfer.trade(sender, reciever);
		if (!success) {
			return null;
		}

		world.addCountry(sender);
		world.addCountry(reciever);

		Country self = selfRole == Role.RECIEVER ? reciever : sender;
		Country other = selfRole == Role.RECIEVER ? sender : reciever;

		return new TransferResult(world, transfer, self, other, rewardComputation, depth, selfRole);
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

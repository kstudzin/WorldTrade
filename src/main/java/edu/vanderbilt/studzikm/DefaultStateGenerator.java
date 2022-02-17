package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.vanderbilt.studzikm.TransferResult.Role;

public class DefaultStateGenerator implements StateGenerator {

	private DefaultTransforms transforms;
	private Collection<Transfer> transfers;
	private RewardComputation rewardComputation;

	public DefaultStateGenerator(
			Map<String, Resource> resources, 
			TransferFactory transferFactory, 
			RewardComputation rewardComputation) {

		this.transforms = new DefaultTransforms(resources);
		this.transfers = transferFactory.getTransfers();
		this.rewardComputation = rewardComputation;
	}

	public <T extends Action> Collection<ActionResult<? extends Action>> generateStates(
			World initialState, 
			Country self, 
			int depth) {

		return Stream.of(generateTransformations(initialState, self, depth), 
				generateTransferAsSender(initialState, self, depth),
				generageTransferAsReciever(initialState, self, depth))
				.flatMap(Function.identity())
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	private Stream<? extends ActionResult<? extends Action>> generageTransferAsReciever(
			World initialState,
			Country self, 
			int depth) {

		return transfers.stream()
		.flatMap(transfer -> performTransferAsReciever(transfer, initialState, self, depth));
	}

	private Stream<? extends ActionResult<Transfer>> performTransferAsReciever(
			Transfer transfer, 
			World world, 
			Country self, 
			int depth) {

		return world.stream()
		.filter(sender -> sender != self && 
							sender.getResource(transfer.getResource()) > 0)
		.map(sender -> performTransfer(transfer, 
				new World(world), 
				new Country(self), 
				new Country(sender), 
				depth, 
				Role.RECIEVER));
	}

	private Stream<? extends ActionResult<Transfer>> generateTransferAsSender(
			World initialState, 
			Country self, 
			int depth) {

		return transfers.stream()
		.filter(t -> self.getResource(t.getResource()) > 0)
		.flatMap(transfer -> performTransferAsSender(transfer,
				initialState, 
				self, 
				depth));
	}

	private Stream<? extends ActionResult<Transfer>> performTransferAsSender(
			Transfer transfer, 
			World world, 
			Country sender, 
			int depth) {

		return world.stream()
		.filter(reciever -> reciever != sender)
		.map(orig -> performTransfer(transfer, 
				new World(world), 
				new Country(orig), 
				new Country(sender), 
				depth, 
				Role.SENDER));
	}

	private ActionResult<Transfer> performTransfer(
			Transfer transfer, 
			World world, 
			Country reciever, 
			Country sender, 
			int depth,
			Role selfRole) {

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

	private Stream<? extends ActionResult<Transform>> generateTransformations(
			World initialState, 
			Country self, 
			int depth) {

		return transforms.ALL_TRANSFORMS.stream()
		.map(transform -> performTransformation(transform, new World(initialState), new Country(self), depth));
	}

	private ActionResult<Transform> performTransformation(
			Transform transform,
			World world, 
			Country country, 
			int depth){

		boolean success = transform.transform(country);
		if (!success) {
			return null;
		}

		world.addCountry(country);
		return new TransformResult(world, transform, country, rewardComputation, depth);
	}

}

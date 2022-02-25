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

	public DefaultStateGenerator(Map<String, Resource> resources, 
			TransferFactory transferFactory, 
			RewardComputation rewardComputation) {
		this.transforms = new DefaultTransforms(resources);
		this.transfers = transferFactory.getTransfers();
		this.rewardComputation = rewardComputation;
	}

	public Collection<?> generateStates(World initialState, Country self, int depth) {

		return Stream.of(
				generateTransformations(initialState, self, depth),
				generateTransferAsSender(initialState, self, depth),
				generateTransferAsReciever(initialState, self, depth)
				)
				.flatMap(Function.identity())
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	private Stream<? extends ActionResult<?>> generateTransferAsReciever(
			World initialState, 
			Country self, 
			int depth) {

		return transfers.stream()
		.flatMap(transfer -> performTransferAsReciever(
				transfer, 
				initialState, 
				self, 
				depth));
	}

	private Stream<?extends ActionResult<?>> performTransferAsReciever(
			Transfer transfer, 
			World world, 
			Country self, 
			int depth) {

		return world.stream()
		.filter(sender -> sender != self)
		.map(sender -> performTransfer(transfer, 
				new World(world), 
				new Country(self), 
				new Country(sender), 
				depth, 
				Role.RECIEVER));
	}

	private Stream<? extends ActionResult<?>> generateTransferAsSender(
			World initialState, 
			Country self, 
			int depth) {

		return transfers.stream()
		.flatMap(transfer -> performTransferAsSender(
				transfer, 
				initialState, 
				self, 
				depth, 
				Role.SENDER));
	}

	private Stream<? extends ActionResult<?>> performTransferAsSender(
			Transfer transfer, 
			World world, 
			Country sender, 
			int depth, 
			Role selfRole) {

		return world.stream()
		.filter(reciever -> reciever != sender)
		.map(reciever -> performTransfer(
				transfer, 
				new World(world), 
				new Country(reciever), 
				new Country(sender), 
				depth, 
				selfRole));
	}

	private TransferResult performTransfer(
			Transfer transfer, 
			World world, 
			Country reciever, 
			Country sender, 
			int depth, 
			Role selfRole) {

		ResourceDelta delta = transfer.trade(sender, reciever);
		if (delta == null) {
			return null;
		}

		world.addCountry(sender);
		world.addCountry(reciever);

		Country self = selfRole == Role.RECIEVER ? reciever : sender;
		Country other = selfRole == Role.RECIEVER ? sender : reciever;

		return new TransferResult(world, transfer, self, other, rewardComputation, depth, selfRole, delta);
	}

	private Stream<? extends ActionResult<?>> generateTransformations(
			World initialState, 
			Country self, 
			int depth) {

		return transforms.stream()
		.map(transform -> performTransformation(
				transform, 
				new World(initialState), 
				new Country(self), 
				depth));
	}

	private TransformResult performTransformation(
			Transform transform, 
			World world, 
			Country country, 
			int depth) {

		ResourceDelta delta = transform.transform(country);
		if (delta == null) {
			return null;
		}

		world.addCountry(country);
		return new TransformResult(world, transform, country, rewardComputation, depth, delta);
	}

}

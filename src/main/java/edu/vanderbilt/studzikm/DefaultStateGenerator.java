package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class DefaultStateGenerator implements StateGenerator {

	private Collection<Transform> transforms;
	private Collection<Transfer> transfers;
	private RewardComputation rewardComputation;

	public DefaultStateGenerator(
			TransferFactory transferFactory, 
			TransformFactory transformFactory,
			RewardComputation rewardComputation) {
		this.transforms = transformFactory.getTransforms();
		this.transfers = transferFactory.getTransfers();
		this.rewardComputation = rewardComputation;
	}

	public Stream<?> generateStates(World initialState, Country self, int depth) {

		return Stream.of(
				generateTransformations(initialState, self, depth),
				generateTransferAsSender(initialState, self, depth),
				generateTransferAsReciever(initialState, self, depth)
				)
				.flatMap(Function.identity())
				.filter(Objects::nonNull);
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
				ActionResult.Role.RECIEVER));
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
				ActionResult.Role.SENDER));
	}

	private Stream<? extends ActionResult<?>> performTransferAsSender(
			Transfer transfer, 
			World world, 
			Country sender, 
			int depth, 
			ActionResult.Role selfRole) {

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
			ActionResult.Role selfRole) {

		ResourceDelta delta = transfer.trade(sender, reciever);
		if (delta == null) {
			return null;
		}

		world.addCountry(sender);
		world.addCountry(reciever);

		Country self = selfRole == ActionResult.Role.RECIEVER ? reciever : sender;
		Country other = selfRole == ActionResult.Role.RECIEVER ? sender : reciever;

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

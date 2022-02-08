package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultStateGenerator implements StateGenerator {

	private DefaultTransforms transforms;
	private Collection<Transfer> transfers;

	public DefaultStateGenerator(Map<String, Resource> resources, TransferFactory transferFactory) {
		this.transforms = new DefaultTransforms(resources);
		this.transfers = transferFactory.getTransfers();
	}

	public Map<World, Action> generateStates(World initialState, Country self) {


		Map<World, Action> result = new HashMap<>();

		result.putAll(generateTransformations(initialState, self));
		result.putAll(generateTransferAsSender(initialState, self));

		return result;
	}

	private Map<World, Action> generateTransferAsSender(World initialState, Country self) {
		return transfers.stream()
		.filter(t -> self.getResource(t.getResource()) > 0)
		.flatMap(transfer -> performTransfer(transfer, initialState, self).stream())
		.collect(Collectors.toMap(ActionResult::getWorld, ActionResult::getAction));
	}

	private Map<World, Action> generateTransformations(World initialState, Country self) {

		return transforms.ALL_TRANSFORMS.stream()
		.map(transform -> performTransformation(transform, new World(initialState), new Country(self)))
		.collect(Collectors.toMap(ActionResult::getWorld, ActionResult::getAction));

	}

	private Collection<ActionResult> performTransfer(Transfer transfer, World world, Country sender) {
		return world.stream()
		.map(orig -> performTransfer(transfer, new World(world), new Country(orig), new Country(sender)))
		.collect(Collectors.toSet());
	}
	
	private ActionResult performTransfer(Transfer transfer, World world, Country reciever, Country sender) {
		transfer.trade(sender, reciever);
		world.addCountry(sender);
		world.addCountry(reciever);
		return new ActionResult(world, transfer);
	}

	private ActionResult performTransformation(Transform transform, World world, Country country){
		transform.transform(country);
		world.addCountry(country);
		return new ActionResult (world, transform);
	}

	private class ActionResult {

		private World world;
		private Action transform;

		public ActionResult(World world, Action transform) {
			this.world = world;
			this.transform = transform;
		}

		public World getWorld() {
			return world;
		}

		public Action getAction() {
			return transform;
		}
	}
}

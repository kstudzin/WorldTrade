package edu.vanderbilt.studzikm;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a transfer action that a country could perform.
 *
 * A transfer is when one country transfers some of its resources to another.
 * A single transfer involves one resource type. Additionally, a transfer
 * can only transfer a specific percentage of a country's total resources.
 * It is likely that you will create several transfers from the same resource
 * type, each of which allows a different percentage of the total resources
 * to be transferred.
 */
public class Transfer implements Action {
	Logger log = LogManager.getLogger(Transfer.class);

	private Resource resource;
	private Double percent;

	/**
	 * Creates a transfer action
	 * @param resource the resource to be transferred
	 * @param percent the amount of total resources transferred
	 */
	public Transfer(Resource resource, Double percent) {
		this.resource = resource;
		this.percent = percent;
	}

	/**
	 * Performs the trade
	 * @param sender the country sending the resources
	 * @param receiver the country receiving the resources
	 * @return the delta of resource and the magnitude of the change
	 */
	public ResourceDelta trade(Country sender, Country receiver) {
		if (sender.getName() == receiver.getName()) {
			log.warn("Found countries with same name in transfer:\n"
					+ "\tSender:   " + sender + "\n"
					+ "\tReciever: " +receiver);
			return null;
		}

		Integer senderAmount = sender.getResource(resource);
		Integer tradeQuantity = (int) (senderAmount * percent);
		if (tradeQuantity <= 0) {
			return null;
		}

		sender.updateResource(resource, tradeQuantity * -1);
		receiver.updateResource(resource, tradeQuantity);

		return new ResourceDelta().addInput(resource, tradeQuantity);
	}

	/**
	 * Gets the resource
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Gets the name of the resource
	 * @return the name
	 */
	public String getName() {
		return resource.getName();
	}

	/**
	 * Gets the type of action
	 * @return TRANSFER type
	 */
	public Type getType() {
		return Type.TRANSFER;
	}

	@Override
	public String toString() {
		return "Transfer [resource=" + resource + ", percent=" + percent + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(percent, resource);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transfer other = (Transfer) obj;
		return Objects.equals(percent, other.percent) && Objects.equals(resource, other.resource);
	}

}

package edu.vanderbilt.studzikm;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transfer implements Action {
	Logger log = LogManager.getLogger(Transfer.class);

	private Resource resource;
	private Double percent;

	public Transfer(Resource resource, Double percent) {
		this.resource = resource;
		this.percent = percent;
	}

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

	public Resource getResource() {
		return resource;
	}

	public String getName() {
		return resource.getName();
	}

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

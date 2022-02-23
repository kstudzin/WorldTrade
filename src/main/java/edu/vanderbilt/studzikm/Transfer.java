package edu.vanderbilt.studzikm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transfer implements Action{
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

	@Override
	public String toString() {
		return "Transfer [resource=" + resource + ", percent=" + percent + "]";
	}
}

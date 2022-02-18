package edu.vanderbilt.studzikm;


public class Transfer implements Action{

	private Resource resource;
	private Double percent;

	public Transfer(Resource resource, Double percent) {
		this.resource = resource;
		this.percent = percent;
	}

	public boolean trade(Country sender, Country receiver) {
		Integer senderAmount = sender.getResource(resource);
		Integer tradeQuantity = (int) (senderAmount * percent);
		if (tradeQuantity <= 0) {
			return false;
		}

		sender.updateResource(resource, tradeQuantity * -1);
		receiver.updateResource(resource, tradeQuantity);

		return true;
	}

	public Resource getResource() {
		return resource;
	}

	@Override
	public String toString() {
		return "Transfer [resource=" + resource + ", percent=" + percent + "]";
	}
}

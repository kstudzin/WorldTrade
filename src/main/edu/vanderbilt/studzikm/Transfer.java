package edu.vanderbilt.studzikm;


public class Transfer {

	private Resource resource;
	private Double percent;

	public Transfer(Resource resource, Double percent) {
		this.resource = resource;
		this.percent = percent;
	}

	public boolean trade(Country sender, Country receiver) {
		Integer senderAmount = sender.getResource(resource);
		Integer tradeQuantity = (int) (senderAmount * percent);

		sender.updateResource(resource, tradeQuantity * -1);
		receiver.updateResource(resource, tradeQuantity);

		return true;
	}
}

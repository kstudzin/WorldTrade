package edu.vanderbilt.studzikm;

import java.util.Map;

/**
 * Represents a component of a schedule item. There is an
 * item for each action that a country performs
 */
public class ScheduleItem {

	/**
	 * The type of action this schedule item represents
	 */
	enum Type {
		TRANSFER, TRANSFORM
	}

	private Type type;
	private String firstName;
	private String secondName;
	private Map<String, Integer> inputs;
	private Map<String, Integer> outputs;
	private double expectedUtility;
	private double selfQuality;
	private double otherQuality;
	private double selfReward;
	private double otherReward;
	private int schedulePosition;

	private ScheduleItem() {
		
	}

	/**
	 * Create a schedule item from a search node
	 * @param node the node to create this schedule item from
	 * @return the schedule item
	 */
	public static ScheduleItem create(SearchNode node) {
		ScheduleItem item = new ScheduleItem();

		item.expectedUtility = node.computeExpectedUtility();
		item.type = node.getActionResult() instanceof TransferResult ? Type.TRANSFER : Type.TRANSFORM;
		if (item.type == Type.TRANSFER) {
			TransferResult result = (TransferResult) node.getActionResult();
			createFromTransferResult(item, result);
		} else {
			TransformResult result = (TransformResult) node.getActionResult();
			createFromTransformResult(item, result);
		}
		item.schedulePosition = node.getActionResult().getSchedulePosition();


		return item;
	}

	private static void createFromTransferResult(ScheduleItem item, TransferResult result) {
		TransferResult.Role selfRole = result.getRole();

		item.firstName = selfRole == TransferResult.Role.SENDER ? 
				result.getSelf().getName() : result.getOther().getName();
		item.secondName = selfRole == TransferResult.Role.SENDER ? 
				result.getOther().getName() : result.getSelf().getName();
		item.inputs = result.getResourceDelta().getInputs();
		item.selfReward = result.getReward();
		item.otherReward = result.getOtherReward();
		item.selfQuality = result.getQuality();
		item.otherQuality = result.getOther().computeQuality();
	}

	private static void createFromTransformResult(ScheduleItem item, TransformResult result) {
		item.firstName = result.getSelf().getName();
		item.inputs = result.getResourceDelta().getInputs();
		item.outputs = result.getResourceDelta().getOutputs();
		item.selfReward = result.getReward();
		item.selfQuality = result.getQuality();
	}

	/**
	 * Gets the quality of the country whose schedule this is
	 * @return a numeric quality
	 */
	public double getSelfQuality() {
		return selfQuality;
	}

	/**
	 * Gets the quality of the another country involved in this schedule
	 * @return a numeric quality
	 */
	public double getOtherQuality() {
		return otherQuality;
	}

	/**
	 * Gets the type of action this schedule item represents
	 * @return the action type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the name to print first for this item.
	 *
	 * This is the owner of the schedule unless the action is a resource
	 * being transferred to the owner
	 * @return the name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the name to print second for this item
	 *
	 * This is only relevant for transfer actions.
	 * @return the name
	 */
	public String getSecondName() {
		return secondName;
	}

	/**
	 * Gets the input consumed by the action
	 * @return a map of strings to amounts
	 */
	public Map<String, Integer> getInputs() {
		return inputs;
	}

	/**
	 * Gets the outputs produced by the action
	 * @return a map of strings to ammounts
	 */
	public Map<String, Integer> getOutputs() {
		return outputs;
	}

	/**
	 * Gets the expected utility of this schedule item
	 * @return a numeric utility
	 */
	public double getExpectedUtility() {
		return expectedUtility;
	}

	/**
	 * Gets the reward for the country owning the schedule
	 * @return the numeric reward
	 */
	public double getSelfReward() {
		return selfReward;
	}

	/**
	 * Gets the reward for a country involved in this schedule item
	 * @return the numeric reward
	 */
	public double getOtherReward() {
		return otherReward;
	}

	/**
	 * Gets the position of the schedule item within this schedule
	 * @return the schedule position
	 */
	public int getSchedulePosition() {
		return schedulePosition;
	}

	public StringBuilder toStringBuilder() {
		StringBuilder builder = new StringBuilder()
				.append('\t')
				.append("(")
				.append(type)
				.append(" ")
				.append(firstName)
				.append(" ");

		if (type == Type.TRANSFER) {
			builder.append(secondName)
					.append(" (");

			inputs.entrySet()
			.forEach(e -> builder.append('(')
					.append(e.getKey())
					.append(' ')
					.append(e.getValue())
					.append(')')
					.append('\n'));
			builder.deleteCharAt(builder.length() - 1)
					.append(')')
					.append('\n');
		} else {
			builder.append('\n')
					.append('\t')
					.append('\t')
					.append("(INPUTS ");

			inputs.entrySet()
			.forEach(e -> builder
					.append('(')
					.append(e.getKey())
					.append(' ')
					.append(e.getValue())
					.append(')')
					.append('\n')
					.append('\t')
					.append('\t')
					.append('\t'));
			builder.deleteCharAt(builder.length() - 1)
					.deleteCharAt(builder.length() - 1)
					.deleteCharAt(builder.length() - 1)
					.deleteCharAt(builder.length() - 1)
					.append(')')
					.append('\n')
					.append('\t')
					.append('\t')
					.append("(OUTPUTS ");

			outputs.entrySet()
			.forEach(e -> builder
					.append('(')
					.append(e.getKey())
					.append(' ')
					.append(e.getValue())
					.append(')')
					.append('\n')
					.append('\t')
					.append('\t')
					.append('\t'));

			builder.deleteCharAt(builder.length() - 1)
					.deleteCharAt(builder.length() - 1)
					.deleteCharAt(builder.length() - 1)
					.deleteCharAt(builder.length() - 1)
					.append(')')
					.append('\n');
		}

		builder.deleteCharAt(builder.length() - 1);
		return builder.append(") EU: ")
				.append(expectedUtility);
	}


	public String toExpectedUtilityTypeString() {
		return Double.toString(expectedUtility) + "," + type;
	}

	public String toExpectedUtilityString() {
		return Double.toString(expectedUtility);
	}

	@Override
	public String toString() {
		return "ScheduleItem{" +
				"type=" + type +
				", firstName='" + firstName + '\'' +
				", secondName='" + secondName + '\'' +
				", inputs=" + inputs +
				", outputs=" + outputs +
				", expectedUtility=" + expectedUtility +
				", selfQuality=" + selfQuality +
				", otherQuality=" + otherQuality +
				", selfReward=" + selfReward +
				", otherReward=" + otherReward +
				'}';
	}

}

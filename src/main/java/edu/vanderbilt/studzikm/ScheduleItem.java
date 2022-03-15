package edu.vanderbilt.studzikm;

import java.util.Map;

class ScheduleItem {

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
	private int schedulePostion;

	private ScheduleItem() {
		
	}

	public static ScheduleItem create(SearchNode node, 
			ExpectedUtilityComputation expectedUtilityComputation) {
		ScheduleItem item = new ScheduleItem();

		item.type = node.getAction() instanceof TransferResult ? Type.TRANSFER : Type.TRANSFORM;
		if (item.type == Type.TRANSFER) {
			TransferResult result = (TransferResult) node.getAction();
			createFromTransferResult(item, result);
			item.expectedUtility = expectedUtilityComputation.compute(result);
		} else {
			TransformResult result = (TransformResult) node.getAction();
			createFromTransformResult(item, result);
			item.expectedUtility = expectedUtilityComputation.compute(result);
		}
		item.schedulePostion = node.getAction().getSchedulePosition();

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

	public double getSelfQuality() {
		return selfQuality;
	}

	public double getOtherQuality() {
		return otherQuality;
	}

	public Type getType() {
		return type;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public Map<String, Integer> getInputs() {
		return inputs;
	}

	public Map<String, Integer> getOutputs() {
		return outputs;
	}

	public double getExpectedUtility() {
		return expectedUtility;
	}

	public double getSelfReward() {
		return selfReward;
	}

	public double getOtherReward() {
		return otherReward;
	}

	public int getSchedulePostion() {return schedulePostion; }

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

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

	private ScheduleItem() {
		
	}

	public static ScheduleItem create(SearchNode node) {
		ScheduleItem item = new ScheduleItem();

		item.type = node.getAction() instanceof TransferResult ? Type.TRANSFER : Type.TRANSFORM;
		if (item.type == Type.TRANSFER) {
			createFromTransferResult(item, 
					(TransferResult) node.getAction());
		} else {
			createFromTransformResult(item, (TransformResult) node.getAction());
		}

		return item;
	}

	private static void createFromTransferResult(ScheduleItem item, TransferResult result) {
		TransferResult.Role selfRole = result.getRole();

		item.firstName = selfRole == TransferResult.Role.SENDER ? 
				result.getSelf().getName() : result.getOther().getName();
		item.secondName = selfRole == TransferResult.Role.SENDER ? 
				result.getOther().getName() : result.getSelf().getName();
		item.inputs = result.getResourceDelta().getInputs();
	}

	private static void createFromTransformResult(ScheduleItem item, TransformResult result) {
		item.firstName = result.getSelf().getName();
		item.inputs = result.getResourceDelta().getInputs();
		item.outputs = result.getResourceDelta().getOutputs();
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
			.forEach(e -> builder
					.append('(')
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
					.append('\t'));
			builder.deleteCharAt(builder.length() - 1)
			.deleteCharAt(builder.length() - 1)
			.deleteCharAt(builder.length() - 1)
			.append(')')
			.append('\n')
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
					.append('\t'));

			builder.deleteCharAt(builder.length() - 1)
			.deleteCharAt(builder.length() - 1)
			.deleteCharAt(builder.length() - 1)
			.append(')')
			.append('\n');
		}

		builder.deleteCharAt(builder.length() - 1);
		return builder.append(')');
	}

}
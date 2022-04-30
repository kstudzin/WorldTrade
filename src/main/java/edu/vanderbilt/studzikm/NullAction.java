package edu.vanderbilt.studzikm;

/**
 * An action that does not have any functionality. Used as the
 * action for the root search node of the frontier. Instance
 * of the null object patter.
 *
 * None of the methods actually return Java null.
 */
public class NullAction implements Action {

	@Override
	public String toString() {
		return "NullAction";
	}

	/**
	 * Null action string
	 * @return a string representing a null action
	 */
	@Override
	public String getName() {
		return "Null Action";
	}

	/**
	 * The type of the action
	 * @return an enum representing a null action
	 */
	@Override
	public Type getType() {
		return Type.NULL;
	}
}

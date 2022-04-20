package edu.vanderbilt.studzikm;

public class NullAction implements Action {

	@Override
	public String toString() {
		return "NullAction";
	}

	@Override
	public String getName() {
		return "Null Action";
	}

	@Override
	public Type getType() {
		return null;
	}
}

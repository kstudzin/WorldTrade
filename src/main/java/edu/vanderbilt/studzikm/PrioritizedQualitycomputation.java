package edu.vanderbilt.studzikm;

// TODO Remove unused implementation
public class PrioritizedQualitycomputation implements QualityComputation {

	@Override
	public double compute(Country country) {
		Integer people = country.getResource("R1");
		Integer housing = country.getResource("R23");

		return 0;
	}

}

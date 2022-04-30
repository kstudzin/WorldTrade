package edu.vanderbilt.studzikm;

/**
 * Interface of a strategy for computing the quality of a country
 */
public interface QualityComputation {

	/**
	 * Computes the quality of the given country
	 * @param country the country to compute the quality of
	 * @return a numeric quality score
	 */
	double compute(Country country);

}

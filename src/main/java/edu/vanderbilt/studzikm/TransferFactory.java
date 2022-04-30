package edu.vanderbilt.studzikm;

import java.util.Collection;

/**
 * Factory interface for supplying transfers
 */
public interface TransferFactory {

	/**
	 * Gets the transfers
	 * @return a collection of transfers
	 */
	Collection<Transfer> getTransfers();
}

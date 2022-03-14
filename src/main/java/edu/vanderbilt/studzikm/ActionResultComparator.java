package edu.vanderbilt.studzikm;

import java.util.Comparator;

public class ActionResultComparator implements Comparator<ActionResult<?>> {
    @Override
    public int compare(ActionResult<?> x, ActionResult<?> y) {

        int compareResult = x.getReward().compareTo(y.getReward());

        if (compareResult == 0 &&
                x instanceof TransferResult &&
                y instanceof TransferResult) {

            // If self rewards are equal, compare rewards for other
            TransferResult xAsTransferResult = (TransferResult) x;
            TransferResult yAsTransferResult = (TransferResult) y;
            compareResult = xAsTransferResult.getOtherReward()
                    .compareTo(yAsTransferResult.getOtherReward());


        } else if (compareResult == 0 &&
                x instanceof TransferResult) {
            // prefer transforms to transfers
            compareResult = -1;

        } else if (compareResult == 0 &&
                y instanceof TransferResult) {
            // prefer transforms to transfers
            compareResult = 1;
        }

        // This comparison isn't particularly meaningful. It simply creates a
        // deterministic ordering which is useful for tests
        if (compareResult == 0) {
            compareResult = x.toString().compareTo(y.toString());
        }

        return compareResult;
    }
}

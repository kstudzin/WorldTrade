package edu.vanderbilt.studzikm;

public class NullActionResult extends ActionResult<NullAction> {
    public NullActionResult(World current,
                            NullAction nullAction,
                            Country self,
                            DefaultRewardComputation defaultRewardComputation,
                            int i,
                            ResourceDelta delta) {
        super(current,
                nullAction,
                self,
                defaultRewardComputation,
                i,
                delta);
    }

    @Override
    public Role getRole() {
        return Role.NULL;
    }
}

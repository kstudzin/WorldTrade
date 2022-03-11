package edu.vanderbilt.studzikm;

public class NullReached implements Reached{

    @Override
    public boolean contains(SearchNode node) {
        return false;
    }
}

package core.consensus;

import core.blockchain.Block;

import java.util.ArrayList;

public class Rating {

    private double value;
    private int mandatory;
    private int specialValidators;
    private int other;

    public Rating(String event, int mandatory, int specialValidators, int other) {
        this.mandatory = mandatory;
        this.specialValidators = specialValidators;
        this.other = other;
        value = 0;
    }

    public double calRating() {

        return value;
    }

}

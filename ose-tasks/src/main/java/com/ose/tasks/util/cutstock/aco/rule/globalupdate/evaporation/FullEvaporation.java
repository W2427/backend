package com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation;

import com.ose.tasks.util.cutstock.aco.ACO;

public class FullEvaporation extends AbstractEvaporation {

    public FullEvaporation(ACO aco, double rate) {
        super(aco, rate);
    }

    @Override
    public double getTheNewValue(int i, int j) {
        return (1.0 - rate) * aco.getGraph().getTau(i, j);
    }

    @Override
    public String toString() {
        return FullEvaporation.class.getSimpleName() + " with rate=" + rate;
    }
}

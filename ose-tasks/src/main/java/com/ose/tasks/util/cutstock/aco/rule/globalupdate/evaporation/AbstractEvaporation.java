package com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.AbstractGlobalUpdateRule;

public abstract class AbstractEvaporation extends AbstractGlobalUpdateRule {

    public AbstractEvaporation(ACO aco, double rate) {
        super(aco, rate);
    }

    public abstract double getTheNewValue(int i, int j);

    @Override
    public abstract String toString();
}

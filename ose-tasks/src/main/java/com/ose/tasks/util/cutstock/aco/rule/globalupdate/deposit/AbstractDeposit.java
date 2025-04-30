package com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.AbstractGlobalUpdateRule;

public abstract class AbstractDeposit extends AbstractGlobalUpdateRule {

    public AbstractDeposit(ACO aco, double rate) {
        super(aco, rate);
    }

    public abstract double getTheNewValue(int i, int j);

    @Override
    public abstract String toString();
}

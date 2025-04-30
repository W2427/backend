package com.ose.tasks.util.cutstock.aco.rule.globalupdate;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.rule.AbstractRule;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractGlobalUpdateRule extends AbstractRule {

    protected double rate;

    public AbstractGlobalUpdateRule(ACO aco, double rate) {
        super(aco);

        checkNotNull(aco, "The aco cannot be null");
        checkArgument(rate >= 0.0, "The rate should be greater or equal than 0");
        checkArgument(rate <= 1.0, "The rate should be less or equal than 1");

        this.rate = rate;
    }
}

package com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.subset.many.AllAnts;

public class FullDeposit extends PartialDeposit {

    public FullDeposit(ACO aco) {
        super(aco, 1.0, new AllAnts(aco));
    }

    @Override
    public String toString() {
        return FullDeposit.class.getSimpleName();
    }
}

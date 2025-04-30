package com.ose.tasks.util.cutstock.aco.rule.localupdate;

import com.ose.tasks.util.cutstock.aco.ACO;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractLocalUpdateRule {

    protected ACO aco;

    public AbstractLocalUpdateRule(ACO aco) {

        checkNotNull(aco, "The aco cannot be null");

        this.aco = aco;
    }

    public abstract void update(int currentNode, int nextNode);
}

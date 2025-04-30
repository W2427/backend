package com.ose.tasks.util.cutstock.aco.daemonactions;

import com.ose.tasks.util.cutstock.aco.ACO;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractDaemonActions {

    protected ACO aco;

    public AbstractDaemonActions(ACO aco) {

        checkNotNull(aco, "The aco cannot be null");

        this.aco = aco;
    }

    public abstract void doAction();

    @Override
    public abstract String toString();
}

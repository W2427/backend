package com.ose.tasks.util.cutstock.aco.ant.selection;

import com.ose.tasks.util.cutstock.util.random.JMetalRandom;

public abstract class AbstractAntSelection {

    protected JMetalRandom rand = JMetalRandom.getInstance();

    public abstract int select(double[] probability, double sumProbability);

    @Override
    public abstract String toString();
}

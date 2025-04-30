package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.exploration.PseudoRandomProportionalRule;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AnAntAtEachVertex;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;
import com.ose.tasks.util.cutstock.aco.daemonactions.RestartCheck;
import com.ose.tasks.util.cutstock.aco.daemonactions.UpdatePheromoneMatrix;
import com.ose.tasks.util.cutstock.aco.daemonactions.UpdateTMinAndTMaxValues;
import com.ose.tasks.util.cutstock.aco.graph.initialization.MMASInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.PartialDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.FullEvaporation;
import com.ose.tasks.util.cutstock.aco.subset.single.GlobalBest;
import com.ose.tasks.util.cutstock.problem.Problem;

public class MaxMinAntSystem extends AntSystem {

    protected int stagnation;

    public MaxMinAntSystem(Problem problem) {
        super(problem);
    }

    public int getStagnation() {
        return stagnation;
    }

    public void setStagnation(int stagnation) {
        this.stagnation = stagnation;
    }

    @Override
    public void build() {

        setGraphInitialization(new MMASInitialization(this, rho));
        setAntInitialization(new AnAntAtEachVertex(this));


        setAntExploration(new PseudoRandomProportionalRule(this, new RouletteWheel()));


        getEvaporations().add(new FullEvaporation(this, rho));
        getDeposits().add(new PartialDeposit(this, 1.0, new GlobalBest(this)));


        getDaemonActions().add(new UpdateTMinAndTMaxValues(this, rho));
        getDaemonActions().add(new UpdatePheromoneMatrix(this));
        getDaemonActions().add(new RestartCheck(this, stagnation));
    }

    @Override
    public String toString() {
        return MaxMinAntSystem.class.getSimpleName();
    }
}

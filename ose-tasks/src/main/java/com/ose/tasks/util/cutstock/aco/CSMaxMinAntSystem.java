package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.exploration.CSPseudoRandomProportionalRule;
import com.ose.tasks.util.cutstock.aco.ant.initialization.CSAnAntAtEachVertex;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;
import com.ose.tasks.util.cutstock.aco.daemonactions.RestartCheck;
import com.ose.tasks.util.cutstock.aco.daemonactions.UpdatePheromoneMatrix;
import com.ose.tasks.util.cutstock.aco.daemonactions.UpdateTMinAndTMaxValues;
import com.ose.tasks.util.cutstock.aco.graph.initialization.MMASInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.PartialDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.FullEvaporation;
import com.ose.tasks.util.cutstock.aco.subset.single.GlobalBest;
import com.ose.tasks.util.cutstock.problem.Problem;

public class CSMaxMinAntSystem extends AntSystem {

    protected int stagnation;



    public CSMaxMinAntSystem(Problem problem) {
        super(problem);
    }

    public int getStagnation() {
        return stagnation;
    }

    public void setStagnation(int stagnation) {
        this.stagnation = stagnation;
    }


    public void build() {

        setGraphInitialization(new MMASInitialization(this, rho));
        setAntInitialization(new CSAnAntAtEachVertex(this));


        setAntExploration(new CSPseudoRandomProportionalRule(this, new RouletteWheel()));


        getEvaporations().add(new FullEvaporation(this, rho));
        getDeposits().add(new PartialDeposit(this, 1.0, new GlobalBest(this)));


        getDaemonActions().add(new UpdateTMinAndTMaxValues(this, rho));
        getDaemonActions().add(new UpdatePheromoneMatrix(this));
        getDaemonActions().add(new RestartCheck(this, stagnation));
    }

    @Override
    public String toString() {
        return CSMaxMinAntSystem.class.getSimpleName();
    }
}

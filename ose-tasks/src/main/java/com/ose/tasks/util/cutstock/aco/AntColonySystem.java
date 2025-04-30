package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.exploration.QSelection;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AnAntAtEachVertex;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;
import com.ose.tasks.util.cutstock.aco.graph.initialization.ACSInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.PartialDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.FullEvaporation;
import com.ose.tasks.util.cutstock.aco.rule.localupdate.ACSLocalUpdatingRule;
import com.ose.tasks.util.cutstock.aco.subset.single.GlobalBest;
import com.ose.tasks.util.cutstock.problem.Problem;

public class AntColonySystem extends AntSystem {

    protected double q0;

    protected double omega;

    public AntColonySystem(Problem problem) {
        super(problem);
    }

    public double getQ0() {
        return q0;
    }

    public void setQ0(double q0) {
        this.q0 = q0;
    }

    public double getOmega() {
        return omega;
    }

    public void setOmega(double omega) {
        this.omega = omega;
    }

    @Override
    public void build() {

        setGraphInitialization(new ACSInitialization(this));
        setAntInitialization(new AnAntAtEachVertex(this));


        setAntExploration(new QSelection(this, new RouletteWheel(), q0));


        setAntLocalUpdate(new ACSLocalUpdatingRule(this, omega));


        getEvaporations().add(new FullEvaporation(this, getRho()));
        getDeposits().add(new PartialDeposit(this, getRho(), new GlobalBest(this)));
    }

    @Override
    public String toString() {
        return AntColonySystem.class.getSimpleName();
    }
}

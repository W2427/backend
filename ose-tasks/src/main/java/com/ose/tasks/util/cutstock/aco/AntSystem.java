package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.exploration.PseudoRandomProportionalRule;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AnAntAtEachVertex;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;
import com.ose.tasks.util.cutstock.aco.graph.initialization.ASInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.FullDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.FullEvaporation;
import com.ose.tasks.util.cutstock.problem.Problem;

public class AntSystem extends ACO {

    public AntSystem(Problem problem) {
        super(problem);
    }

    @Override
    public void build() {
        setGraphInitialization(new ASInitialization(this));
        setAntInitialization(new AnAntAtEachVertex(this));

        setAntExploration(new PseudoRandomProportionalRule(this, new RouletteWheel()));


        getEvaporations().add(new FullEvaporation(this, rho));
        getDeposits().add(new FullDeposit(this));
    }

    @Override
    public String toString() {
        return AntSystem.class.getSimpleName();
    }
}

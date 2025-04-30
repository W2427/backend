package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.exploration.PseudoRandomProportionalRule;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AnAntAtEachVertex;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;
import com.ose.tasks.util.cutstock.aco.graph.initialization.EASInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.ElitistDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.FullEvaporation;
import com.ose.tasks.util.cutstock.problem.Problem;

public class ElitistAntSystem extends AntSystem {

    protected int weight;

    public ElitistAntSystem(Problem problem) {
        super(problem);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void build() {
        setGraphInitialization(new EASInitialization(this, rho));
        setAntInitialization(new AnAntAtEachVertex(this));

        setAntExploration(new PseudoRandomProportionalRule(this, new RouletteWheel()));


        getEvaporations().add(new FullEvaporation(this, rho));
        getDeposits().add(new ElitistDeposit(this, weight));
    }

    @Override
    public String toString() {
        return ElitistAntSystem.class.getSimpleName();
    }
}

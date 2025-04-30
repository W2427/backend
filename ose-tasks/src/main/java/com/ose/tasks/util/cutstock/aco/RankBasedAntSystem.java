package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.exploration.PseudoRandomProportionalRule;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AnAntAtEachVertex;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;
import com.ose.tasks.util.cutstock.aco.graph.initialization.ASRankInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.RankDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.FullEvaporation;
import com.ose.tasks.util.cutstock.aco.subset.many.RankAnt;
import com.ose.tasks.util.cutstock.problem.Problem;

public class RankBasedAntSystem extends ElitistAntSystem {

    public RankBasedAntSystem(Problem problem) {
        super(problem);
    }

    @Override
    public void build() {
        setGraphInitialization(new ASRankInitialization(this, rho, weight));
        setAntInitialization(new AnAntAtEachVertex(this));

        setAntExploration(new PseudoRandomProportionalRule(this, new RouletteWheel()));


        getEvaporations().add(new FullEvaporation(this, rho));
        getDeposits().add(new RankDeposit(this, weight, new RankAnt(this, getNumberOfAnts())));
    }

    @Override
    public String toString() {
        return RankBasedAntSystem.class.getSimpleName();
    }
}

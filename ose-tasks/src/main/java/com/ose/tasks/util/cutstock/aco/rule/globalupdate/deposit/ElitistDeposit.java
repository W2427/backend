package com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;
import com.ose.tasks.util.cutstock.aco.subset.many.AllAnts;

import static com.google.common.base.Preconditions.checkArgument;

public class ElitistDeposit extends PartialDeposit {

    protected int weight;

    public ElitistDeposit(ACO aco, int weight) {
        super(aco, 1.0, new AllAnts(aco));

        checkArgument(weight >= 0, "The weight should be greater or equal than 0");

        this.weight = weight;
    }

    /**
     * Default Constructor for weight=the number of ants
     *
     * @param aco The ant colony optimization used
     */
    public ElitistDeposit(ACO aco) {
        this(aco, aco.getNumberOfAnts());
    }

    @Override
    public double getTheNewValue(int i, int j) {
        return aco.getGraph().getTau(i, j) + super.getDeltaTau(i, j) + weight * getDeltaTauGlobalBest(i, j);
    }

    public double getDeltaTauGlobalBest(int i, int j) {

        Ant globalBest = aco.getGlobalBest();

        if (globalBest.path[i][j] == 1) {
            return aco.getProblem().getDeltaTau(globalBest.getTourLength(), i, j);
        }

        return 0.0;
    }

    @Override
    public String toString() {
        return ElitistDeposit.class.getSimpleName() + " with weight=" + weight;
    }
}

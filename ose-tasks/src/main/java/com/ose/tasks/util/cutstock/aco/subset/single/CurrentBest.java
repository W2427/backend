package com.ose.tasks.util.cutstock.aco.subset.single;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the subset with the current best solution.
 * The current-best solution means the best solution found during an iteration.
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class CurrentBest extends AbstractSingleAnt {

    /**
     * Constructor
     *
     * @param aco The ant colony optimization used
     */
    public CurrentBest(ACO aco) {
        super(aco);
    }

    @Override
    public List<Ant> getSubSet() {
        List<Ant> list = new ArrayList<>();

        list.add(aco.getCurrentBest().clone());

        return list;
    }

    @Override
    public String toString() {
        return CurrentBest.class.getSimpleName();
    }
}

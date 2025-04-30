package com.ose.tasks.util.cutstock.aco.subset.many;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the subset with all ants used
 * in the algorithm
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class AllAnts extends AbstractManyAnts {

    /**
     * Constructor
     *
     * @param aco The ant colony optimization used
     */
    public AllAnts(ACO aco) {
        super(aco);
    }

    @Override
    public List<Ant> getSubSet() {

        Ant[] ants = aco.getAnts();

        List<Ant> list = new ArrayList<>();

        for (int i = 0; i < ants.length; i++) {
            list.add(ants[i].clone());
        }

        return list;
    }

    @Override
    public String toString() {
        return AllAnts.class.getSimpleName();
    }
}

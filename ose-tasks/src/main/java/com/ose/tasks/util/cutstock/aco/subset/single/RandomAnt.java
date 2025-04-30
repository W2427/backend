package com.ose.tasks.util.cutstock.aco.subset.single;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the subset with a randomly-selected solution.
 * The solution is selected from the set of all ants
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class RandomAnt extends AbstractSingleAnt {

    /**
     * Constructor
     *
     * @param aco The ant colony optimization used
     */
    public RandomAnt(ACO aco) {
        super(aco);
    }

    @Override
    public List<Ant> getSubSet() {

        Ant[] ants = aco.getAnts();

        int index = rand.nextInt(0, ants.length - 1);

        List<Ant> list = new ArrayList<>();

        list.add(ants[index].clone());

        return list;
    }

    @Override
    public String toString() {
        return RandomAnt.class.getSimpleName();
    }
}

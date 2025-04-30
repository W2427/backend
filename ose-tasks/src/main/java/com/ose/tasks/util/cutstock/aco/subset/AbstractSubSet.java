package com.ose.tasks.util.cutstock.aco.subset;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;
import com.ose.tasks.util.cutstock.util.random.JMetalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents the subset of ants used to evaporate or deposit
 * values in the pheromone matrix. All kind of subset should implement this class.
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public abstract class AbstractSubSet {

    /**
     * The ant colony optimization used
     */
    protected ACO aco;

    /**
     * The rand class
     */
    protected JMetalRandom rand = JMetalRandom.getInstance();

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(AbstractSubSet.class);


    /**
     * Constructor
     *
     * @param aco The ant colony optimization used
     */
    public AbstractSubSet(ACO aco) {

        checkNotNull(aco, "The aco cannot be null");

        this.aco = aco;
    }

    /**
     * Get the list of ants that will be used to evaporate or
     * deposit the pheromone in the pheromone graph
     *
     * @return a list of ants
     */
    public abstract List<Ant> getSubSet();

    @Override
    public abstract String toString();
}

package com.ose.tasks.util.cutstock.aco.ant.exploration;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;
import com.ose.tasks.util.cutstock.aco.ant.selection.AbstractAntSelection;
import com.ose.tasks.util.cutstock.util.random.JMetalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents how an ant builds its tour. Currently, this framework support two possible
 * ant exploration:
 * <br><br>
 * 1) AS algorithm; <br>
 * 2) ACS algorithm
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public abstract class AbstractAntExploration {

    /**
     * The ant colony optimization used
     */
    protected ACO aco;

    /**
     * The rand class
     */
    protected JMetalRandom rand = JMetalRandom.getInstance();

    /**
     * The ant selection used
     */
    protected AbstractAntSelection antSelection;

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(AbstractAntExploration.class);

    /**
     * Constructor
     *
     * @param aco          The ant colony optimization used
     * @param antSelection The ant selection used
     */
    public AbstractAntExploration(ACO aco, AbstractAntSelection antSelection) {

        checkNotNull(aco, "The aco cannot be null");
        checkNotNull(antSelection, "The antSelection cannot be null");

        this.aco = aco;
        this.antSelection = antSelection;
    }

    /**
     * Get the ant selection
     *
     * @return the ant selection
     */
    public AbstractAntSelection getAntSelection() {
        return antSelection;
    }

    /**
     * Given that an ant is in given node,
     * this method returns the next node
     *
     * @param ant         the current ant
     * @param currentNode the current node
     * @return the next node
     */
    public abstract int getNextNode(Ant ant, int currentNode);

    @Override
    public abstract String toString();
}

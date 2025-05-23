package com.ose.tasks.util.cutstock.aco.daemonactions;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Each time a new best-so-far tour is found, the value of tmax is updated.
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class UpdateTMinAndTMaxValues extends AbstractDaemonActions {

    protected double rate;

    protected Ant bestSoFar;

    /**
     * The class logger
     */
    static final Logger logger = LoggerFactory.getLogger(UpdateTMinAndTMaxValues.class);

    /**
     * Constructor
     *
     * @param aco  The ant colony optimization
     * @param rate The evaporation rate
     */
    public UpdateTMinAndTMaxValues(ACO aco, double rate) {
        super(aco);

        checkArgument(rate > 0.0 && rate <= 1.0, "The rate should be between (0,1]");

        this.rate = rate;
    }

    @Override
    public void doAction() {



        if (bestSoFar == null) {
            bestSoFar = aco.getGlobalBest().clone();
            updateMinAndMaxValues();
        } else if (bestSoFar.getTourLength() != aco.getGlobalBest().getTourLength()) {
            bestSoFar = aco.getGlobalBest().clone();
            updateMinAndMaxValues();
        } else {

        }
    }

    protected void updateMinAndMaxValues() {



        aco.getGraph().setTMax(1.0 / (rate * bestSoFar.getTourLength()));
        aco.getGraph().setTMin(aco.getGraph().getTMax() / 10.0);


    }

    @Override
    public String toString() {
        return UpdateTMinAndTMaxValues.class.getSimpleName();
    }
}

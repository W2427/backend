package com.ose.tasks.util.cutstock.aco.daemonactions;

import com.ose.tasks.util.cutstock.aco.ACO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Update the pheromone value regarding the tMin and tMax values
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class UpdatePheromoneMatrix extends AbstractDaemonActions {

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(UpdatePheromoneMatrix.class);


    public UpdatePheromoneMatrix(ACO aco) {
        super(aco);
    }

    @Override
    public void doAction() {



        for (int i = 0; i < aco.getProblem().getNumberOfNodes(); i++) {

            for (int j = i; j < aco.getProblem().getNumberOfNodes(); j++) {

                if (i != j) {
                    aco.getGraph().setTau(i, j, Math.min(aco.getGraph().getTau(i, j), aco.getGraph().getTMax()));
                    aco.getGraph().setTau(i, j, Math.max(aco.getGraph().getTau(i, j), aco.getGraph().getTMin()));
                    aco.getGraph().setTau(j, i, aco.getGraph().getTau(i, j));
                }
            }
        }
    }

    @Override
    public String toString() {
        return UpdatePheromoneMatrix.class.getSimpleName();
    }
}

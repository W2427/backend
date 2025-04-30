package com.ose.tasks.util.cutstock.util;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.problem.Problem;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * The algorithm's executor class
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class ExecutionStats {

    public double executionTime;

    public ACO aco;

    public Problem problem;

    public int[] bestSolution;

    /**
     * The class logger
     */
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ExecutionStats.class);


    public static ExecutionStats execute(ACO aco, Problem problem) {
        ExecutionStats ets = new ExecutionStats();

        ets.aco = aco;
        ets.problem = problem;

        long initTime = System.currentTimeMillis();
        ets.bestSolution = aco.solve();
        ets.executionTime = System.currentTimeMillis() - initTime;

        return ets;
    }

    public void printStats() {
        logger.info("==================================================");
        logger.info("Execution Time: " + executionTime);
        logger.info("Best Value: " + problem.evaluate(bestSolution));
        logger.info("Best Solution: " + Arrays.toString(bestSolution));
        logger.info("==================================================");
    }

    public void printDotFormat() {
        System.out.println(Convert.toDot(aco.getGraph().getTau()));
    }
}

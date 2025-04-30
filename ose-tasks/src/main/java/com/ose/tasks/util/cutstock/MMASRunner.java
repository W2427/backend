package com.ose.tasks.util.cutstock;

import com.ose.tasks.util.cutstock.aco.MaxMinAntSystem;
import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.problem.tsp.TravellingSalesmanProblem;
import com.ose.tasks.util.cutstock.util.ExecutionStats;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MMASRunner {

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(MMASRunner.class);

    public static void main(String[] args) throws ParseException, IOException {

        String instance = "src/main/resources/problems/tsp/oliver30.tsp";

        Problem problem = new TravellingSalesmanProblem(instance);

        MaxMinAntSystem aco = new MaxMinAntSystem(problem);

        aco.setNumberOfAnts(30);
        aco.setNumberOfIterations(1000);
        aco.setAlpha(1.0);
        aco.setBeta(2.0);
        aco.setRho(0.1);
        aco.setStagnation(500);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        es.printStats();
    }

}

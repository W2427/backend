package com.ose.tasks.util.cutstock;

import com.ose.tasks.util.cutstock.aco.ElitistAntSystem;
import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.problem.tsp.TravellingSalesmanProblem;
import com.ose.tasks.util.cutstock.util.ExecutionStats;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EASRunner {

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(EASRunner.class);


    public static void main(String[] args) throws ParseException, IOException {

        String instance = "src/main/resources/problems/tsp/oliver30.tsp";

        Problem problem = new TravellingSalesmanProblem(instance);

        ElitistAntSystem aco = new ElitistAntSystem(problem);

        aco.setNumberOfAnts(30);
        aco.setNumberOfIterations(5000);
        aco.setAlpha(1.0);
        aco.setBeta(2.0);
        aco.setRho(0.1);
        aco.setWeight(6);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        es.printStats();
    }

}

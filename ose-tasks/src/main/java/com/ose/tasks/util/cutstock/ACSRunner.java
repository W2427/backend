package com.ose.tasks.util.cutstock;

import com.ose.tasks.util.cutstock.aco.AntColonySystem;
import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.problem.kp.KnapsackProblem;
import com.ose.tasks.util.cutstock.util.ExecutionStats;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ACSRunner {

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ACSRunner.class);


    public static void main(String[] args) throws ParseException, IOException {

        String instance = "src/main/resources/problems/kp/p06.kp";

        Problem problem = new KnapsackProblem(instance);





        AntColonySystem aco = new AntColonySystem(problem);

        aco.setNumberOfAnts(50);
        aco.setNumberOfIterations(500);
        aco.setAlpha(1.0);
        aco.setBeta(2.0);
        aco.setRho(0.1);
        aco.setOmega(0.1);
        aco.setQ0(0.9);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        es.printStats();
    }

}

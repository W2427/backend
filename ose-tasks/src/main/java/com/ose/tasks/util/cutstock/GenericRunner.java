package com.ose.tasks.util.cutstock;

import com.ose.tasks.util.cutstock.util.Parameters;
import org.apache.commons.cli.*;

import java.io.IOException;

public class GenericRunner {

    protected static Options options;

    public static void main(String[] args) throws ParseException, IOException {


        options = constructTheOptions();























    }

    public static Parameters parse(String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h")) {
            printHelp();
        }

        Parameters parameters = new Parameters();

        for (Option option : options.getOptions()) {
            parameters.put(option.getOpt(), cmd.getOptionValue(option.getOpt()));
        }

        return parameters;
    }

    protected static Options constructTheOptions() {
        final Options options = new Options();

        options.addOption("h", "help", false, "Show the help page");
        options.addOption("p", "problem", true, "The problem addressed");
        options.addOption("aco", "aco", true, "The aco's variation");
        options.addOption("in", "instance", true, "The problem instance");
        options.addOption("n", "ants", true, "The number of ants");
        options.addOption("it", "iterations", true, "The number of iterations");
        options.addOption("a", "alpha", true, "The alpha value");
        options.addOption("b", "beta", true, "The beta value");
        options.addOption("rho", "rho", true, "The evaporation rate");
        options.addOption("w", "weight", true, "The weight for ASRank and EAS algorithms");
        options.addOption("o", "omega", true, "The evaporation rate for the local update rule");
        options.addOption("q0", "q0", true, "A random number uniformly distributed in [0:1]");
        options.addOption("tMin", "tMin", true, "The minimum value of pheronome");
        options.addOption("tMax", "tMax", true, "The maximum value of pheronome");

        return options;
    }

    protected static void printHelp() {

        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("jacof", options);

        System.exit(0);
    }
}

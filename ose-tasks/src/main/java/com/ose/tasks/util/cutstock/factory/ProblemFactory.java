package com.ose.tasks.util.cutstock.factory;

import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.problem.nrp.NextReleaseProblem;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ProblemFactory {

    public static Problem getProblem(String problemName, String problemInstance) throws IOException {

        checkNotNull(problemName, "The problemName parameter should not be null");
        checkArgument(!problemName.isEmpty(), "The problemName parameter cannot be empty");

        if (problemName.equalsIgnoreCase("NRP")) {
            return new NextReleaseProblem(problemInstance);
        } else {
            throw new IllegalArgumentException("The specified problem was not found");
        }
    }
}

package com.ose.tasks.util.cutstock.util;

public class AcoConfig {

    private int antQty;

    private int iterationNumber;

    private double alpha;

    private double beta;

    private double rho;

    private int stagnation;


    public int getAntQty() {
        return antQty;
    }

    public void setAntQty(int antQty) {
        this.antQty = antQty;
    }

    public int getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public int getStagnation() {
        return stagnation;
    }

    public void setStagnation(int stagnation) {
        this.stagnation = stagnation;
    }
}

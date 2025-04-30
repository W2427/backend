package com.ose.tasks.util.cutstock.problem.kp;

import com.google.common.base.Preconditions;
import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.util.io.InstanceReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to create an instance of the Knapsack Problem (01 Knapsack Problem)
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 * @see https://people.sc.fsu.edu/~jburkardt/datasets/knapsack_01/knapsack_01.html
 */
public class KnapsackProblem extends Problem {

    protected int numberOfItems;

    protected double[] profits;

    protected double[] weights;

    protected double capacity;

    /**
     * Sum of the profits
     */
    protected double profitR;

    /**
     * Sum of the weights
     */
    protected double weightR;

    public KnapsackProblem(String filename) throws IOException {

        InstanceReader reader = new InstanceReader(new File(filename));

        this.numberOfItems = reader.readIntegerValue();
        this.weights = reader.readDoubleArray();
        this.profits = reader.readDoubleArray();
        this.capacity = reader.readDoubleValue();

        this.profitR = Arrays.stream(profits).reduce(Double::sum).getAsDouble();
        this.weightR = Arrays.stream(weights).reduce(Double::sum).getAsDouble();
    }

    @Override
    public double evaluate(int[] solution) {

        Preconditions.checkNotNull(solution, "The solution cannot be null");

        double sum = 0.0;

        for (int i = 0; i < solution.length; i++) {
            sum += this.profits[solution[i]];
        }

        return sum;
    }

    @Override
    public boolean better(double s1, double best) {
        return s1 > best;
    }

    public double capacity(int[] solution) {

        Preconditions.checkNotNull(solution, "The solution cannot be null");

        double sum = 0.0;

        for (int i = 0; i < solution.length; i++) {
            sum += this.weights[solution[i]];
        }

        return sum;
    }

    @Override
    public int getNumberOfNodes() {
        return this.numberOfItems;
    }

    @Override
    public int getNumberOfMaterial() {
        return 0;
    }

    @Override
    public int getNumberOfCuts() {
        return 0;
    }

    @Override
    public double getCnn() {
        return this.profitR;
    }

    @Override
    public double getDeltaTau(double tourLength, int i, int j) {
        return tourLength / profitR;
    }

    @Override
    public double getNij(int i, int j) {
        return (profits[j] / weights[j]);
    }

    @Override
    public List<Integer> initNodesToVisit(int startingNode) {

        List<Integer> nodesToVisit = new ArrayList<>();

        for (int i = 0; i < getNumberOfNodes(); i++) {
            if (i != startingNode && weights[i] <= capacity) {
                nodesToVisit.add(new Integer(i));
            }
        }

        return nodesToVisit;
    }

    @Override
    public List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit) {

        List<Integer> nodesToRemove = new ArrayList<Integer>();

        double totalCost = 0.0;

        for (Integer i : tour) {
            totalCost += weights[i];
        }

        for (Integer i : nodesToVisit) {
            if (totalCost + weights[i] > capacity) {
                nodesToRemove.add(i);
            }
        }

        for (Integer i : nodesToRemove) {
            nodesToVisit.remove(i);
        }

        return nodesToVisit;
    }

    @Override
    public List<Integer> updateMaterialNodesToVisit(List<Integer> tour, List<Integer> materialNodesToVisit) {
        return null;
    }

    @Override
    public List<Integer> updateCutNodesToVisit(List<Integer> tour, List<Integer> cutNodesToVisit) {
        return null;
    }

    @Override
    public int[] getNodeNos() {
        return new int[0];
    }

    @Override
    public List<Integer> initCutNodesToVisit(int currentNode) {
        return null;
    }

    @Override
    public List<Integer> initMaterialNodesToVisit(int currentNode) {
        return null;
    }

    @Override
    public double getScrapLimit() {
        return 0;
    }

    @Override
    public String toString() {
        return KnapsackProblem.class.getSimpleName();
    }

    @Override
    public double[] getNodes() {
        return new double[0];
    }
}

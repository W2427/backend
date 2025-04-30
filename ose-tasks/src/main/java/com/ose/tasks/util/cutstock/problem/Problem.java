package com.ose.tasks.util.cutstock.problem;

import java.util.List;

public abstract class Problem {


    public boolean better(int[] s1, int[] best) {
        return better(evaluate(s1), evaluate(best));
    }

    public abstract boolean better(double s1, double best);


    public abstract double evaluate(int[] solution);


    public abstract int getNumberOfNodes();


    public abstract int getNumberOfMaterial();

    public abstract int getNumberOfCuts();


    public abstract double getCnn();


    public abstract double getDeltaTau(double tourLength, int i, int j);


    public abstract double getNij(int i, int j);

    @Override
    public abstract String toString();

    public abstract double[] getNodes();


    public abstract List<Integer> initNodesToVisit(int startingNode);


    public abstract List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit);

    public abstract List<Integer> updateMaterialNodesToVisit(List<Integer> tour, List<Integer> materialNodesToVisit);

    public abstract List<Integer> updateCutNodesToVisit(List<Integer> tour, List<Integer> cutNodesToVisit);

    public abstract int[] getNodeNos();

    public abstract List<Integer> initCutNodesToVisit(int currentNode);

    public abstract List<Integer> initMaterialNodesToVisit(int currentNode);

    public abstract double getScrapLimit();

    public double getCutDelta() {
        return 0.05;
    }
}

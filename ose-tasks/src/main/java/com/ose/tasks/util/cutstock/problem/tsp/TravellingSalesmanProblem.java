package com.ose.tasks.util.cutstock.problem.tsp;

import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.util.NearestNeighbour;
import com.ose.tasks.util.cutstock.util.io.InstanceReader;
import com.ose.tasks.util.cutstock.util.io.TSPLIBReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Travelling Salesman Problem Class
 *
 * @author Thiago Nascimento
 * @version 1.0
 * @since 2014-07-27
 */
public class TravellingSalesmanProblem extends Problem {

    public double Q = 1.0;

    /**
     * Distance Matrix
     */
    protected double[][] distance;

    /**
     * Number of Cities
     */
    protected int numberOfCities;

    /**
     * Nearest Neighbour heuristic
     */
    protected double cnn;

    /**
     * 读取问题描述，从文件
     */
    public TravellingSalesmanProblem(String filename) throws IOException {
        this(filename, false);
    }

    /**
     * 初始化问题，从文本文件
     * numberOfCities
     * distance
     * cnn nearest neighbour
     */
    public TravellingSalesmanProblem(String filename, boolean isTspLibFormmat) throws IOException {

        TSPLIBReader r = new TSPLIBReader(new InstanceReader(new File(filename)));

        numberOfCities = r.getDimension();

        distance = r.getDistance();

        NearestNeighbour nn = new NearestNeighbour();

        this.cnn = evaluate(nn.solve(this));

        System.out.println("Best Solution: " + Arrays.toString(getTheBestSolution()));
        System.out.println("Best Value: " + evaluate(getTheBestSolution()));
    }

    /**
     * 初始化启发系数
     */
    @Override
    public double getNij(int i, int j) {
        return 1.0 / distance[i][j];
    }

    /**
     * 初始化优化之比较函数
     */
    @Override
    public boolean better(double s1, double best) {
        return s1 < best;
    }

    /**
     * 取得两点之间的距离
     */
    public double getDistance(int i, int j) {
        return this.distance[i][j];
    }

    /**
     * 取得最初的最优解
     * 可以考虑采取贪婪算法
     */
    public int[] getTheBestSolution() {
        return new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 23, 25, 26, 27, 28, 29, 1, 0};
    }

    /**
     * 初始化评估方法,总距离
     */
    @Override
    public double evaluate(int[] solution) {

        double totalDistance = 0;

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += distance[i][j];
        }

        return totalDistance;
    }

    /**
     * 取得节点个数
     */
    @Override
    public int getNumberOfNodes() {
        return numberOfCities;
    }

    @Override
    public int getNumberOfMaterial() {
        return 0;
    }

    @Override
    public int getNumberOfCuts() {
        return 0;
    }


    /**
     * 取得总距离
     */
    @Override
    public double getCnn() {
        return cnn;
    }

    /**
     * 取得 信息素增量
     */
    @Override
    public double getDeltaTau(double tourLength, int i, int j) {
        return Q / tourLength;
    }

    @Override
    public String toString() {
        return TravellingSalesmanProblem.class.getSimpleName();
    }

    @Override
    public double[] getNodes() {
        return new double[0];
    }

    /**
     * 初始化访问节点清单
     */
    @Override
    public List<Integer> initNodesToVisit(int startingNode) {

        List<Integer> nodesToVisit = new ArrayList<>();


        for (int i = 0; i < getNumberOfNodes(); i++) {
            if (i != startingNode) {
                nodesToVisit.add(new Integer(i));
            }
        }

        return nodesToVisit;
    }

    /**
     * 遍历节点闭合， 0 + last节点
     */
    @Override
    public List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit) {

        if (nodesToVisit.isEmpty()) {
            if (!tour.get(0).equals(tour.get(tour.size() - 1))) {
                nodesToVisit.add(tour.get(0));
            }
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
    public double getCutDelta() {
        return 0;
    }
}

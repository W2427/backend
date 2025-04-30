package com.ose.tasks.util.cutstock.problem.cs;

import com.google.common.base.Preconditions;
import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.util.CutEntity;
import com.ose.tasks.util.cutstock.util.CuttingMode;
import com.ose.tasks.util.cutstock.util.MaterialEntity;
import com.ose.tasks.util.cutstock.util.io.InstanceReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class to create an instance of the Cutting Stock Problem (General One Dimensional Cutting stock)
 *
 * @author Feng Tieji
 * @version 1.0.0
 */
public class CuttingStockProblem extends Problem {



    protected int numberOfMaterialPipe;


    protected int numberOfCutPipe;


    protected double[] scraps;

    private double cutDelta = 0.05;

    public double getSurplus() {
        return surplus;
    }

    public void setSurplus(double surplus) {
        this.surplus = surplus;
    }


    private double surplus;


    protected int[] cuttingTimes;


    protected double[] materialPipes;


    protected double[] cutPipes;


    protected double scrapLimit;

    /**
     * Sum of the scrap 废料总长度
     */
    protected double scrapSum;

    /**
     * Sum of the suplus 余料总长度
     */
    protected double suplusSum;

    /**
     * Sum of the cuttingTime 切割次数总数
     */
    protected int cuttingTimeSum;

    /**
     * 原料管总长度
     */
    protected double materialPipeSum;

    /**
     * 下料管总长度
     */
    protected double cutPipeSum;





    /**
     * Nearest Neighbour heuristic
     */
    protected double cnn;

    protected int[] solution;

    /**
     * used material length
     */
    protected double usedMaterialLength;


    private double[] nodes;


    public int getNumberOfMaterialPipe() {
        return numberOfMaterialPipe;
    }

    public int getNumberOfCutPipe() {
        return numberOfCutPipe;
    }

    /**
     * file content
     * L1 cut pipe numbers, 切割管的根数
     * L2 cut pipes length list (space split) 切割管 长度列表
     * L3 material pipe numbers, 原料管的根数
     * L4 material pipes length list (space split) 原料管 长度列表
     *
     *
     * L5 scrap length limit, 废料管长度限制
     */
    public CuttingStockProblem(String filename) throws IOException {


        InstanceReader reader = new InstanceReader(new File(filename));

        this.numberOfCutPipe = reader.readIntegerValue();
        this.cutPipes = reader.readDoubleArray();
        this.numberOfCutPipe = cutPipes.length;
        this.numberOfMaterialPipe = reader.readIntegerValue();
        this.materialPipes = reader.readDoubleArray();
        this.numberOfMaterialPipe = materialPipes.length;
        this.scrapLimit = reader.readDoubleValue();


        CuttingMode cm = new CuttingMode();
        solution = cm.solve(this);

        this.cnn = evaluate(solution);

        this.materialPipeSum = Arrays.stream(materialPipes).reduce(Double::sum).getAsDouble();
        this.cutPipeSum = Arrays.stream(cutPipes).reduce(Double::sum).getAsDouble();
        this.nodes = initNodes();
    }

    public CuttingStockProblem(List<CutEntity> cutEntities,
                               List<MaterialEntity> materialEntities,
                               double scrapLimit,
                               double cutDelta) {

        this.numberOfCutPipe = cutEntities.size();
        double[] tempArr = new double[cutEntities.size()];
        for (int i = 0; i < cutEntities.size(); i++) {
            tempArr[i] = cutEntities.get(i).getLength().doubleValue();
        }

        double[] tempArr1 = new double[materialEntities.size()];
        for (int i = 0; i < materialEntities.size(); i++) {
            tempArr1[i] = materialEntities.get(i).getLength().doubleValue();
        }
        this.cutPipes = tempArr;
        this.numberOfMaterialPipe = materialEntities.size();
        this.materialPipes = tempArr1;
        this.scrapLimit = scrapLimit;

        if(cutDelta <= 0) {
            this.cutDelta = 0.05;
        } else {
            this.cutDelta = cutDelta;
        }


        CuttingMode cm = new CuttingMode();
        solution = cm.solve(this);
        this.cnn = evaluate(solution);

        this.materialPipeSum = Arrays.stream(materialPipes).reduce(Double::sum).getAsDouble();
        this.cutPipeSum = Arrays.stream(cutPipes).reduce(Double::sum).getAsDouble();
        this.nodes = initNodes();
    }


    /**
     * 方案评估的计算函数
     *
     * @param solution solution为一个方案标记的整形数组，标记了方案的次序
     * @return
     */
    @Override
    public double evaluate(int[] solution) {

        Preconditions.checkNotNull(solution, "The solution cannot be null");

        double scrap = 0.0;

        double cutSurplusSum = 0.0;

        double usedMaterialLength = 0.0;
        double cutSurplus = 0.0;

        for (int i = 0; i < solution.length; i++) {

            if (solution[i] > numberOfCutPipe - 1) {
                if (cutSurplus < 0) cutSurplus = 0;
                cutSurplusSum = cutSurplusSum + cutSurplus;
                cutSurplus = materialPipes[solution[i] - numberOfCutPipe];
                usedMaterialLength = usedMaterialLength + materialPipes[solution[i] - numberOfCutPipe];
            } else {
                cutSurplus = cutSurplus - cutPipes[solution[i]] - getCutDelta();
            }
        }
        if (cutSurplus > 0) cutSurplusSum = cutSurplusSum + cutSurplus;
/*
		for (int i = 0; i < solution.length; i++) {

            double[] pipeInfo = getScrap(cutModes[solution[i]]);
			scrap += pipeInfo[0];
            usedMaterialLength += pipeInfo[1];
		}*/

        this.usedMaterialLength = usedMaterialLength;

        return cutSurplusSum;
    }

    @Override
    public boolean better(double s1, double best) {
        return s1 < best;
    }


    private double[] getScrap(String mode) {

        Preconditions.checkNotNull(mode, "The mode/solution cannot be null");

        double[] scrap = new double[2];
        double materialLength = 0.0;
        double cutPipeLength = 0.0;
        String[] md = mode.split(",");
        Set<Integer> modes = new HashSet<>();
        for (int i = 0; i < md.length; i++) {
            modes.add(Integer.parseInt(md[i]));
        }
        for (Integer i : modes) {
            if (i > numberOfCutPipe - 1) {
                materialLength = materialPipes[i - numberOfCutPipe];

            } else {
                cutPipeLength += cutPipes[i];
            }
        }

        scrap[0] = materialLength - cutPipeLength;
        scrap[1] = materialLength;





        return scrap;
    }

    public double[] getCutPipes() {
        return cutPipes;
    }

    public double[] getMaterialPipes() {
        return materialPipes;
    }


    @Override
    public int getNumberOfNodes() {
        return this.numberOfCutPipe + this.numberOfMaterialPipe;
    }

    @Override
    public int getNumberOfMaterial() {
        return this.materialPipes.length;
    }

    @Override
    public int getNumberOfCuts() {
        return cutPipes.length;
    }

    @Override
    public double getCnn() {
        return cnn;
    }


    @Override
    public double getDeltaTau(double tourLength, int i, int j) {
        double dltTau = 0.0;
        if (tourLength == 0) return 1.0;
        else return (usedMaterialLength * 0.01) / tourLength;

    }



    @Override
    public double getNij(int i, int j) {
        if (materialPipes[j - numberOfCutPipe] == cutPipes[i]) return 1;
        else return 1 / (materialPipes[j] - cutPipes[i]);
    }

    @Override
    public List<Integer> initNodesToVisit(int startingNode) {

        List<Integer> nodesToVisit = new ArrayList<>();

        for (int i = 0; i < getNumberOfNodes(); i++) {

            if (i != startingNode)
                nodesToVisit.add(new Integer(i));

        }

        return nodesToVisit;
    }

    @Override
    public List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit) {

/*        List<Integer> nodesToRemove = new ArrayList<Integer>();

        double totalCost = 0.0;
        int cutPipeCnt = 0;
        for(Integer i:tour){
            String[] tempArr = cutModes[i].split(",");
            Integer materialCnt = Integer.parseInt(tempArr[tempArr.length-1]);
            nodesToRemove.add(materialCnt);
            cutPipeCnt += tempArr.length -1;
        }

        if(cutPipeCnt == cutPipes.length) {
            nodesToVisit.clear();
        } else{
            for (Integer i : nodesToRemove) {
                nodesToVisit.remove(i);
            }
        }*/
        return nodesToVisit;
    }

    @Override
    public List<Integer> updateMaterialNodesToVisit(List<Integer> tour, List<Integer> materialNodesToVisit) {

        List<Integer> nodesToVisit = new ArrayList<>();








        return nodesToVisit;
    }

    @Override
    public List<Integer> updateCutNodesToVisit(List<Integer> tour, List<Integer> cutNodesToVisit) {


        List<Integer> nodesToVisit = new ArrayList<>();








        return nodesToVisit;
    }

    @Override
    public int[] getNodeNos() {
        List<Integer> nos = new ArrayList<>();
        for (int i = 0; i < getNumberOfNodes(); i++) {
            nos.add(new Integer(i));
        }

        return nos.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public List<Integer> initCutNodesToVisit(int currentNode) {
        List<Integer> nodesToVisit = new ArrayList<>();

        for (int i = 0; i < getNumberOfCuts(); i++) {
            if (i != currentNode)
                nodesToVisit.add(new Integer(i));
        }
        return nodesToVisit;
    }

    @Override
    public List<Integer> initMaterialNodesToVisit(int currentNode) {

        List<Integer> nodesToVisit = new ArrayList<>();

        for (int i = 0; i < getNumberOfMaterialPipe(); i++) {
            if (i != currentNode - cutPipes.length)
                nodesToVisit.add(new Integer(i + cutPipes.length));
        }
        return nodesToVisit;
    }

    @Override
    public String toString() {
        return CuttingStockProblem.class.getSimpleName();
    }

    @Override
    public double[] getNodes() {

        return nodes;
    }

    private double[] initNodes() {
        double[] dbl = new double[cutPipes.length + materialPipes.length];
        for (int i = 0; i < cutPipes.length; i++) {
            dbl[i] = cutPipes[i];
        }
        for (int i = 0; i < materialPipes.length; i++) {
            dbl[i + cutPipes.length] = materialPipes[i];
        }
        return dbl;
    }

    @Override
    public double getScrapLimit() {
        return this.scrapLimit;
    }

    @Override
    public double getCutDelta() {
        return cutDelta;
    }

    public void setCutDelta(double cutDelta) {
        this.cutDelta = cutDelta;
    }
}

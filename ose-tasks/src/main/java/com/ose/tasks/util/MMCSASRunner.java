package com.ose.tasks.util;

import com.ose.tasks.util.cutstock.aco.CSMaxMinAntSystem;
import com.ose.tasks.util.cutstock.problem.Problem;
import com.ose.tasks.util.cutstock.problem.cs.CuttingStockProblem;
import com.ose.tasks.util.cutstock.util.*;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class MMCSASRunner {

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(MMCSASRunner.class);

    /**
     * @param cutEntities      需要切割的实体列表，按照长度有小到大排列
     * @param materialEntities 使用的原材料列表，按长度有小到大排列
     * @param scrapLimit       废料门槛值
     * @param cutDelta         切割的宽度
     * @param acoConfig        蚁群参数
     * @return *************************************************
     * 系统返回值是一个类对象 nestResult，包含以下信息
     * boolean nestResult; // 套料结果 boolean
     * double executionTime; 套料运行时间
     * List<CutStockResult> csResults; 使用到的材料管的套料结果组合
     * double surplusSumLength; 总的余料长度
     * double materialSumLength; 用料总长度
     * double cutSumLength; 切割管子总长度
     * double scrapSumLength; 废料总长度
     * csResults 下料组合结果中，包含
     * String materialId; 原料管ID
     * double materialLength; 原料管长度
     * Set<CutEntity> nestEntities; 切割管组合，包含了那些切割的管子
     * double surplusLength; 原料管使用后废料长度
     * double scrapLength; 原料管使用后余料长度
     * **************************************
     */
    public static NestResult cutStock(List<CutEntity> cutEntities,
                                      List<MaterialEntity> materialEntities,
                                      double scrapLimit,
                                      double cutDelta,
                                      AcoConfig acoConfig) {
        Problem problem = new CuttingStockProblem(cutEntities, materialEntities, scrapLimit, cutDelta);

        CSMaxMinAntSystem aco = new CSMaxMinAntSystem(problem);

        if (acoConfig != null) {
            aco.setNumberOfAnts(acoConfig.getAntQty());
            aco.setNumberOfIterations(acoConfig.getIterationNumber());
            aco.setAlpha(acoConfig.getAlpha());
            aco.setBeta(acoConfig.getBeta());
            aco.setRho(acoConfig.getRho());
            aco.setStagnation(acoConfig.getStagnation());
        } else {
            aco.setNumberOfAnts(((CuttingStockProblem) problem).getNumberOfMaterialPipe() * 2);
            aco.setNumberOfIterations(100);
            aco.setAlpha(1.0);
            aco.setBeta(2.0);
            aco.setRho(0.1);
            aco.setStagnation(200);
        }



//        ((CuttingStockProblem) problem).setCutDelta(cutDelta);
        NestResult nestResult = new NestResult();

        if(materialEntities.get(materialEntities.size()-1).getLength().doubleValue() < cutEntities.get(cutEntities.size()-1).getLength().doubleValue()) {
            //如果原材料最长的一根短于 切割 的最长的一根，则 报没有方案退出
            logger.info("The material length can not meet cut Entity Length");
            nestResult.setNestResultDesc("The material length can not meet cut Entity Length");
            nestResult.setNestResult(false);

        } else {

            ExecutionStats es = ExecutionStats.execute(aco, problem);
            if (es.bestSolution == null) {
                logger.info("there is no enough materials");
                nestResult.setNestResultDesc("There is no enough materials");
                nestResult.setNestResult(false);
            } else {
                logger.info("Best Solution: " + Arrays.toString(es.bestSolution));
                int[] bestSolution = es.bestSolution;
                nestResult = summarize(es.bestSolution,
                    cutEntities,
                    materialEntities,
                    problem.getScrapLimit(),
                    problem.getCutDelta());
                nestResult.setExecutionTime(String.valueOf(es.executionTime));
                System.out.println(nestResult.toString());
            }
        }
        return nestResult;

    }

    /**
     * 下料管段清单 cutEntities
     * 原材料清单 materialEntities
     * 废料长度标准 0.08m
     * 单位为米
     *
     * @param args
     * @throws ParseException
     * @throws IOException
     */
    public static void main(String[] args) throws ParseException, IOException {


        String instance = "src/main/resources/problems/cs/cs.cs";

        List<CutEntity> cutEntities = initCutEntities();
//                new ArrayList(Arrays.asList(0.5,0.6,0.6,0.7,0.7,0.8,0.9,0.9,1.0,1.3,1.4));

        List<MaterialEntity> materialEntities = initMaterialEntities();
//                new ArrayList(Arrays.asList(2.4,3.01,4.01,6.0));

        double scrapLimit = 0.08;

//		Problem problem = new CuttingStockProblem(instance);

        Problem problem = new CuttingStockProblem(cutEntities, materialEntities, scrapLimit, 0.05);

        CSMaxMinAntSystem aco = new CSMaxMinAntSystem(problem);

        aco.setNumberOfAnts(((CuttingStockProblem) problem).getNumberOfCutPipe() * 2);
        aco.setNumberOfIterations(100);
        aco.setAlpha(2);//(1.0);
        aco.setBeta(8);//(2.0);
        aco.setRho(0.3);//(0.1);//(0.1);
        aco.setStagnation(500);

        AcoConfig acoConfig = new AcoConfig();

        acoConfig.setAntQty(((CuttingStockProblem) problem).getNumberOfCutPipe() * 2);
        acoConfig.setIterationNumber(100);
        acoConfig.setAlpha(2);
        acoConfig.setBeta(8);
        acoConfig.setRho(0.3);
        acoConfig.setStagnation(500);

//        NestResult nr =
        cutStock(cutEntities, materialEntities, 0.08, 0.05, acoConfig);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        if (es.bestSolution == null) {
            logger.info("there is no enough materials");
            NestResult nestResult = new NestResult();
            nestResult.setNestResult(false);
        } else {
            logger.info("Best Solution: " + Arrays.toString(es.bestSolution));
            int[] bestSolution = es.bestSolution;
            NestResult nestResult = summarize(es.bestSolution,
                cutEntities,
                materialEntities,
                0.08,
                problem.getCutDelta());
            nestResult.setExecutionTime(String.valueOf(es.executionTime));
            System.out.println(es.aco.getGlobalBest().getTourLength());

        }
//		es.printStats();
    }

    public static NestResult summarize(int[] solution,
                                       List<CutEntity> cutEntities,
                                       List<MaterialEntity> materialEntities,
                                       double scrapLimit,
                                       double cutDelta) {

        NestResult nestResult = new NestResult();
        List<CutStockResult> csResults = new ArrayList<>();

        int cutPipeCnt = cutEntities.size();
        double scrap = 0.0;

        double surplusSumLength = 0.0;

        double scrapSumLength = 0.0;

        double cutSumLength = 0.0;

        double usedMaterialLength = 0.0;
        double cutSurplus = 0.0;
        int materialNo = -1;
        if (solution.length == 0) {
            nestResult.setNestResult(false);
            return nestResult;
        }
//        solution=new int[]{10,9,5,13,6,8,4,7,1,2,0,3};
        Set<CutEntity> ces = new HashSet<>();
        for (int i = 0; i < solution.length; i++) {

            if (solution[i] > cutEntities.size() - 1) { //如果是原料管
                if (materialNo != -1) { //第一根原料管
                    CutStockResult csResult = new CutStockResult();
                    materialEntities.get(materialNo - cutPipeCnt).setUsed(true);
                    materialEntities.get(materialNo - cutPipeCnt).setScrapLength(BigDecimal.valueOf(cutSurplus));//todo <0 -> 0
                    csResult.setMaterialId(materialEntities.get(materialNo - cutPipeCnt).getId());
                    csResult.setMaterialLength(materialEntities.get(materialNo - cutPipeCnt).getLength());
                    csResult.setNestEntities(ces);
                    if (cutSurplus <= scrapLimit) { //废料
                        if (cutSurplus < 0) cutSurplus = 0;
                        scrapSumLength += cutSurplus;
                        csResult.setScrapLength(BigDecimal.valueOf(cutSurplus));
                        csResult.setSurplusLength(new BigDecimal(0.0));
                    } else {
                        surplusSumLength += cutSurplus;
                        csResult.setSurplusLength(BigDecimal.valueOf(cutSurplus));
                        csResult.setScrapLength(new BigDecimal(0.0));
                    }
                    csResults.add(csResult);
                    ces = new HashSet<>();

                }
                materialNo = solution[i];
//                materialEntities.get(solution[i]-cutPipeCnt).setUsed(true);
//                cutSurplusSum = cutSurplusSum + cutSurplus;//总的余料长度
                cutSurplus = materialEntities.get(solution[i] - cutPipeCnt).getLength().doubleValue();
                usedMaterialLength = usedMaterialLength + cutSurplus;

            } else {
                cutSurplus = cutSurplus - cutEntities.get(solution[i]).getLength().doubleValue() - cutDelta;
                ces.add(cutEntities.get(solution[i]));
                cutSumLength += cutEntities.get(solution[i]).getLength().doubleValue();

            }
        }
        if (materialNo != -1) {
            materialEntities.get(materialNo - cutPipeCnt).setUsed(true);
            materialEntities.get(materialNo - cutPipeCnt).setScrapLength(BigDecimal.valueOf(cutSurplus));
            CutStockResult csResult = new CutStockResult();
            csResult.setMaterialId(materialEntities.get(materialNo - cutPipeCnt).getId());
            csResult.setMaterialLength(materialEntities.get(materialNo - cutPipeCnt).getLength());
            csResult.setNestEntities(ces);
            if (cutSurplus <= scrapLimit) { //废料
                if (cutSurplus < 0) cutSurplus = 0;
                scrapSumLength += cutSurplus;
                csResult.setScrapLength(BigDecimal.valueOf(cutSurplus));
                csResult.setSurplusLength(new BigDecimal(0.0));
            } else {
                surplusSumLength += cutSurplus;
                csResult.setSurplusLength(BigDecimal.valueOf(cutSurplus));
                csResult.setScrapLength(BigDecimal.valueOf(0.0));
            }
            csResults.add(csResult);
        }

        nestResult.setCsResults(csResults);
        nestResult.setCutSumLength(BigDecimal.valueOf(cutSumLength));
        nestResult.setMaterialSumLength(BigDecimal.valueOf(usedMaterialLength));
        nestResult.setNestResult(true);
        nestResult.setSurplusSumLength(BigDecimal.valueOf(surplusSumLength));
        nestResult.setScrapSumLength(BigDecimal.valueOf(scrapSumLength));

        return nestResult;
    }

    private static List<CutEntity> initCutEntities() {
        List<CutEntity> cutEntities = new ArrayList<>();
//        double[] cuts = {0.5,0.6,0.6,0.6,0.7,0.7,0.7,0.8,0.8,0.9,0.9,0.9,0.9,1.0,1.1,1.2,1.3,1.3,1.4};
        double[] cuts = {0.47942, 1.154125, 7.38505, 11.999925, 2.771775, 5.819775};

        for (int i = 0; i < cuts.length; i++) {
            CutEntity cutEntity = new CutEntity();
            cutEntity.setLength(BigDecimal.valueOf(cuts[i]));
            cutEntity.setId((long) i);
            cutEntities.add(cutEntity);
        }
        return cutEntities;
    }

    private static List<MaterialEntity> initMaterialEntities() {
        List<MaterialEntity> materialEntities = new ArrayList<>();
//        double[] materials = {2.4,3.01,4.01,4.5};//,5,5.2,5.5,6,6.5};
        double[] materials = {5.680175};//,5,5.2,5.5,6,6.5};

        for (int i = 0; i < materials.length; i++) {
            MaterialEntity materialEntity = new MaterialEntity();
            materialEntity.setLength(BigDecimal.valueOf(materials[i]));
            materialEntity.setId((long) (19 + i));
            materialEntities.add(materialEntity);
        }
        return materialEntities;
    }
}

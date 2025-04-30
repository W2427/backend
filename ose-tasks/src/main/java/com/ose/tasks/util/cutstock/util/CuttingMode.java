package com.ose.tasks.util.cutstock.util;

import com.ose.tasks.util.cutstock.problem.cs.CuttingStockProblem;
import com.ose.tasks.util.cutstock.util.random.JMetalRandom;

import java.util.*;

/**
 * Least scrap Algorithm
 *
 * @author Feng Tieji
 * 切割模式 取得最少余料的初始算法
 * @version 1.0
 * @since 2019-06-21
 */
public class CuttingMode {

    protected JMetalRandom rand = JMetalRandom.getInstance();


    protected String[] cutModes;

    public String[] getCutModes() {
        return cutModes;
    }

    /**
     * pipesToCutList 记录下料管的编号集合，由短到长排列{0,1,2,3,4,5},下料完成之后就删除一个
     * pipesToCut 记录下料管的长度集合，由短到长排列{0.1,0.2,0.3,0.5,0.6,0.8},下料完成之后删除一个
     * currentCutPipeNo 记录当前下料管在集合中的编号，如 0，1等
     * currentCutPipeLength 记录当前下料管的长度，如0.1
     * currentCutPipeCnt 记录切割管在 切割管集合中的位置，数组位置
     * pipesMaterialList 记录材料管的编号，由短到长排列{0,1,2,3},切割一段后删除一个
     * pipesMaterial 记录原料管的长度集合，由短到长排列{2.1，2.5，2.7，2.9}，切割一段后删除一个
     * currentMaterialNo 记录当前原料管在集合中的编号，如0，1等
     * currentMaterialLength 记录原料管的长度
     * currentMaterialCnt 记录切割管在 切割管集合中的位置，数组位置
     *
     * @param p
     * @return
     */
    public int[] solve(CuttingStockProblem p) {

        List<Integer> pipesToCutList = new ArrayList<Integer>();
        List<Double> pipesToCut = new ArrayList<>();
        int currentCutPipeNo = 0;
        Double currentCutPipeLength = 0.0;
        int currentCutPipeCnt = -1;

        int usedMaterialPipe = 0;

        List<Integer> pipesMaterialList = new ArrayList<>();
        List<Double> pipesMaterial = new ArrayList<>();
        int currentMaterialNo = 0;
        Double currentMaterialLength = 0.0;
        int currentMaterialCnt = -1;

        List<Integer> solution = new ArrayList<Integer>();
        Boolean materialPipeUsed = true;

        Double surplusLen = 0.0;

        Double scrapLimit = p.getScrapLimit();



        for (int i = 0; i < p.getCutPipes().length; i++) {
            pipesToCut.add(new Double(p.getCutPipes()[i]));
        }

        Collections.sort(pipesToCut);



        for (int i = 0; i < p.getMaterialPipes().length; i++) {
            pipesMaterial.add(new Double(p.getMaterialPipes()[i]));
        }
        Collections.sort(pipesMaterial);

        int materialCnt = pipesMaterial.size();



        for (int i = 0; i < pipesToCut.size(); i++) {
            pipesToCutList.add(new Integer(i));
        }


        for (int i = 0; i < pipesMaterial.size(); i++) {
            pipesMaterialList.add(i + pipesToCut.size());
        }




        int currentCutPipe = 0;
        int currentMaterialPipe = 0;

        Set<Integer> cutMode = new HashSet<>();
        while (!pipesToCutList.isEmpty() && usedMaterialPipe <= materialCnt) {

            if (materialPipeUsed) {
                usedMaterialPipe++;
                cutMode = new HashSet<>();
                currentCutPipeCnt = pipesToCut.size() - 1;
                currentCutPipeNo = pipesToCutList.get(currentCutPipeCnt);
                currentCutPipeLength = pipesToCut.get(currentCutPipeCnt);
                Double maxCutLen = pipesToCut.get(pipesToCut.size() - 1);
                currentMaterialCnt = -1;


                for (int i = 0; i < pipesMaterial.size(); i++) {
                    if (i == pipesMaterial.size() - 1) {
                        if (pipesMaterial.get(i) < maxCutLen) {
                            currentMaterialCnt = -1;
                        } else {
                            currentMaterialCnt = i;
                        }
                    } else if (pipesMaterial.get(i) < maxCutLen && maxCutLen <= pipesMaterial.get(i + 1)) {
                        currentMaterialCnt = i + 1;
                        break;
                    } else if(pipesMaterial.get(i) >= maxCutLen) {

                        currentMaterialCnt = 0;
                        break;
                    }
                }
                if (currentMaterialCnt > -1) {
                    currentMaterialLength = pipesMaterial.get(currentMaterialCnt);
                    currentMaterialNo = pipesMaterialList.get(currentMaterialCnt);
                    surplusLen = currentMaterialLength - currentCutPipeLength - p.getCutDelta();
                    cutMode.add(currentMaterialNo);
                    cutMode.add(currentCutPipeNo);
                    solution.add(currentMaterialNo);
                    solution.add(currentCutPipeCnt);
                    pipesMaterialList.remove(currentMaterialCnt);
                    pipesMaterial.remove(currentMaterialCnt);
                    pipesToCutList.remove(currentCutPipeCnt);
                    pipesToCut.remove(currentCutPipeCnt);
                    materialPipeUsed = false;
                    if (surplusLen < scrapLimit) {
                        materialPipeUsed = true;
                    }
                }

            } else {

                currentCutPipeCnt = -1;
                for (int i = 0; i < pipesToCut.size(); i++) {
                    if (i == pipesToCut.size() - 1) {
                        if (pipesToCut.get(i) <= surplusLen) {
                            currentCutPipeCnt = i;
                        }
                    } else if (pipesToCut.get(i) > surplusLen && i == 0) {

                        break;
                    } else if (pipesToCut.get(i) <= surplusLen && pipesToCut.get(i + 1) > surplusLen) {
                        currentCutPipeCnt = i;
                        break;
                    }

                }

                if (currentCutPipeCnt == -1) {
                    materialPipeUsed = true;
                } else {
                    currentCutPipeNo = pipesToCutList.get(currentCutPipeCnt);
                    cutMode.add(currentCutPipeNo);
                    currentCutPipeLength = pipesToCut.get(currentCutPipeCnt);
                    surplusLen = surplusLen - currentCutPipeLength - p.getCutDelta();
                    pipesToCutList.remove(currentCutPipeCnt);
                    pipesToCut.remove(currentCutPipeCnt);
                    solution.add(currentCutPipeCnt);
                    if (surplusLen < scrapLimit) {
                        materialPipeUsed = true;
                    }
                }









            }

        }


        return solution.stream().mapToInt(x -> x).toArray();
    }

}

package com.ose.tasks.util.cutstock.aco.ant.exploration;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.Ant;
import com.ose.tasks.util.cutstock.aco.ant.selection.AbstractAntSelection;
import com.ose.tasks.util.cutstock.aco.ant.selection.RouletteWheel;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * This class represents how an ant in AS algorithm chooses the next node
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class CSPseudoRandomProportionalRule extends AbstractAntExploration {

    /**
     * Constructor
     *
     * @param aco          The ant colony optimization used
     * @param antSelection The ant selection used
     */
    public CSPseudoRandomProportionalRule(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
    }

    /**
     * Constructor by using RouletteWheel as default ant selection
     *
     * @param aco The ant colony optimization used
     */
    public CSPseudoRandomProportionalRule(ACO aco) {
        this(aco, new RouletteWheel());
    }

    @Override
    public int getNextNode(Ant ant, int i) {
        return doCutPipeExploration(ant, i);
    }

    public int doCutPipeExploration(Ant ant, int i) {

        int nextNode = -1;

        double sum = 0.0;

        double[] tij = new double[aco.getProblem().getNumberOfNodes()];
        double[] nij = new double[aco.getProblem().getNumberOfNodes()];


        if (ant.getSurplus() < aco.getProblem().getScrapLimit()) {
            return doMaterialPipeExploration(ant, i);
        }
        List<Integer> availableCutNodesToVisit = ant.getAvailableCutNodesToVisit();

        if (availableCutNodesToVisit.size() == 0) {
            return doMaterialPipeExploration(ant, i);
        }







        for (Integer j : availableCutNodesToVisit) {

            checkState(aco.getGraph().getTau(i, j) != 0.0, "The tau(i,j) should not be 0.0");


            double tempSurplus = ant.getSurplus() - ant.getPipes()[j] - ant.getAco().getProblem().getCutDelta();
            if (tempSurplus <= aco.getProblem().getScrapLimit()) tempSurplus = aco.getProblem().getScrapLimit();
            tij[j] = Math.pow(aco.getGraph().getTau(i, j), aco.getAlpha());

            nij[j] = Math.pow(1 / tempSurplus, aco.getBeta());

            sum += tij[j] * nij[j];
        }

        checkState(sum != 0.0, "The sum cannot be 0.0");

        double[] probability = new double[aco.getProblem().getNumberOfCuts()];

        double sumProbability = 0.0;

        for (Integer j : availableCutNodesToVisit) {

            probability[j] = (tij[j] * nij[j]) / sum;

            sumProbability += probability[j];
        }


        nextNode = antSelection.select(probability, sumProbability);

        checkState(nextNode != -1, "The next node should not be -1");

        return nextNode;
    }

    public int doMaterialPipeExploration(Ant ant, int i) {

        List<Integer> availableMaterialNodesToVisit = ant.getAvailableMaterialNodesToVisit();

        double[] pipes = ant.getPipes();

        List<Integer> cutNodes = ant.getCutNodesToVisit();
        List<Integer> materialNodes = ant.getMaterialNodesToVisit();
        if (materialNodes.size() == 0) {
            return -1;
        }
        double maxMaterialLength = ant.getPipes()[materialNodes.get(materialNodes.size() - 1)];
        double maxCutLength = ant.getPipes()[cutNodes.get(cutNodes.size() - 1)];
        Double gap = maxMaterialLength - maxCutLength;
        if (gap < 0) return -1;

        Double randDbl = rand.nextDouble(maxCutLength, maxCutLength + gap);
        int target = -1;
        for (int k = availableMaterialNodesToVisit.size() - 1; k >= 0; k--) {
            if (k == 0 && pipes[availableMaterialNodesToVisit.get(k)] >= maxCutLength) {
                target = 0;
                break;
            } else if (pipes[availableMaterialNodesToVisit.get(k)] >= randDbl && k > 0 && pipes[availableMaterialNodesToVisit.get(k - 1)] < randDbl) {
                target = k;
                break;
            } else {
                target = -1;
            }
        }

        if (target == -1)
            return -1;
        int nextNode = availableMaterialNodesToVisit.get(target);

        return nextNode;
    }

    public int doExploration(Ant ant, int i) {

        int nextNode = -1;


        double sum = 0.0;

        double[] tij = new double[aco.getProblem().getNumberOfNodes()];
        double[] nij = new double[aco.getProblem().getNumberOfNodes()];


        for (Integer j : ant.getNodesToVisit()) {

            checkState(aco.getGraph().getTau(i, j) != 0.0, "The tau(i,j) should not be 0.0");

            tij[j] = Math.pow(aco.getGraph().getTau(i, j), aco.getAlpha());
            nij[j] = Math.pow(aco.getProblem().getNij(i, j), aco.getBeta());

            sum += tij[j] * nij[j];
        }

        checkState(sum != 0.0, "The sum cannot be 0.0");

        double[] probability = new double[aco.getProblem().getNumberOfNodes()];

        double sumProbability = 0.0;

        for (Integer j : ant.getNodesToVisit()) {

            probability[j] = (tij[j] * nij[j]) / sum;

            sumProbability += probability[j];
        }


        nextNode = antSelection.select(probability, sumProbability);

        checkState(nextNode != -1, "The next node should not be -1");

        return nextNode;
    }

    @Override
    public String toString() {
        return CSPseudoRandomProportionalRule.class.getSimpleName() + " with " + antSelection;
    }
}

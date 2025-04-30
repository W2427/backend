package com.ose.tasks.util.cutstock.aco.ant;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AbstractAntInitialization;
import com.ose.tasks.util.cutstock.util.random.JMetalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents an ant and its process of building a solution
 * for a given addressed problem.
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public class Ant extends Observable implements Runnable {

    /**
     * Identifier
     */
    protected int id;

    private boolean resetOk = false;

    /**
     * The ant colony optimization
     */
    public ACO aco;

    /**
     * The Current Node
     */
    public int currentNode;

    /**
     * The path traveled
     */
    public int[][] path;

    /**
     * The tour built
     */
    public List<Integer> tour;

    /**
     * The list with the nodes to visit
     */
    public List<Integer> nodesToVisit;


    /**
     * The list with the material nodes to visit
     */
    public List<Integer> materialNodesToVisit;


    /**
     * The list with the cut nodes to visit
     */
    private List<Integer> cutNodesToVisit;


    /**
     * The tour length traveled
     */
    protected double tourLength;

    /**
     * The ant initialization
     */
    protected AbstractAntInitialization antInitialization;

    /**
     * cut surplus
     */
    private double surplus;

    private double[] pipes;

    private int[] pipeNos;

    public double getSurplus() {
        return surplus;
    }

    public double[] getPipes() {
        return pipes;
    }

    public int[] getPipeNos() {
        return pipeNos;
    }

    public int getCutPipeCnt() {
        return cutPipeCnt;
    }

    public int getMaterialPipeCnt() {
        return materialPipeCnt;
    }

    private int cutPipeCnt;

    private int materialPipeCnt;

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(Ant.class);

    public boolean isTourResult() {
        return tourResult;
    }

    public void setTourResult(boolean tourResult) {
        this.tourResult = tourResult;
    }

    private boolean tourResult = true;

    /**
     * Constructor
     *
     * @param aco The ant colony optimization
     * @param id  The ant's id
     */
    public Ant(ACO aco, int id) {

        checkNotNull(aco, "The aco should not be null");
        checkArgument(id >= 0, "The id should be > 0. Passed: %s", id);

        this.aco = aco;
        this.id = id;
        this.nodesToVisit = new ArrayList<>();
        this.cutPipeCnt = aco.getProblem().getNumberOfCuts();
        this.materialPipeCnt = aco.getProblem().getNumberOfMaterial();
        this.tour = new ArrayList<>();
        this.path = new int[aco.getProblem().getNumberOfNodes()][aco.getProblem().getNumberOfNodes()];
    }

    /**
     * Restart all information before starting the search process
     */
    public void reset() {


        resetOk = true;
        pipes = aco.getProblem().getNodes();
        pipeNos = aco.getProblem().getNodeNos();
        Double gap = pipes[pipes.length - 1] - pipes[cutPipeCnt - 1];
        double maxCutLength = pipes[aco.getProblem().getNumberOfCuts() - 1];
        int target = getInitNode(maxCutLength, gap, pipes);
        if (target != -1) {
            this.currentNode = target;
            this.surplus = pipes[target];
        } else {
            resetOk = false;
        }

        this.tourLength = 0.0;
        this.tour.clear();
        this.nodesToVisit = aco.getProblem().initNodesToVisit(this.currentNode);
        this.cutNodesToVisit = aco.getProblem().initCutNodesToVisit(this.currentNode);
        this.materialNodesToVisit = aco.getProblem().initMaterialNodesToVisit(this.currentNode);
        this.tour.add(new Integer(currentNode));
        this.path = new int[aco.getProblem().getNumberOfNodes()][aco.getProblem().getNumberOfNodes()];
        this.tourResult = true;
    }


    private int getInitNode(double maxCutLength, Double gap, double[] pipes) {
        JMetalRandom rand = JMetalRandom.getInstance();
        Double randDbl = rand.nextDouble(maxCutLength, maxCutLength + gap);
        int target = -1;
        for (int i = pipeNos.length - 1; i >= cutPipeCnt; i--) {
            if (i == cutPipeCnt && pipes[pipeNos[i]] >= maxCutLength) {
                target = cutPipeCnt;
                break;
            } else if (pipes[pipeNos[i]] >= randDbl && (i - cutPipeCnt > 0) && pipes[pipeNos[i - 1]] < randDbl) {
                target = i;
                break;
            }
        }
        if (target == -1) {
            return target;
        }
        return pipeNos[target];
    }

    @Override
    public void run() {
        reset();
        explore();
        setChanged();
        notifyObservers(this);
    }

    /**
     * Construct the ant's solution
     */
    public boolean explore() {
        if(!resetOk) {
            setTourResult(false);
            tour.clear();
            return false;
        }

        while (!nodesToVisit.isEmpty()) {


            int nextNode = aco.getAntExploration().getNextNode(this, currentNode);


            nodesToVisit.remove(new Integer(nextNode));
            if (nextNode == -1) {

                setTourResult(false);
                tour.clear();
                return false;
            } else if (nextNode > cutPipeCnt - 1) {

                materialNodesToVisit.remove(new Integer(nextNode));
                surplus = pipes[nextNode];
            } else {
                cutNodesToVisit.remove(new Integer(nextNode));
                if (cutNodesToVisit.size() == 0) nodesToVisit.clear();

                surplus = surplus - pipes[nextNode] - aco.getProblem().getCutDelta();
                if(surplus < 0) {
                    surplus = 0;
                }
            }


            if (aco.getAntLocalUpdate() != null) {
                aco.getAntLocalUpdate().update(currentNode, nextNode);
            }


            tour.add(new Integer(nextNode));


            path[currentNode][nextNode] = 1;
            path[nextNode][currentNode] = 1;


            nodesToVisit = aco.getProblem().updateNodesToVisit(tour, nodesToVisit);







            currentNode = nextNode;
        }
        return true;
    }

    /**
     * Convert from an ant solution to integer array
     *
     * @return the solution formatted in integer array
     */
    public int[] getSolution() {
        return tour.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Clone an ant
     */
    public Ant clone() {
        Ant ant = new Ant(aco, id);

        ant.id = id;
        ant.currentNode = currentNode;
        ant.tourLength = tourLength;
        ant.tour = new ArrayList<>(tour);
        ant.nodesToVisit = new ArrayList<>(nodesToVisit);
        ant.antInitialization = antInitialization;
        ant.path = path.clone();

        return ant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getNodesToVisit() {
        return nodesToVisit;
    }


    public List<Integer> getMaterialNodesToVisit() {
        return materialNodesToVisit;
    }

    public List<Integer> getCutNodesToVisit() {
        return cutNodesToVisit;
    }

    public double getTourLength() {
        return tourLength;
    }

    public void setTourLength(double tourLength) {
        this.tourLength = tourLength;
    }

    public void setNodesToVisit(List<Integer> nodesToVisit) {
        this.nodesToVisit = nodesToVisit;
    }

    public AbstractAntInitialization getAntInitialization() {
        return antInitialization;
    }

    public void setAntInitialization(AbstractAntInitialization antInitialization) {
        this.antInitialization = antInitialization;
    }

    public ACO getAco() {
        return aco;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "Ant_" + id + " " + tourLength + " " + tour;
    }

    public List<Integer> getAvailableCutNodesToVisit() {
        List<Integer> availableList = new ArrayList<>();
        for (Integer i : cutNodesToVisit) {
            if (pipes[i] <= surplus) {
                availableList.add(i);
            } else {
                break;
            }
        }
        return availableList;
    }

    public List<Integer> getAvailableMaterialNodesToVisit() {
        List<Integer> availableList = new ArrayList<>();
        for (Integer i : materialNodesToVisit) {
            if (pipes[i] >= pipes[cutNodesToVisit.get(0)]) {
                availableList.add(i);
            }
        }
        return availableList;
    }
}

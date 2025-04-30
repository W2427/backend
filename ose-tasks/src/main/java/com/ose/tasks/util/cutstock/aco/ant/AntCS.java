package com.ose.tasks.util.cutstock.aco.ant;

import com.ose.tasks.util.cutstock.aco.ACO;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AbstractAntInitialization;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents an ant and its process of building a solution
 * for a given addressed problem.
 *
 * @author feng tj
 * @version 1.0.0
 */
public class AntCS extends Ant implements Runnable {

    /**
     * Identifier
     */
    protected int id;

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
     * The tour length traveled
     */
    protected double tourLength;

    /**
     * The ant initialization
     */
    protected AbstractAntInitialization antInitialization;

    /**
     * Constructor
     *
     * @param aco The ant colony optimization
     * @param id  The ant's id
     */

    public AntCS(ACO aco, int id) {
        super(aco, id);

        checkNotNull(aco, "The aco should not be null");
        checkArgument(id >= 0, "The id should be > 0. Passed: %s", id);

        this.aco = aco;
        this.id = id;
        this.nodesToVisit = new ArrayList<>();
        this.tour = new ArrayList<>();
        this.path = new int[aco.getProblem().getNumberOfNodes()][aco.getProblem().getNumberOfNodes()];
    }

    /**
     * Restart all information before starting the search process
     */
    @Override
    public void reset() {
        this.currentNode = antInitialization.getPosition(id);
        this.tourLength = 0.0;
        this.tour.clear();
        this.nodesToVisit = aco.getProblem().initNodesToVisit(this.currentNode);
        this.tour.add(new Integer(currentNode));
        this.path = new int[aco.getProblem().getNumberOfNodes()][aco.getProblem().getNumberOfNodes()];
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
    @Override
    public boolean explore() {


        while (!nodesToVisit.isEmpty()) {


            int nextNode = aco.getAntExploration().getNextNode(this, currentNode);


            nodesToVisit.remove(new Integer(nextNode));


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
    public AntCS clone() {
        AntCS ant = new AntCS(aco, id);

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

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "Ant_" + id + " " + tourLength + " " + tour;
    }
}

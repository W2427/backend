package com.ose.tasks.util.cutstock.aco;

import com.ose.tasks.util.cutstock.aco.ant.Ant;
import com.ose.tasks.util.cutstock.aco.ant.exploration.AbstractAntExploration;
import com.ose.tasks.util.cutstock.aco.ant.initialization.AbstractAntInitialization;
import com.ose.tasks.util.cutstock.aco.daemonactions.AbstractDaemonActions;
import com.ose.tasks.util.cutstock.aco.graph.AntGraph;
import com.ose.tasks.util.cutstock.aco.graph.initialization.AbstractGraphInitialization;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.deposit.AbstractDeposit;
import com.ose.tasks.util.cutstock.aco.rule.globalupdate.evaporation.AbstractEvaporation;
import com.ose.tasks.util.cutstock.aco.rule.localupdate.AbstractLocalUpdateRule;
import com.ose.tasks.util.cutstock.problem.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This is the base class. This one has the main components
 * of all ACO's implementations. So, All ACO's implementations should be extended from this class.
 * <p>
 * In this framework, all ants build their solutions by using java's threads
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public abstract class ACO implements Observer {

    /**
     * Importance of the pheromones values
     */
    protected double alpha;

    /**
     * Importance of the heuristic information
     */
    protected double beta;

    /**
     * The number of ants
     */
    protected int numberOfAnts;

    /**
     * The number of iterations
     */
    protected int numberOfIterations;

    /**
     * Ants
     **/
    protected Ant[] ants;

    /**
     * The graph
     */
    protected AntGraph graph;

    /**
     * The current iteration
     */
    protected int it = 0;

    /**
     * Total of ants that finished your tour
     */
    protected int finishedAnts = 0;

    /**
     * Best Ant in tour
     */
    protected Ant globalBest;

    /**
     * Best Current Ant in tour
     */
    protected Ant currentBest;

    /**
     * The addressed problem
     */
    protected Problem problem;

    /**
     * The graph initialization
     */
    protected AbstractGraphInitialization graphInitialization;

    /**
     * The ant initialization
     */
    protected AbstractAntInitialization antInitialization;

    /**
     * The ant exploration
     */
    protected AbstractAntExploration antExploration;

    /**
     * The ant local update rule
     */
    protected AbstractLocalUpdateRule antLocalUpdate;

    /**
     * The daemon actions
     */
    protected List<AbstractDaemonActions> daemonActions = new ArrayList<>();

    /**
     * The pheromone evaporation's rules
     */
    protected List<AbstractEvaporation> evaporations = new ArrayList<>();

    /**
     * The pheromone deposit's rules
     */
    protected List<AbstractDeposit> deposits = new ArrayList<>();

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ACO.class);


    /**
     * The evaporation rate
     */
    protected double rho;

    /**
     * 是否有匹配组合
     */
    protected boolean findFlag = false;

    /**
     * Constructor
     *
     * @param problem The addressed problem
     */
    public ACO(Problem problem) {

        checkNotNull(problem, "The problem cannot be null");

        this.problem = problem;
        this.graph = new AntGraph(problem);
    }

    /**
     * Solve the addressed problem
     *
     * @return the best solution found by the ants
     */
    public int[] solve() {

        logger.info("Starting ACO");

        build();

        printParameters();

        initializePheromones();
        initializeAnts();

        while (!terminationCondition()) {
            constructAntsSolutions();
            if (findFlag) {
                updatePheromones();
                daemonActions();
            }

        }

        logger.info("Done");

        if (globalBest != null)
            return globalBest.getSolution();
        else
            return null;
    }

    /**
     * Initialize the pheromone values. This method creates a
     * graph and initialize it.
     */
    protected void initializePheromones() {



        this.graph.initialize(graphInitialization);
    }

    /**
     * Initialize the ants. This method creates an array of ants
     * and positions them in one of the graph's vertex
     */
    protected void initializeAnts() {



        this.ants = new Ant[numberOfAnts];

        for (int k = 0; k < numberOfAnts; k++) {
            ants[k] = new Ant(this, k);
            ants[k].setAntInitialization(getAntInitialization());
            ants[k].addObserver(this);
        }
    }

    /**
     * Verify if the search has finished. To reach this, the number of
     * iterations is verified.
     *
     * @return true if the search has finished. Otherwise, false
     */
    protected boolean terminationCondition() {
        return ++it > numberOfIterations;
    }

    /**
     * Update the pheromone values in the graph
     */
    protected void updatePheromones() {



        for (int i = 0; i < problem.getNumberOfNodes(); i++) {

            for (int j = i; j < problem.getNumberOfNodes(); j++) {

                if (i != j) {

                    for (AbstractEvaporation evaporation : evaporations) {
                        graph.setTau(i, j, evaporation.getTheNewValue(i, j));
                        graph.setTau(j, i, graph.getTau(i, j));
                    }

                    for (AbstractDeposit deposit : deposits) {
                        graph.setTau(i, j, deposit.getTheNewValue(i, j));
                        graph.setTau(j, i, graph.getTau(i, j));
                    }
                }
            }
        }
    }

    /**
     * Construct the ant's solutions
     */
    private synchronized void constructAntsSolutions() {





        currentBest = null;
        findFlag = false;


        for (int k = 0; k < numberOfAnts; k++) {
            Thread t = new Thread(ants[k], "Ant " + ants[k].getId());
            t.start();
        }


        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Perform the daemon actions
     */
    public void daemonActions() {

        if (daemonActions.isEmpty()) {

        } else {

        }

        for (AbstractDaemonActions daemonAction : daemonActions) {
            daemonAction.doAction();
        }
    }

    /**
     * When an ant has finished its search process, this method is called to
     * update the current and global best solutions.
     */
    @Override
    public synchronized void update(Observable obj, Object arg) {

        Ant ant = (Ant) obj;


        if (ant.isTourResult()) {

            findFlag = true;


            ant.setTourLength(problem.evaluate(ant.getSolution()));


            if (currentBest == null || problem.better(ant.getTourLength(), currentBest.getTourLength())) {
                currentBest = ant.clone();
            }


            if (globalBest == null || problem.better(ant.getTourLength(), globalBest.getTourLength())) {
                globalBest = ant.clone();
            }
        }



        if (++finishedAnts == numberOfAnts) {

            finishedAnts = 0;
            if (!findFlag) {
                logger.info("There is no enough material");
            }




            notify();
        }
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

    public AntGraph getGraph() {
        return graph;
    }

    public void setGraph(AntGraph graph) {
        this.graph = graph;
    }

    public int getNumberOfAnts() {
        return numberOfAnts;
    }

    public void setNumberOfAnts(int numberOfAnts) {
        this.numberOfAnts = numberOfAnts;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Ant[] getAnts() {
        return ants;
    }

    public void setAnts(Ant[] ants) {
        this.ants = ants;
    }

    public Ant getGlobalBest() {
        return globalBest;
    }

    public void setGlobalBest(Ant globalBest) {
        this.globalBest = globalBest;
    }

    public Ant getCurrentBest() {
        return currentBest;
    }

    public void setCurrentBest(Ant currentBest) {
        this.currentBest = currentBest;
    }

    public AbstractAntInitialization getAntInitialization() {
        return antInitialization;
    }

    public void setAntInitialization(AbstractAntInitialization antInitialization) {
        this.antInitialization = antInitialization;
    }

    public void setGraphInitialization(AbstractGraphInitialization graphInitialization) {
        this.graphInitialization = graphInitialization;
    }

    public AbstractGraphInitialization getGraphInitialization() {
        return graphInitialization;
    }

    public AbstractAntExploration getAntExploration() {
        return antExploration;
    }

    public void setAntExploration(AbstractAntExploration antExploration) {
        this.antExploration = antExploration;
    }

    public AbstractLocalUpdateRule getAntLocalUpdate() {
        return antLocalUpdate;
    }

    public void setAntLocalUpdate(AbstractLocalUpdateRule antLocalUpdate) {
        this.antLocalUpdate = antLocalUpdate;
    }

    public List<AbstractDeposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<AbstractDeposit> deposits) {
        this.deposits = deposits;
    }

    public List<AbstractEvaporation> getEvaporations() {
        return evaporations;
    }

    public void setEvaporations(List<AbstractEvaporation> evaporations) {
        this.evaporations = evaporations;
    }

    public List<AbstractDaemonActions> getDaemonActions() {
        return daemonActions;
    }

    public void setDaemonActions(List<AbstractDaemonActions> daemonActions) {
        this.daemonActions = daemonActions;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    /**
     * Print the parameters
     */
    protected void printParameters() {
        logger.info("=================== Parameters ===================");
        logger.info("Derivation: " + this.toString());
        logger.info("Problem: " + this.problem);
        logger.info("Number of Ants: " + this.numberOfAnts);
        logger.info("Number of Iterations: " + this.numberOfIterations);
        logger.info("Alpha: " + this.alpha);
        logger.info("Beta: " + this.beta);
        logger.info("Graph Initialization: " + this.graphInitialization);
        logger.info("Ant Initialization: " + this.antInitialization);
        logger.info("Ant Exploration: " + this.antExploration);
        logger.info("Ant Local Update Rule: " + this.antLocalUpdate);
        logger.info("Evaporations: " + this.evaporations);
        logger.info("Deposits: " + this.deposits);
        logger.info("Daemon Actions: " + this.daemonActions);
        logger.info("==================================================");
    }

    /**
     * Build an ant's implementation
     */
    public abstract void build();

    /**
     * Returns a string representation of the object.
     */
    public abstract String toString();
}

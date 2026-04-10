package main;

/**
 * Controller for the Dining Philosophers simulation.
 *
 * <p>This class is responsible for creating and running the simulation.
 * It creates all required {@link Chopstick} and {@link Philosopher} Objects,
 * starts the philosophers, lets them execute for the desired time, then shuts them down safely.
 * </p>
 *
 * <p>This simulation demonstrates a solution to the classic Dining Philosophers concurrency problem.
 * Here, multiple threads compete for shared resources, which can lead to issues such as deadlock and starvation.
 * </p>
 *
 * @author Sean-Paul Brown
 */
public class MainTable {
    
    /**
     * The default number of philosophers to use
     */
    private static final int NO_OF_PHILOSOPHER = 5;
    /**
     * Default duration of the simulation in milliseconds.
     * <p>Calculated as 1000 ms (as 1 second) * 10 for 10 total seconds</p>
     */
    private static final int SIMULATION_MILLIS = 1000 * 10;
    
    /**
     * Main method and entry point for simulation.
     *
     * <p>Creates a {@link MainTable} instance with default configuration and starts simulation</p>
     *
     * @param args <b>UNUSED</b>
     */
    public static void main(String[] args) {
        MainTable mainTable = new MainTable();
        mainTable.start();
    }
    
    /**
     * The number of philosophers and chopsticks in the simulation
     */
    private final int numPhil;
    /**
     * Duration of the simulation in milliseconds
     */
    private final int timeToRunMS;
    
    /**
     * Constructor with the default values
     */
    MainTable() {
        this(NO_OF_PHILOSOPHER, SIMULATION_MILLIS);
    }
    
    /**
     * Constructor with custom values
     *
     * @param numberOfPhilosophers the number of philosophers to simulate
     * @param timeToRunMS the duration of the simulation in milliseconds
     */
    MainTable(int numberOfPhilosophers, int timeToRunMS) {
        this.numPhil = numberOfPhilosophers;
        this.timeToRunMS = timeToRunMS;
    }
    
    /**
     * Starts and manages the simulation of Dining Philosophers
     *
     * <p>
     *     <ol>
     *         <li>Initializes an array of {@link Chopstick} Objects.</li>
     *         <li>Initializes an array of {@link Philosopher} Objects, assigning each a left and right chopstick</li>
     *         <li>Starts all philosopher threads</li>
     *         <li>Waits for the desired duration</li>
     *         <li>Signals all philosophers to stop</li>
     *         <li>Waits for all philosophers to stop</li>
     *         <li>Prints a summary of the simulation</li>
     *     </ol>
     * </p>
     *
     * <p>
     *     Chopsticks are assigned in a way that the last philosopher's right chopstick
     *     is the same chopstick as the first philosopher's left.
     *     This mirrors the classic problem setup.
     * </p>
     */
    public void start() {
        // make the chopsticks array and initialize it
        final Chopstick[] chopsticks = new Chopstick[numPhil];
        for (int i = 0; i < numPhil; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        // make the philosophers and initialize it
        final Philosopher[] philosophers = new Philosopher[numPhil];
        for (int i = 0; i < numPhil; i++) {
            // for each philosopher give them the chopstick of i and the next one
            // it loops so that the last philosopher will be the first chopstick on their 'right'
            philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % numPhil]);
        }
        
        // start the simulation
        for (Philosopher ph : philosophers) {
            ph.start();
        }
        
        // wait for the desired time for the simulation to run
        try {
            Thread.sleep(timeToRunMS);
        } catch (InterruptedException e) {
            System.err.println("Main interrupted");
        }
        
        // tell all the philosophers to stop
        for (Philosopher ph : philosophers) {
            ph.stop();
        }
        
        // now we can join each stopping thread, waiting for them to finish
        for (Philosopher ph : philosophers) {
            ph.join();
        }
        
        // print off some summary stats
        for (Philosopher philosopher : philosophers) {
            System.out.println(philosopher + " => No of Turns to Eat = " + philosopher.getTurnsWhileEating());
        }
    }
}


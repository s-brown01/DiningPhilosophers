package main;

public class MainTable {
    
    /**
     * How many philosophers to use
     */
    private static final int NO_OF_PHILOSOPHER = 5;
    /**
     * How long the simulation will run in milliseconds. 1000 ms per 1 second * 10 for 10 total seconds
     */
    private static final int SIMULATION_MILLIS = 1000 * 10;
    
    /**
     * The main method to run everything
     * @param args
     */
    public static void main(String[] args) {
        MainTable mainTable = new MainTable();
        mainTable.start();
    }
    
    private final int numPhil;
    private final int timeToRunMS;
    
    MainTable() {
        this(NO_OF_PHILOSOPHER, SIMULATION_MILLIS);
    }
    
    MainTable(int numberOfPhilosophers, int timeToRunMS) {
        this.numPhil = numberOfPhilosophers;
        this.timeToRunMS = timeToRunMS;
    }
    
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


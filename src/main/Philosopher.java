package main;

import java.util.Random;

/**
 * Represents a single Dining Philosopher.
 *
 * <p>
 *     Runs as an independent thread and alternates between thinking and eating.
 *     To eat, a philosopher must pickup both their left and right {@link Chopstick}.
 *     Philosophers will try to grab their left chopstick first.
 *     If that is successful, they will grab their right chopstick.
 *     If they possess both, then they will eat.
 *     After eating, the philosopher will release (put down) both chopsticks.
 * </p>
 *
 *  <p>This class demonstrates thread synchronization and shared resource
 *  management, as multiple philosophers compete for limited chopsticks.
 *  </p>
 *
 *  <p>The philosopher continues executing until it is signaled to stop.</p>
 *
 * @author Sean-Paul Brown
 */
public class Philosopher implements Runnable {
    
    /**
     * Unique identifier for the philosopher.
     */
    private final int id;
    /**
     * The chopstick to the philosopher's left.
     */
    private final Chopstick leftChopstick;
    /**
     * The chopstick to the philosopher's right.
     */
    private final Chopstick rightChopstick;
    /**
     * Controls whether the philosopher should continue running.
     * Marked {@code volatile} to ensure visibility across threads.
     */
    private volatile boolean isRunning;
    /**
     * Random number generator used to simulate variable thinking and eating times.
     */
    private final Random random = new Random();
    /**
     * Count for the amount of times the philosopher has successfully eaten.
     */
    private int turnsWhileEating = 0;
    /**
     * The thread in which this philosopher executes.
     */
    private final Thread thread;
    
    /**
     * Constructs a philosopher with a unique ID and assigned chopsticks.
     *
     * @param id the philosopher's unique identifier
     * @param leftChopStick the chopstick on the left side
     * @param rightChopStick the chopstick on the right side
     */
    public Philosopher(int id, Chopstick leftChopStick, Chopstick rightChopStick) {
        this.id = id;
        this.leftChopstick = leftChopStick;
        this.rightChopstick = rightChopStick;
        
        this.isRunning = false;
        this.thread = new Thread(this);
    }
    
    /**
     * Starts the philosopher's execution in a new thread.
     *
     * <p>Sets the running flag to {@code true} and begins the execution
     * of the {@link #run()} method.</p>
     */
    public void start() {
        isRunning = true;
        this.thread.start();
    }
    
    /**
     * Main execution loop for the philosopher.
     *
     * <p>The philosopher repeatedly:
     * <ol>
     *     <li>Thinks for a random period of time</li>
     *     <li>Attempts to pick up the left chopstick</li>
     *     <li>If successful, attempts to pick up the right chopstick</li>
     *     <li>If both chopsticks are acquired, eats</li>
     *     <li>Releases chopsticks after use</li>
     * </ol>
     * </p>
     *
     * <p>This process continues until the {@code isRunning} flag is set to {@code false}.</p>
     */
    @Override
    public void run() {
        while(isRunning) {
            think();
            // if this philosopher can pick up the left chopstick
            if (leftChopstick.pickUp(this, "left")) {
                // see if they can pick up the right chopstick
                if (rightChopstick.pickUp(this, "right")) {
                    // only if they have left and right can they eat
                    eat();
                    // put down the right chopstick first
                    rightChopstick.putDown(this, "right");
                }
                // put down the left chopstick whether we ate or not
                leftChopstick.putDown(this, "left");
            }
        }
    }
    
    /**
     * Signals the philosopher to stop execution.
     *
     * <p>The philosopher will finish its current iteration and then exit
     * the execution loop.</p>
     */
    public void stop() {
        isRunning = false;
    }
    
    /**
     * Waits for the philosopher's thread to terminate.
     *
     * <p>This method blocks until the thread finishes execution.</p>
     */
    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(this + " interrupted while joining");
        }
    }
    
    /**
     * Simulates the thinking phase.
     *
     * <p>The philosopher pauses for a random duration to represent thinking.</p>
     */
    private void think() {
        System.out.println(this + " is thinking");
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            System.err.println("Philosopher #" + id + " interrupted while thinking");
        }
    }
    
    /**
     * Simulates the eating phase.
     *
     * <p>The philosopher increments their eating counter and pauses
     * for a random duration to represent eating.</p>
     */
    private void eat() {
        System.out.println(this + " is eating");
        turnsWhileEating++;
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            System.err.println("Philosopher #" + id + " interrupted while eating");
        }
    }
    
    /**
     * Returns the number of times the philosopher has eaten.
     *
     * @return the number of completed eating cycles
     */
    public int getTurnsWhileEating() {
        return turnsWhileEating;
    }
    
    /**
     * Returns a string representation of the philosopher.
     *
     * @return a string in the format {@code Philosopher-{id}}
     */
    @Override
    public String toString() {
        return "Philosopher-" + id;
    }
}
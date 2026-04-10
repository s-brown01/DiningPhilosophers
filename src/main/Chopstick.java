package main;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a single shared chopstick in the Dining Philosophers.
 *
 * <p>
 *     Each chopstick is modeled as a lock to ensure mutual exclusion.
 *     Only one philosopher can hold this at a time.
 *     Philosophers attempt to acquire this using a timed lock attempt with minimal blocking.
 * </p>
 *
 *  <p>This class demonstrates resource contention and synchronization using
 *  {@link ReentrantLock}.</p>
 *
 * @author Sean-Paul Brown
 */
public class Chopstick {
    /**
     * A constant to show the time a philosopher should wait in milliseconds
     */
    private static final int TIME_TO_WAIT_MS = 10;
    /**
     * Lock used to represent ownership of the chopstick.
     */
    private final Lock lock = new ReentrantLock();
    /**
     * Unique identifier for the chopstick.
     */
    private final int id;
    
    /**
     * Constructs a chopstick with a given identifier.
     *
     * @param id the unique ID of the chopstick
     */
    public Chopstick(int id) {
        this.id = id;
    }
    
    /**
     * Attempts to pick up the chopstick.
     *
     * <p>The philosopher tries to acquire the lock using {@code tryLock} and
     * waits up to 10 milliseconds before giving up.</p>
     *
     * @param who the philosopher attempting to pick up the chopstick
     * @param side a label indicating whether this is the left or right chopstick
     * @return {@code true} if the chopstick was successfully acquired,
     *         {@code false} otherwise
     */
    public boolean pickUp(Philosopher who, String side) {
        // try to pick up the given chopstick
        try {
            // tryLock waits for a certain amount of time
            // here the philosopher will wait 10 milliseconds to get the chopstick
            if (lock.tryLock(TIME_TO_WAIT_MS, TimeUnit.MILLISECONDS)) {
                // return true that the chopstick was picked up
                System.out.println(who + " picked up " + side + " " + this);
                return true;
            }
        } catch (InterruptedException e) {
            System.err.println("Philosopher " + who + " interrupted while picking up their " + side + " chopstick: " + this);
        }
        // this will always be reached if the chopstick was not picked up, so return false
        return false;
    }
    
    /**
     * Releases the chopstick.
     *
     * <p>The lock is released and becomes available for other philosophers.
     * If a philosopher attempts to release a lock they do not hold, an error
     * is printed.</p>
     *
     * @param who the philosopher putting down the chopstick
     * @param side a label indicating whether this is the left or right chopstick
     */
    public void putDown(Philosopher who, String side) {
        try {
            lock.unlock();
            System.out.println(who + " put down " + side + " " + this);
        } catch (IllegalMonitorStateException  e) {
            System.err.println("A philosopher (" + who + ") tried to put down someone else's chopstick: " + this);
        }
    }
    
    /**
     * Returns a string representation of the chopstick.
     *
     * @return a string in the format {@code Chopstick-{id}}
     */
    @Override
    public String toString() {
        return "Chopstick-" + id;
    }
}

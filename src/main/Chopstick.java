package main;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Chopstick {
    private final Lock lock = new ReentrantLock();
    private final int id;
    
    public Chopstick(int id) {
        this.id = id;
    }
    
    public boolean pickUp(Philosopher who, String where) {
        // try to pick up the given chopstick
        try {
            // tryLock waits for a certain amount of time
            // here the philosopher will wait 10 milliseconds to get the chopstick
            if (lock.tryLock(10, TimeUnit.MILLISECONDS)) {
                // return true that the chopstick was picked up
                System.out.println(who + " picked up " + where + " " + this);
                return true;
            }
        } catch (InterruptedException e) {
            System.err.println("Philosopher " + who + " interrupted while picking up their " + where + " chopstick: " + this);
        }
        // this will always be reached if the chopstick was not picked up, so return false
        return false;
    }
    
    public void putDown(Philosopher who, String name) {
        try {
            lock.unlock();
            System.out.println(who + " put down " + name + " " + this);
        } catch (IllegalMonitorStateException  e) {
            System.err.println("A philosopher (" + who + ") tried to put down someone else's chopstick: " + this);
        }
    }
    
    @Override
    public String toString() {
        return "Chopstick-" + id;
    }
}

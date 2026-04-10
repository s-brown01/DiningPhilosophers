package main;

import java.util.Random;

public class Philosopher implements Runnable {
    
    private final int id;
    private final Chopstick leftChopstick;
    private final Chopstick rightChopstick;
    private volatile boolean isRunning;
    private final Random randomGenerator = new Random();
    private int turnsWhileEating = 0;
    
    private final Thread thread;
    
    /**
     * **
     *
     * @param id Philosopher number
     *
     * @param leftChopStick
     * @param rightChopStick
     */
    public Philosopher(int id, Chopstick leftChopStick, Chopstick rightChopStick) {
        this.id = id;
        this.leftChopstick = leftChopStick;
        this.rightChopstick = rightChopStick;
        
        this.isRunning = false;
        this.thread = new Thread(this);
    }
    
    public void start() {
        isRunning = true;
        this.thread.start();
    }
    
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
    
    public void stop() {
        isRunning = false;
    }
    
    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(this + " interrupted while joining");
        }
    }
    
    private void think() {
        System.out.println(this + " is thinking");
        try {
            Thread.sleep(randomGenerator.nextInt(1000));
        } catch (InterruptedException e) {
            System.err.println("Philosopher #" + id + " interrupted while thinking");
        }
    }
    
    private void eat() {
        System.out.println(this + " is eating");
        turnsWhileEating++;
        try {
            Thread.sleep(randomGenerator.nextInt(1000));
        } catch (InterruptedException e) {
            System.err.println("Philosopher #" + id + " interrupted while eating");
        }
    }
    
    public int getTurnsWhileEating() {
        return turnsWhileEating;
    }
    
    
    @Override
    public String toString() {
        return "Philosopher-" + id;
    }
}
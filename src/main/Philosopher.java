package main;

public class Philosopher implements Runnable {
    
    public static void main(String[] args) {
        Philosopher ph = new Philosopher();
        ph.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignored
        }
        ph.stop();
    }
    
    private volatile boolean isRunning;
    private volatile boolean isHungry;
    private Thread thread;
    
    
    Philosopher() {
        
        this.isRunning = true;
        this.isHungry = false;
        this.thread = new Thread(this);
    }
    
    public void start() {
        System.out.println("Philosopher started");
        
        isRunning = true;
        isHungry = true;
        thread.start();
    }
    
    public void stop() {
        System.out.println("Philosopher stopped");
        
        isRunning = false;
    }
    
    @Override
    public void run() {
        while (isRunning) {
            if (isHungry) {
                System.out.println("Philosopher is hungry");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
//            throw new RuntimeException(e);
            }
        }
    }
}

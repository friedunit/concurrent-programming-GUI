/*
 * Filename: Car.java
 * Author: John Kaiser
 * Date: 2/28/2020
 * Purpose: Runnable Car class to increment xPosition and display speed.
 */
package cmsc335project3;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;


public class Car implements Runnable {
    private int xPosition;
    private int yPosition = 0;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    //Make seperate booleans for atLight and suspended (pause button)
    public final AtomicBoolean atLight = new AtomicBoolean(false);
    public final AtomicBoolean suspended = new AtomicBoolean(false);
    private String threadName = "";
    public Thread thread;
    private int speed = 0;
    
    //Constructor for name, max and min: Max and min for range of initial xPosition for car
    public Car(String name, int max, int min) {
        this.threadName = name;
        this.xPosition = ThreadLocalRandom.current().nextInt(min, max);
        System.out.println("Creating " + threadName);
    }

    public synchronized int getPosition() {
        return xPosition;
    }
    
    public int getSpeed() {
        if(isRunning.get()) {
            if(atLight.get()) 
                speed = 0;
            else 
                //Incrementing 5 meters every 1/10th of a second.
                //Thats 50 meters per second, 3000 meters per minute
                //3 km per minute * 60 for 180 kph
                speed = 3*60;
        } else 
            speed = 0;
        return speed;
    }
    
    public void start() {
        System.out.println("Starting " + threadName);
        if(thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
    
    public void stop() {
        thread.interrupt();
        isRunning.set(false);
        System.out.println("Stopping " + threadName);
    }
    
    public void suspend() {
        suspended.set(true);
        System.out.println("Suspending " + threadName);
    }
    
    public synchronized void resume() {
        //If car is suspended, set suspended to false and notify
        if(suspended.get() || atLight.get()) {
            suspended.set(false);
            atLight.set(false);
            notify();
            System.out.println("Resuming " + threadName);
        }
    }
    
    @Override
    public void run() {
        System.out.println("Running " + threadName);
        isRunning.set(true);
        while(isRunning.get()) {
            try {
                
                while(xPosition < 3000) {
                    synchronized(this) {
                        while(suspended.get() || atLight.get()) {
                            System.out.println(threadName + " waiting");
                            wait(); 
                        }
                    }
                    //Check if still running
                    if(isRunning.get()) {
                    Thread.sleep(100);
                    xPosition+=5;
                    }
                }
                xPosition = 0;     
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
    
}

/*
 * Filename: CMSC335Project3.java
 * Author: John Kaiser
 * Date: 2/28/2020
 * Purpose: As a new engineer for a traffic congestion mitigation company, you have been tasked with developing a
Java Swing GUI that displays time, traffic signals and other information for traffic analysts. The final GUI
design is up to you but should include viewing ports/panels to display the following components of the
simulation:
1. Current time stamps in 1 second intervals
2. Real-time Traffic light display for three major intersections
3. X, Y positions and speed of up to 3 cars as they traverse each of the 3 intersections
Some of the details of the simulation are up to you but the following guidelines will set the guardrails:
1. The components listed above should run in separate threads.
2. Loop through the simulation with button(s) providing the ability to start, pause, stop and
continue the simulation.
3. You will need to use basic distance formulas such as distance = Speed * time. Be sure to be
consistent and define your units of measure (e.g. mile/hour, versus km/hour)
4. Assume a straight distance between each traffic light of 1000 meters.
5. Since you are traveling a straight line, you can assume Y = 0 for your X,Y positions.
6. Provide the ability to add more cars and intersections to the simulation through the GUI.
7. Don't worry about physics. Assume cars will stop on a dime for red lights, and continue through
yellow lights and green lights.
8. Document all assumptions and limitations of your simulation.
 */
package cmsc335project3;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CMSC335Project3 extends JFrame implements Runnable, ChangeListener {
    
    static JLabel timeText = new JLabel();
    static JLabel trafficAtext = new JLabel();
    static JLabel trafficBtext = new JLabel();
    static JLabel trafficCtext = new JLabel();
    //JButtons to start, pause, and stop
    private JButton start = new JButton("Start");
    private JButton pause = new JButton("Pause");
    private JButton stop = new JButton("Stop");
    //JSliders for showing car progress
    static JSlider car1Slider = new JSlider(0, 3000);
    static JSlider car2Slider = new JSlider(0, 3000);
    static JSlider car3Slider = new JSlider(0, 3000);
    static JSlider car4Slider = new JSlider(0, 3000);
    
    private static boolean isRunning;
    private static final AtomicBoolean simIsRunning = new AtomicBoolean(false);
    
    //Create 3 runnable intersection objects, each on their own thread
    Intersection A = new Intersection("aThread", trafficAtext);
    Intersection B = new Intersection("bThread", trafficBtext);
    Intersection C = new Intersection("cThread", trafficCtext);
    //Create 4 runnable Car objects and a thread for each one
    Car car1 = new Car("Car1Thread", 300, 0);
    Car car2 = new Car("Car2Thread", 1000, 0);
    Car car3 = new Car("Car3Thread", 2000, 1000);
    Car car4 = new Car("Car4Thread", 2000, 1000);
    //Array of cars to loop through later
    Car[] carArray = {car1, car2, car3, car4};
    Intersection[] intersectionArray = {A, B, C};
    static Thread gui;
    
    Object[][] trafficData = {
        {"Car 1", car1.getPosition(), 0, 0},
        {"Car 2", car2.getPosition(), 0, 0},
        {"Car 3", car3.getPosition(), 0, 0},
        {"Car 4", car4.getPosition(), 0, 0}
    };
    //Table for displaying data
    String[] columnNames = {"Car", "X-Pos", "Y-Pos", "Speed km/h"};
    JTable dataTable = new JTable(trafficData, columnNames);
    
    
    public CMSC335Project3() {
        super("CMSC 335 Project 3: Traffic Tracker");
        isRunning = Thread.currentThread().isAlive();
        buildGUI();
        setButtons();
    }
    
    private void display() {
        setSize(600,400);
        setVisible(true);
        //Centers the frame on the screen
        setLocationRelativeTo(null);
        //Sets the window to be closeable
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void buildGUI() {
        
        JLabel welcome = new JLabel("Welcome to the Traffic Tracker Simulator!");
        JLabel welcome2 = new JLabel("Click the Start button to begin simulation");
        
        JLabel time = new JLabel("Current time: ");
        JLabel trafficLightA = new JLabel("Intersection A: ");
        JLabel trafficLightB = new JLabel("Intersection B: ");
        JLabel trafficLightC = new JLabel("Intersection C: ");
        
        //Add changeListeners to car sliders
        car1Slider.addChangeListener(this);
        car2Slider.addChangeListener(this);
        car3Slider.addChangeListener(this);
        car4Slider.addChangeListener(this);
        
        car1Slider.setValue(car1.getPosition());
        car2Slider.setValue(car2.getPosition());
        car3Slider.setValue(car3.getPosition());
        car4Slider.setValue(car4.getPosition());
        
        car1Slider.setMajorTickSpacing(1000);
        car1Slider.setPaintTicks(true);
        
        car2Slider.setMajorTickSpacing(1000);
        car2Slider.setPaintTicks(true);
    
        dataTable.setPreferredScrollableViewportSize(new Dimension(400, 70));
        dataTable.setFillsViewportHeight(true);
        
        JPanel dataPanel = new JPanel();  
        
        //Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(dataTable);
        dataPanel.add(scrollPane);
        
    
        //GUI Layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addContainerGap(30, 30) //Container gap on left side     
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)    
                .addComponent(welcome)
                .addComponent(welcome2)    
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(time)
                    .addComponent(timeText)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)    
                .addGroup(layout.createSequentialGroup()    
                    .addComponent(start)
                    .addComponent(pause)
                    .addComponent(stop)))       
                    .addComponent(car1Slider)
                    .addComponent(car2Slider) 
                    .addComponent(car3Slider)
                    .addComponent(car4Slider)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)   
                .addGroup(layout.createSequentialGroup()   
                    .addComponent(trafficLightA)
                    .addComponent(trafficAtext)
                        .addContainerGap(20, 20)
                    .addComponent(trafficLightB)
                    .addComponent(trafficBtext)
                        .addContainerGap(20, 20)
                    .addComponent(trafficLightC)
                    .addComponent(trafficCtext))
                    .addComponent(dataPanel)))
                        
            .addContainerGap(30, 30) //Container gap on right side
                
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createSequentialGroup()
                    .addComponent(welcome)
                    .addComponent(welcome2))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(time)
                    .addComponent(timeText))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(start)
                    .addComponent(pause)
                    .addComponent(stop))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)    
                    .addComponent(car1Slider)
                    .addComponent(car2Slider)
                    .addComponent(car3Slider)
                    .addComponent(car4Slider))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(trafficLightA)
                    .addComponent(trafficAtext)
                    .addComponent(trafficLightB)
                    .addComponent(trafficBtext)
                    .addComponent(trafficLightC)
                    .addComponent(trafficCtext))
                .addComponent(dataPanel)

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addGap(20, 20, 20))
                .addGap(20, 20, 20)    
        );
        
        pack();
    }
    
    private void setButtons() {
        //Start car and intersection threads with start button
        start.addActionListener((ActionEvent e) -> {
            if(!simIsRunning.get()) {
                System.out.println(Thread.currentThread().getName() + " calling start");
                A.start();
                B.start();
                C.start();
                car1.start();
                car2.start();
                car3.start();
                car4.start();
                
                gui.start();
                
            }
            //Set simIsRunning to true
            simIsRunning.set(true);   
        });
        
        pause.addActionListener((ActionEvent e) -> {
            if(simIsRunning.get()) {
                //Loop through cars and intersections to call suspend()
                for(Car i: carArray) {
                    i.suspend();
                    System.out.println(Thread.currentThread().getName() + " calling suspend");
                }
                for(Intersection i: intersectionArray) {
                    //Call interrupt for sleeping intersection threads
                    i.interrupt();
                    i.suspend();
                }
                
                pause.setText("Continue");
                simIsRunning.set(false);
            } else {
                for(Car i:carArray) {
                    if(i.suspended.get()) {
                        i.resume();
                        System.out.println(Thread.currentThread().getName() + " calling resume");
                    }
                }
                for(Intersection i: intersectionArray) {
                    i.resume();
                }
                pause.setText("Pause");
                simIsRunning.set(true);
            }
        });
        
        stop.addActionListener((ActionEvent e) -> {
            if(simIsRunning.get()) {
                System.out.println(Thread.currentThread().getName() + " calling stop");
                for(Car i: carArray) {
                    i.stop();
                }
                for(Intersection i: intersectionArray) {
                    i.stop();
                }
                simIsRunning.set(false);
            }
        });
    }
    
        @Override
    public void stateChanged(ChangeEvent e) {
        //When car sliders change, update data in table
        trafficData[0][1] = car1Slider.getValue();
        trafficData[1][1] = car2Slider.getValue();
        trafficData[2][1] = car3Slider.getValue();
        trafficData[3][1] = car4Slider.getValue();
        //Update speed
        trafficData[0][3] = car1.getSpeed() + " km/h";
        trafficData[1][3] = car2.getSpeed() + " km/h";
        trafficData[2][3] = car3.getSpeed() + " km/h";
        trafficData[3][3] = car4.getSpeed() + " km/h";
        //Update table
        dataTable.repaint();
    }
    
    private void getData() {
        if(simIsRunning.get()) {
        //Get colors for intersections, if Red check xPosition
        switch(A.getColor()) {
            case "Red":
                for(Car i: carArray) {
                    //If car xPosition is within 500 meters and light is red, set suspend to true for car to wait
                    if(i.getPosition()>500 && i.getPosition()<1000) {
                        i.atLight.set(true);
                    }
                }
                break;
            case "Green":
                for(Car i:carArray) {
                    if(i.atLight.get()) {
                        i.resume();
                    }
                }
                break;
        }
        
        switch(B.getColor()) {
            case "Red":
                for(Car i: carArray) {
                    //If car xPosition is within 500 meters and light is red, set suspend to true for car to wait
                    if(i.getPosition()>1500 && i.getPosition()<2000) {
                        i.atLight.set(true);
                    }
                }
                break;
            case "Green":
                for(Car i:carArray) {
                    if(i.atLight.get()) {
                        i.resume();
                    }
                }
                break;
        }
        
        switch(C.getColor()) {
            case "Red":
                for(Car i: carArray) {
                    //If car xPosition is within 500 meters and light is red, set suspend to true for car to wait
                    if(i.getPosition()>2500 && i.getPosition()<3000) {
                        i.atLight.set(true);
                    }
                }
                break;
            case "Green":
                for(Car i:carArray) {
                    if(i.atLight.get()) {
                        i.resume();
                    }
                }
                break;
        }
        }
        
    }
    
    @Override
    public void run() {
        while(isRunning) {
            //While running, if simulation is running, set car sliders to car xPosition and get data
            if(simIsRunning.get()) {
            car1Slider.setValue(car1.getPosition());
            car2Slider.setValue(car2.getPosition());
            car3Slider.setValue(car3.getPosition());
            car4Slider.setValue(car4.getPosition());

            getData();
            }
        }
    }
   
    public static void main(String[] args) {
        CMSC335Project3 test = new CMSC335Project3();
        test.display();
        gui = new Thread(test);
        
        Thread time = new Thread(new Time());
        time.start();
    }   
}

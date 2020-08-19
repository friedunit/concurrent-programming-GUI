/*
 * Filename: Time.java
 * Author: John Kaiser
 * Date: 2/28/2020
 * Purpose: Create runnable Time class to get current time and update timeText textfield in GUI
 */
package cmsc335project3;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time implements Runnable {
    private boolean isRunning;
    private String timePattern = "hh:mm:ss a";
    private SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern); 
    Date date = new Date(System.currentTimeMillis());

    public Time() {
        this.isRunning = Thread.currentThread().isAlive();
    }
    
    public String getTime() {
        date = new Date(System.currentTimeMillis());
        return timeFormat.format(date);
    }

    @Override
    public void run() {
        //While running, constantly update current time
        while (isRunning) {
            
            CMSC335Project3.timeText.setText(getTime());
        } 
    }
    
}

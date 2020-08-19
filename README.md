# Purpose: As a new engineer for a traffic congestion mitigation company, you have been tasked with developing a
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

![Screenshot1](https://raw.githubusercontent.com/friedunit/concurrent-programming-GUI/master/screenshots/gui1.png)

The current time updates every second and runs while the program is running. I added JSliders to represent the 4 cars moving along the road. The tick marks represent the traffic lights 1000 meters apart. The table displays the information and updates with the simulation to show each car’s x-position and speed. Clicking the start button begins the simulation.

![Screenshot2](https://raw.githubusercontent.com/friedunit/concurrent-programming-GUI/master/screenshots/gui2.png)
 
Each of the 3 intersections displays their current traffic light color. While green and yellow, the cars traverse through the lights, incrementing the x-position 5 meters every 1/10th of a second. This makes their speed 180 km/h which, of course, is extremely fast, but it is just for the simulation.
The intersections are set to stay green for 10 seconds, then yellow for 5 seconds and red for 5 seconds. The intersection threads sleep for that amount of time before cycling to the next color.

![Screenshot3](https://raw.githubusercontent.com/friedunit/concurrent-programming-GUI/master/screenshots/gui3.png)

Here the lights have turned yellow. Next, they turn red and any cars within 500 meters of the light will be suspended. The cars wait until the light turns green and notify is called for the cars to resume.

![Screenshot4](https://raw.githubusercontent.com/friedunit/concurrent-programming-GUI/master/screenshots/gui4.png)

In this screenshot, car 1 is stopped at the first light and speed is 0. The simulation continues to loop through with cars setting back to 0 upon reaching the end at 3000 meters.
Clicking the pause button suspends all car and intersection threads.

 

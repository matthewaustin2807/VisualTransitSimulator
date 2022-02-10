# VisualTransitSimulator

## Introduction
The Visual Transit Simulator (VTS) was a project that me and my team did in the Program Design and Development (CSCI3081W) course at the University of Minnesota. In a nutshell, the VTS simulates the different types of public transportation that circulates the University of Minnesota area. Although the app mainly runs on Java Script, the JS codes are already provided. Therefore, the visualization module for this program was not my creation. However, the entire simulator module logic through which the program  runs on are my own Java code. This includes designing the UML diagrams for the client and server side objects, adding functionalities to the software and also testing the program. 

## What could the VTS do?
The VTS itself is mainly a simulator that allows user to simulate how public transportation circulates around the University of Minnesota campus. As of course completion, it supports four types of vehicles: small buses, large buses, diesel trains and electric trains. The simulator operates over a certain time unit specified by the user. At each time unit, the simulator will update its state by creating passengers at stops, moving vehicles through routes, getting the vehicles to pick up passengers at each stop, calculate the CO2 consumption based on the type of vehicle and the number of passengers and also simulate maintenance issues that could effect the each individual routes. All information supplied to the simulator such as the number of vehicles, the number of stops and also stop locations are supplied through a configuration file. It is the program's responsibility to read through the file and create the simulation based on it.

## How to run the program
A `build.gradle` file is configured to automate the build process of the program. By running the command `./gradlew apprun`, it will launch the simulator module. Going to this link: http://localhost:7777/project/web_graphics/project.html in a browser will then start the visualization module. If these two modules are launched in unison, they would be able to communicate with each other to create a fully functional program.

## What I learned by doing this project
- Firstly, I gained experience creating my own UML diagrams that describes the classes that made up the program. These UML diagrams were very important to the development process since it provided a strong structure by which I could follow during the development of the program.
- Secondly, I learned how to properly refactor and make changes to an already existing program. This allowed me to fix and modify several functionalities that had existed in the program.
- Third, I managed to bring into practice several software design patterns such as the Random Factory pattern, the Strategy pattern, the Observer pattern and the Decorator design pattern. Other than those mentioned, I also learned about several other design patterns whilst trying to figure out which pattern would correctly apply to the specific functionality of the program.
- Fourth, I learned to document all of my codes that I wrote through extensive and definitely informative Javadoc comments
- Then, I gained valuable experience in program testing through the usage of JUnit tests, supported by the Mockito test double framework. These tests were also written with the goal of reaching more than 90% branch coverage as seen in the JaCoCo test report.
- Finally, and most probably the most important skill that I learned through this program is to work together in a team environment. We as a team, managed to work through each problem by splitting tasks among ourselves. We also avoided conflicts that usually might arise from a group project by having a someone taking charge of several tasks at a time.

--- 
This is by far one of the most extensive project that I have build and thanks to it, I managed to gain valuable skills and experience in many aspects of program design.

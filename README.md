# VisualTransitSimulator

## Introduction
The Visual Transit Simulator (VTS) was a project that I did in the Program Design and Development (CSCI3081W) course at the University of Minnesota. In a nutshell, the VTS simulates the different types of public transportation that circulates the University of Minnesota area. Although the app mainly runs on Java Script, the JS codes are already provided. Therefore, the visualization module for this program was not my creation. However, the entire simulator module logic through which the program  runs on are my own Java code. This includes designing the UML diagrams for the client and server side objects, adding functionalities to the software and also testing the program. 

##What could the VTS do?
The VTS itself is mainly a simulator that allows user to simulate how public transportation circulates around the University of Minnesota campus. As of course completion, it supports four types of vehicles: small buses, large buses, diesel trains and electric trains. The simulator operates over a certain time unit specified by the user. At each time unit, the simulator will update its state by creating passengers at stops, moving vehicles through routes, getting the vehicles to pick up passengers at each stop, calculate the CO2 consumption based on the type of vehicle and the number of passengers and also simulate maintenance issues that could effect the each individual routes. All information supplied to the simulator such as the number of vehicles, the number of stops and also stop locations are supplied through a configuration file. It is the program's responsibility to read through the file and create the simulation based on it.

##How to run the program
A build.gradle file is configured to automate the build process of the program. By running the command ./gradlew apprun, it will launch the simulator module. Going to this link: http://localhost:7777/project/web_graphics/project.html in a browser will then start the visualization module. If these two modules are launched in unison, they would be able to communicate with each other to create a fully functional program.

##What I learned by doing this project

package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

public class DieselTrainDecorator extends VehicleDecorator {
  private Vehicle vehicle;

  public DieselTrainDecorator(Vehicle vehicle) {
    super(vehicle);
    this.vehicle = vehicle;
  }

  @Override
  public int getRedValue() {
    return 255;
  }

  @Override
  public int getGreenValue() {
    return 204;
  }

  @Override
  public int getBlueValue() {
    return 51;
  }

  @Override
  public int getAlphaValue() {
    if (this.vehicle.getHasBeenInABrokenLine()) {
      return 155;
    } else {
      return 255;
    }
  }

  /**
   * Report statistics for the train.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Diesel Train Info Start####");
    out.println("ID: " + getId());
    out.println("Name: " + getName());
    out.println("Speed: " + getSpeed());
    out.println("Capacity: " + getCapacity());
    out.println("Position: " + (getPosition().getLatitude() + "," + getPosition().getLongitude()));
    out.println("Distance to next stop: " + getDistanceRemaining());
    out.println("****Passengers Info Start****");
    out.println("Num of passengers: " + getPassengers().size());
    for (Passenger pass : getPassengers()) {
      pass.report(out);
    }
    out.println("****Passengers Info End****");
    out.println("####Diesel Train Info End####");
  }

  /**
   * co2 consumption for an electric train.
   *
   * @return The current co2 consumption value
   */
  public int getCurrentCO2Emission() {
    return (2 * getPassengers().size()) + 6;
  }
}

package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

public class SmallBusDecorator extends VehicleDecorator {
  private Vehicle vehicle;

  public SmallBusDecorator(Vehicle vehicle) {
    super(vehicle);
    this.vehicle = vehicle;
  }

  @Override
  public int getRedValue() {
    return 122;
  }

  @Override
  public int getGreenValue() {
    return 0;
  }

  @Override
  public int getBlueValue() {
    return 25;
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
   * co2 consumption for a bus.
   *
   * @return The current co2 consumption value
   */
  @Override
  public int getCurrentCO2Emission() {
    return (getPassengers().size()) + 1;
  }

  /**
   * Report statistics for the bus.
   *
   * @param out stream for printing
   */
  @Override
  public void report(PrintStream out) {
    out.println("####Small Bus Info Start####");
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
    out.println("####Small Bus Info End####");
  }
}

package edu.umn.cs.csci3081w.project.model;

public abstract class VehicleDecorator extends Vehicle {

  /**
   * Constructor for a bus.
   *
   * @param vehicle bus object
   */
  public VehicleDecorator(Vehicle vehicle) {
    super(vehicle.getId(), vehicle.getLine(), vehicle.getCapacity(),
        vehicle.getSpeed(), vehicle.getPassengerLoader(), vehicle.getPassengerUnloader());
  }

  public abstract int getRedValue();

  public abstract int getGreenValue();

  public abstract int getBlueValue();

  public abstract int getAlphaValue();

}

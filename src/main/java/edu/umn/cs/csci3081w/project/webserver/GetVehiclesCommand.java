package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.DieselTrain;
import edu.umn.cs.csci3081w.project.model.DieselTrainDecorator;
import edu.umn.cs.csci3081w.project.model.ElectricTrain;
import edu.umn.cs.csci3081w.project.model.ElectricTrainDecorator;
import edu.umn.cs.csci3081w.project.model.LargeBus;
import edu.umn.cs.csci3081w.project.model.LargeBusDecorator;
import edu.umn.cs.csci3081w.project.model.SmallBus;
import edu.umn.cs.csci3081w.project.model.SmallBusDecorator;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import edu.umn.cs.csci3081w.project.model.VehicleDecorator;
import java.util.List;

public class GetVehiclesCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  public GetVehiclesCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Retrieves vehicles information from the simulation.
   *
   * @param session current simulation session
   * @param command the get vehicles command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    List<Vehicle> vehicles = simulator.getActiveVehicles();
    JsonObject data = new JsonObject();
    data.addProperty("command", "updateVehicles");
    JsonArray vehiclesArray = new JsonArray();
    VehicleDecorator vehicleDecorator;
    for (int i = 0; i < vehicles.size(); i++) {
      Vehicle currVehicle = vehicles.get(i);
      JsonObject s = new JsonObject();
      s.addProperty("id", currVehicle.getId());
      s.addProperty("numPassengers", currVehicle.getPassengers().size());
      s.addProperty("capacity", currVehicle.getCapacity());
      String vehicleType = "";
      if (currVehicle instanceof SmallBus) {
        vehicleType = SmallBus.SMALL_BUS_VEHICLE;
        vehicleDecorator = new SmallBusDecorator(vehicles.get(i));
      } else if (currVehicle instanceof LargeBus) {
        vehicleType = LargeBus.LARGE_BUS_VEHICLE;
        vehicleDecorator = new LargeBusDecorator(vehicles.get(i));
      } else if (currVehicle instanceof ElectricTrain) {
        vehicleType = ElectricTrain.ELECTRIC_TRAIN_VEHICLE;
        vehicleDecorator = new ElectricTrainDecorator(vehicles.get(i));
      } else if (currVehicle instanceof DieselTrain) {
        vehicleType = DieselTrain.DIESEL_TRAIN_VEHICLE;
        vehicleDecorator = new DieselTrainDecorator(vehicles.get(i));
      } else {
        vehicleDecorator = null;
      }
      s.addProperty("type", vehicleType);
      s.addProperty("co2", currVehicle.getCurrentCO2Emission());
      JsonObject positionJsonObject = new JsonObject();
      positionJsonObject.addProperty("longitude", currVehicle.getPosition().getLongitude());
      positionJsonObject.addProperty("latitude", currVehicle.getPosition().getLatitude());
      s.add("position", positionJsonObject);
      JsonObject colorJsonObject = new JsonObject();
      colorJsonObject.addProperty("r", vehicleDecorator.getRedValue());
      colorJsonObject.addProperty("g", vehicleDecorator.getGreenValue());
      colorJsonObject.addProperty("b", vehicleDecorator.getBlueValue());
      colorJsonObject.addProperty("alpha", vehicleDecorator.getAlphaValue());
      s.add("color", colorJsonObject);
      vehiclesArray.add(s);
    }
    data.add("vehicles", vehiclesArray);
    session.sendJson(data);
  }

}

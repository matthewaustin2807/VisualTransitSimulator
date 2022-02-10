package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.DieselTrain;
import edu.umn.cs.csci3081w.project.model.DieselTrainDecorator;
import edu.umn.cs.csci3081w.project.model.ElectricTrain;
import edu.umn.cs.csci3081w.project.model.ElectricTrainDecorator;
import edu.umn.cs.csci3081w.project.model.Issue;
import edu.umn.cs.csci3081w.project.model.LargeBus;
import edu.umn.cs.csci3081w.project.model.LargeBusDecorator;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.Passenger;
import edu.umn.cs.csci3081w.project.model.PassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.SmallBus;
import edu.umn.cs.csci3081w.project.model.SmallBusDecorator;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import edu.umn.cs.csci3081w.project.model.VehicleDecorator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GetVehiclesCommandTest {

  private Vehicle testVehicleSB;
  private Vehicle testVehicleLB;
  private Vehicle testVehicleET;
  private Vehicle testVehicleDT;
  private VehicleDecorator decoratorSB;
  private VehicleDecorator decoratorLB;
  private VehicleDecorator decoratorET;
  private VehicleDecorator decoratorDT;
  private Route testRouteIn;
  private Route testRouteOut;

  /**
   * Sets up the variable before each test.
   */
  @BeforeEach
  public void setUp() {
    List<Stop> stopsIn = new ArrayList<Stop>();
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.843774422231134);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);

    testRouteIn = new Route(0, "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.843774422231134);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.025);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);

    testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Passenger testPassenger1 = new Passenger(3, "testPassenger1");
    Passenger testPassenger2 = new Passenger(2, "testPassenger2");
    Passenger testPassenger3 = new Passenger(1, "testPassenger3");

    testVehicleSB = new SmallBus(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    testVehicleSB.loadPassenger(testPassenger1); //has two passengers, no issue
    testVehicleSB.loadPassenger(testPassenger2);
    decoratorSB = new SmallBusDecorator(testVehicleSB);

    testVehicleLB = new LargeBus(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    testVehicleLB.setHasBeenInABrokenLine(); //has no passengers, has issue
    decoratorLB = new LargeBusDecorator(testVehicleLB);

    testVehicleET = new ElectricTrain(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    testVehicleET.loadPassenger(testPassenger3); //has one passenger, has issue
    testVehicleET.setHasBeenInABrokenLine();
    decoratorET = new ElectricTrainDecorator(testVehicleLB);

    testVehicleDT = new DieselTrain(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 10, 1.0); //no passenger, no issue
    decoratorDT = new DieselTrainDecorator(testVehicleDT);

  }

  /**
   * Tests the vehicle creation process to ensure that the correct
   * vehicle are generated at each time step.
   */
  @Test
  public void testExecute() {
    //create a spy of a webserversession, and ensure its sendJson method does nothing.
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    //Setup simulator to have a list of active (test) vehicles
    VisualTransitSimulator stubSimulator = mock(VisualTransitSimulator.class);
    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(testVehicleSB);
    activeVehicles.add(testVehicleLB);
    activeVehicles.add(testVehicleET);
    activeVehicles.add(testVehicleDT);
    when(stubSimulator.getActiveVehicles()).thenReturn(activeVehicles);
    //List of decorators for easier testing
    VehicleDecorator[] listDecorator = {decoratorSB, decoratorLB, decoratorET, decoratorDT};
    //Prepare command input
    JsonObject command = new JsonObject();
    //create Argument captor to capture the argument of GetVehiclesCommand's sendJson call
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    //Call method
    GetVehiclesCommand gvc = new GetVehiclesCommand(stubSimulator);
    gvc.execute(webServerSessionDouble, command);
    //capture argument and check values.
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject output = messageCaptor.getValue();
    JsonArray jsonVehicleArray = output.getAsJsonArray("vehicles");
    for (int i = 0; i < activeVehicles.size(); i++) {
      Vehicle currVehicle = activeVehicles.get(i);
      JsonObject s = (JsonObject) jsonVehicleArray.get(i);
      assertEquals(currVehicle.getId(), s.get("id").getAsInt());
      assertEquals(currVehicle.getPassengers().size(), s.get("numPassengers").getAsInt());
      assertEquals(currVehicle.getCapacity(), s.get("capacity").getAsInt());
      if (currVehicle instanceof SmallBus) {
        assertEquals(SmallBus.SMALL_BUS_VEHICLE, s.get("type").getAsString());
      } else if (currVehicle instanceof LargeBus) {
        assertEquals(LargeBus.LARGE_BUS_VEHICLE, s.get("type").getAsString());
      } else if (currVehicle instanceof ElectricTrain) {
        assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, s.get("type").getAsString());
      } else if (currVehicle instanceof DieselTrain) {
        assertEquals(DieselTrain.DIESEL_TRAIN_VEHICLE, s.get("type").getAsString());
      } else {
        fail();
      }
      assertEquals(currVehicle.getCurrentCO2Emission(), s.get("co2").getAsInt());
      JsonObject positionJsonObject = s.getAsJsonObject("position");
      assertEquals(currVehicle.getPosition().getLongitude(),
          positionJsonObject.get("longitude").getAsDouble());
      assertEquals(currVehicle.getPosition().getLatitude(),
          positionJsonObject.get("latitude").getAsDouble());
      JsonObject colorJsonObject = s.getAsJsonObject("color");
      assertEquals(listDecorator[i].getRedValue(), colorJsonObject.get("r").getAsInt());
      assertEquals(listDecorator[i].getGreenValue(), colorJsonObject.get("g").getAsInt());
      assertEquals(listDecorator[i].getBlueValue(), colorJsonObject.get("b").getAsInt());
      assertEquals(listDecorator[i].getAlphaValue(), colorJsonObject.get("alpha").getAsInt());
    }

  }
}

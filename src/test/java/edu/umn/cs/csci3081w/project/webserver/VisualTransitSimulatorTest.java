package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import edu.umn.cs.csci3081w.project.model.ElectricTrain;
import edu.umn.cs.csci3081w.project.model.Issue;
import edu.umn.cs.csci3081w.project.model.LargeBus;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.PassengerGenerator;
import edu.umn.cs.csci3081w.project.model.PassengerLoader;
import edu.umn.cs.csci3081w.project.model.PassengerUnloader;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import edu.umn.cs.csci3081w.project.model.VehicleTestImpl;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class VisualTransitSimulatorTest {

  private VisualTransitSimulator testSimulator;
  private Vehicle testVehicle;
  private Route testRouteIn;
  private Route testRouteOut;

  /**
   * Initialization of the test Visual Transit Simulator class.
   */
  @BeforeEach
  public void testSimulatorCreation() {
    WebServerSession sessionDummy = mock(WebServerSession.class);
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;

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

    testVehicle = new VehicleTestImpl(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0, new PassengerLoader(), new PassengerUnloader());
    List<Line> testLines = new ArrayList<>();
    testLines.add(new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()));
    String configFile = URLDecoder.decode(getClass().getClassLoader()
        .getResource("config.txt").getFile(), StandardCharsets.UTF_8);
    testSimulator
        = new VisualTransitSimulator(configFile, sessionDummy);
    //todo: Current probblem is trying to stub teh configManager. Not necssary?

    //daytime
    testSimulator.setVehicleFactories(10);
    //Setting time-step values
    List<Integer> vehicleStartTimings = new ArrayList<>();
    vehicleStartTimings.add(1);
    vehicleStartTimings.add(1);
    int numTimeSteps = 4;
    testSimulator.start(vehicleStartTimings, numTimeSteps);
  }

  /**
   * Tests if the correct information are received through the configFile by checking the lines.
   */
  @Test
  public void testGetLines() {
    int j = 0;
    for (int i = 0; i < testSimulator.getLines().size(); i++) {
      assertEquals(10000 + i, testSimulator.getLines().get(i).getId());
    }
  }

  /**
   * Tests whether the update method works properly.
   */
  @Test
  public void testUpdate() {
    testSimulator.update();
    assertEquals(1, testSimulator.getSimulationTimeElapsed());
    for (int i = 0; i < testSimulator.getActiveVehicles().size(); i++) {
      if (testSimulator.getActiveVehicles().get(i) instanceof LargeBus) {
        assertEquals(2000, testSimulator.getActiveVehicles().get(i).getId());
      } else if (testSimulator.getActiveVehicles().get(i) instanceof ElectricTrain) {
        assertEquals(3000, testSimulator.getActiveVehicles().get(i).getId());
      }
    }
    testSimulator.getLines().get(1).createIssue();
    testSimulator.update();
    testSimulator.getLines().get(0).createIssue();
    for (int i = 0; i < 3; i++) {
      testSimulator.update();
    }
    assertEquals(5, testSimulator.getSimulationTimeElapsed());
  }

}

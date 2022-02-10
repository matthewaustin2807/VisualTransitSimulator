package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Issue;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.PassengerGenerator;
import edu.umn.cs.csci3081w.project.model.PassengerLoader;
import edu.umn.cs.csci3081w.project.model.PassengerUnloader;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import edu.umn.cs.csci3081w.project.model.VehicleTestImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LineIssueCommandTest {

  private Line testLine;
  private Vehicle testVehicle;
  private Line testLineIssue;
  private Vehicle testVehicleIssue;
  private Route testRouteIn;
  private Route testRouteOut;


  /**
   * Setup operations before each test runs.
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

    testLine = new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue());

    testVehicle = new VehicleTestImpl(1, testLine, 3, 1.0,
        new PassengerLoader(), new PassengerUnloader());

    testLineIssue = new Line(10101, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue());

    testVehicleIssue = new VehicleTestImpl(1, testLineIssue, 3, 1.0,
        new PassengerLoader(), new PassengerUnloader());
  }

  /**
   * Tests if the issue on each line are actually created when the user makes an issue to a line.
   */
  @Test
  public void testExecute() {
    //creating test objects
    ArrayList<Line> lineList = new ArrayList<>();
    lineList.add(testLine); //line w/o issue
    lineList.add(testLineIssue); //line w/ issue
    ArrayList<Vehicle> vehicleList = new ArrayList<>();
    vehicleList.add(testVehicle); //in line w/o issue
    vehicleList.add(testVehicleIssue); //in line w/ issue
    VisualTransitSimulator stubSimulator = mock(VisualTransitSimulator.class);
    when(stubSimulator.getLines()).thenReturn(lineList);
    when(stubSimulator.getActiveVehicles()).thenReturn(vehicleList);
    WebServerSession dummySession = mock(WebServerSession.class);
    JsonObject command = new JsonObject();
    command.addProperty("id", 10101); //line tested to have issue
    //Calling method
    LineIssueCommand testLic = new LineIssueCommand(stubSimulator);
    testLic.execute(dummySession, command);
    //Checking results
    assertFalse(testLine.isIssueExist());
    assertTrue(testLineIssue.isIssueExist());
    assertFalse(testVehicle.getHasBeenInABrokenLine());
    assertTrue(testVehicleIssue.getHasBeenInABrokenLine());
  }
}

package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Issue;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.PassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GetRoutesCommandTest {

  private Line testLine;
  private Line testLineIssue;
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

    testLineIssue = new Line(10101, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue());
  }

  /**
   * Tests if the correct route information are returned from the simulation.
   */
  @Test
  public void testExecute() {
    //creating test objects
    ArrayList<Line> lineList = new ArrayList<>();
    lineList.add(testLine); //line w/o issue
    lineList.add(testLineIssue); //line w/ issue
    VisualTransitSimulator stubSimulator = mock(VisualTransitSimulator.class);
    when(stubSimulator.getLines()).thenReturn(lineList);
    JsonObject command = new JsonObject();
    //create a spy of a webserversession, and ensure its sendJson method does nothing.
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    //create captor to grab "output" of execute.
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    //Call method and capture JsonObject
    GetRoutesCommand getRoutesCommand = new GetRoutesCommand(stubSimulator);
    getRoutesCommand.execute(webServerSessionDouble, command);
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    //Check the captured object's values
    JsonObject testOutput = messageCaptor.getValue();
    //Check contents
    JsonArray routesArray = testOutput.getAsJsonArray("routes");
    int j = 0;
    for (int i = 0; i < (lineList.size() * 2); i += 2) {
      assertTrue(equalsRoute((JsonObject) routesArray.get(i), lineList.get(j).getOutboundRoute()));
      assertTrue(equalsRoute((JsonObject) routesArray.get(i + 1),
          lineList.get(j).getInboundRoute()));
    }

  }

  private boolean equalsRoute(JsonObject objected, Route routed) {
    if (objected.get("id").getAsInt() != routed.getId()) {
      return false;
    }
    JsonArray stopsArray = objected.getAsJsonArray("stops");
    if (stopsArray.size() != routed.getStops().size()) {
      return false;
    }
    for (int i = 0; i < routed.getStops().size(); i++) {
      JsonObject thisStop = (JsonObject) stopsArray.get(i);
      Stop thatStop = routed.getStops().get(i);
      if (thisStop.get("id").getAsInt() != (thatStop.getId())) {
        return false;
      }
      if (thisStop.get("numPeople").getAsInt() != (thatStop.getPassengers().size())) {
        return false;
      }
      JsonObject thisPosition = thisStop.getAsJsonObject("position");
      if (thisPosition.get("longitude").getAsDouble() != (thatStop.getPosition().getLongitude())) {
        return false;
      }
      if (thisPosition.get("latitude").getAsDouble() != (thatStop.getPosition().getLatitude())) {
        return false;
      }
    }
    return true;
  }

}

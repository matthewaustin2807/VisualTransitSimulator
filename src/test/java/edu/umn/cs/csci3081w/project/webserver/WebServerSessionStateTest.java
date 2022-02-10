package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class WebServerSessionStateTest {

  /**
   * Tests if the commands from the simulator module are captured properly.
   */
  @Test
  public void testGetCommands() {
    WebServerSessionState webServerState = new WebServerSessionState();
    VisualTransitSimulator simulatorDummy = mock(VisualTransitSimulator.class);
    assertEquals(0, webServerState.getCommands().size());
    //Adding some instances of commands
    webServerState.getCommands().put("getRoutes", new GetRoutesCommand(simulatorDummy));
    webServerState.getCommands().put("getVehicles", new GetVehiclesCommand(simulatorDummy));
    webServerState.getCommands().put("start", new StartCommand(simulatorDummy));
    webServerState.getCommands().put("update", new UpdateCommand(simulatorDummy));
    webServerState.getCommands().put("initLines", new InitLinesCommand(simulatorDummy));
    webServerState.getCommands().put("registerVehicle", new RegisterVehicleCommand(simulatorDummy));
    webServerState.getCommands().put("lineIssue", new LineIssueCommand(simulatorDummy));
    //Checking contents
    assertEquals(7, webServerState.getCommands().size());
    assertTrue(webServerState.getCommands().get("getRoutes") instanceof GetRoutesCommand);
    assertTrue(webServerState.getCommands().get("getVehicles") instanceof GetVehiclesCommand);
    assertTrue(webServerState.getCommands().get("start") instanceof StartCommand);
    assertTrue(webServerState.getCommands().get("update") instanceof UpdateCommand);
    assertTrue(webServerState.getCommands().get("initLines") instanceof InitLinesCommand);
    assertTrue(webServerState.getCommands().get("registerVehicle")
        instanceof RegisterVehicleCommand);
    assertTrue(webServerState.getCommands().get("lineIssue") instanceof LineIssueCommand);
  }

}

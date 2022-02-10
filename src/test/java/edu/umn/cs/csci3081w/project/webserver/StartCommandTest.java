package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class StartCommandTest {

  /**
   * Tests if when the start button is clicked, all the necessary methods
   * and code are executed properly.
   */
  @Test
  public void testExecute() {
    //create test objects
    VisualTransitSimulator visualTransitSimulatorMock = mock(VisualTransitSimulator.class);
    WebServerSession dummySession = mock(WebServerSession.class);
    doNothing().when(visualTransitSimulatorMock)
        .setVehicleFactories(Mockito.isA(Integer.class));
    doNothing().when(visualTransitSimulatorMock)
        .start(Mockito.isA(List.class), Mockito.isA(Integer.class));
    int numTimeStepsValue = 5;
    int[] arr = {5, 10, 15, 20, 25};
    JsonArray timeBetweenVehicles = new JsonArray();
    for (int time : arr) {
      timeBetweenVehicles.add(time);
    }
    //Create command to pass and captor to catch arguments.
    JsonObject command = new JsonObject();
    command.addProperty("numTimeSteps", numTimeStepsValue);
    command.add("timeBetweenVehicles", timeBetweenVehicles);
    ArgumentCaptor<List> timeBetween = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<Integer> timeSteps = ArgumentCaptor.forClass(Integer.class);
    //call method and capture arguments
    StartCommand testStart = new StartCommand(visualTransitSimulatorMock);
    testStart.execute(dummySession, command);
    verify(visualTransitSimulatorMock).start(timeBetween.capture(), timeSteps.capture());
    //check arguments passed.
    for (int i = 0; i < arr.length; i++) {
      assertEquals(arr[i], timeBetween.getValue().get(i));
    }
    assertEquals(numTimeStepsValue, timeSteps.getValue());
  }

  /**
   * Tests if the getCurrentSimulationTime() method returns the correct value.
   */
  @Test
  public void testGetCurrentSimulationTime() {
    StartCommand startCommand = new StartCommand(mock(VisualTransitSimulator.class));
    assertEquals(LocalDateTime.now().getHour(), startCommand.getCurrentSimulationTime());
  }

}

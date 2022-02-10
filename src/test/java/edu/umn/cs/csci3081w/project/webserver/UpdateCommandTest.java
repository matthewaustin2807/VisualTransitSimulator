package edu.umn.cs.csci3081w.project.webserver;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

public class UpdateCommandTest {

  /**
   * Tests whether the update command is actually called by the simulator at each time step.
   */
  @Test
  public void testExecute() {
    WebServerSession dummySession = mock(WebServerSession.class);
    //todo: remove below line due to redundancy. Kept for later reference.
    //SimulatorCommand dummySimCom = Mockito.mock(SimulatorCommand.class,
    // Mockito.CALLS_REAL_METHODS);
    JsonObject someJson = new JsonObject();
    VisualTransitSimulator dummySimulator = mock(VisualTransitSimulator.class);
    UpdateCommand spyUpdate = new UpdateCommand(dummySimulator);
    doNothing().when(dummySimulator).update();
    //call method
    spyUpdate.execute(dummySession, someJson);
    //Check that the update command of the simulator was called.
    verify(dummySimulator, times(1)).update();

  }
}

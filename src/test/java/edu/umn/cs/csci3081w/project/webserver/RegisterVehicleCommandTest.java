package edu.umn.cs.csci3081w.project.webserver;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import edu.umn.cs.csci3081w.project.model.VehicleTestImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class RegisterVehicleCommandTest {

  /**
   * Tests if the vehicles are properly registered at each time step.
   */
  @Test
  public void testExecute() {
    //Creating test vehicles and simulator and dummy session.
    WebServerSession dummySession = mock(WebServerSession.class);
    VisualTransitSimulator dummySimulator = mock(VisualTransitSimulator.class);
    VehicleTestImpl stubVehicle1 = mock(VehicleTestImpl.class); //id: 5
    when(stubVehicle1.getId()).thenReturn(5);
    VehicleTestImpl stubVehicle2 = mock(VehicleTestImpl.class); //id: 10
    when(stubVehicle2.getId()).thenReturn(10);
    VehicleTestImpl stubVehicle3 = mock(VehicleTestImpl.class); //id: 15
    when(stubVehicle3.getId()).thenReturn(15);
    List<Vehicle> testActiveVehicles = new ArrayList<Vehicle>();
    testActiveVehicles.add(stubVehicle1);
    testActiveVehicles.add(stubVehicle2);
    testActiveVehicles.add(stubVehicle3);
    when(dummySimulator.getActiveVehicles()).thenReturn(testActiveVehicles);

    //Desired vehicle id is 10 (input)
    JsonObject vehicleId = new JsonObject();
    vehicleId.addProperty("id", 10);
    //Setup captor to catch statement that calls addObserver() (the "output")
    doNothing().when(dummySimulator).addObserver(Mockito.isA(VehicleTestImpl.class));
    ArgumentCaptor<VehicleTestImpl> messageCaptor = ArgumentCaptor.forClass(VehicleTestImpl.class);

    //Calling method and capturing "output"
    RegisterVehicleCommand rvc = new RegisterVehicleCommand(dummySimulator);
    rvc.execute(dummySession, vehicleId);
    verify(dummySimulator).addObserver(messageCaptor.capture());
    VehicleTestImpl passedVehicle = messageCaptor.getValue();
    //Check value of the passed vehicle is as expected (10)
    assertEquals(10, passedVehicle.getId());

  }
}

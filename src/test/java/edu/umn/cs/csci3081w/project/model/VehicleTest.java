package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.webserver.WebServerSession;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class VehicleTest {

  private Vehicle testVehicle;
  private Route testRouteIn;
  private Route testRouteOut;
  private SmallBus testSmallBus;
  private LargeBus testLargeBus;
  private ElectricTrain testElectricTrain;
  private DieselTrain testDieselTrain;


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

    testVehicle = new VehicleTestImpl(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0, new PassengerLoader(), new PassengerUnloader());
  }

  /**
   * Tests constructor.
   */
  @Test
  public void testConstructor() {

    assertEquals(1, testVehicle.getId());
    assertEquals("testRouteOut1", testVehicle.getName());
    assertEquals(3, testVehicle.getCapacity());
    assertEquals(1, testVehicle.getSpeed());
    assertEquals(testRouteOut, testVehicle.getLine().getOutboundRoute());
    assertEquals(testRouteIn, testVehicle.getLine().getInboundRoute());
  }

  /**
   * Tests if testIsTripComplete function works properly.
   */
  @Test
  public void testIsTripCompleteFalse() {
    assertEquals(false, testVehicle.isTripComplete());
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    assertEquals(true, testVehicle.isTripComplete());

  }


  /**
   * Tests if loadPassenger function works properly.
   */
  @Test
  public void testLoadPassenger() {

    Passenger testPassenger1 = new Passenger(3, "testPassenger1");
    Passenger testPassenger2 = new Passenger(2, "testPassenger2");
    Passenger testPassenger3 = new Passenger(1, "testPassenger3");
    Passenger testPassenger4 = new Passenger(1, "testPassenger4");

    assertEquals(1, testVehicle.loadPassenger(testPassenger1));
    assertEquals(1, testVehicle.loadPassenger(testPassenger2));
    assertEquals(1, testVehicle.loadPassenger(testPassenger3));
    assertEquals(0, testVehicle.loadPassenger(testPassenger4));
  }


  /**
   * Tests if move function works properly.
   */
  @Test
  public void testMove() {

    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
    testVehicle.move();

    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals(null, testVehicle.getNextStop());

  }

  /**
   * Tests if update function works properly.
   */
  @Test
  public void testUpdate() {

    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
    testVehicle.update();

    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.update();
    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.update();
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.update();
    assertEquals(null, testVehicle.getNextStop());

  }

  /**
   * Test to see if observer got attached.
   */
  @Test
  public void testProvideInfo() {
    //create a spy of a webserversession, and ensure its sendJson method does nothing.
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    //create a stub of a VehicleConcreteSubject that
    //returns the above spy when getSession() is called.
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    //create Argument captor to capture the output of Vehicle.
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    //set the test vehicle's VehicleSubject field to the stub.
    testVehicle.setVehicleSubject(vehicleConcreteSubjectStub);
    //call provideInfo and capture the output via the captor.
    testVehicle.update();
    testVehicle.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    //Check the captured object's values
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the correct info is generated for small buses.
   */
  @Test
  public void testProvideInfoSmallBus() {
    testSmallBus = new SmallBus(2, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testSmallBus.setVehicleSubject(vehicleConcreteSubjectStub);
    testSmallBus.update();
    testSmallBus.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "2" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: SMALL_BUS_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 1" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the correct info is generated for large buses.
   */
  @Test
  public void testProvideInfoLargeBus() {
    testLargeBus = new LargeBus(3, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testLargeBus.setVehicleSubject(vehicleConcreteSubjectStub);
    testLargeBus.update();
    testLargeBus.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "3" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: LARGE_BUS_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 3" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the correct info is generated for electric trains.
   */
  @Test
  public void testProvideInfoElectricTrain() {
    testElectricTrain = new ElectricTrain(4, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testElectricTrain.setVehicleSubject(vehicleConcreteSubjectStub);
    testElectricTrain.update();
    testElectricTrain.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "4" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: ELECTRIC_TRAIN_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the correct info is generated for diesel trains.
   */
  @Test
  public void testProvideInfoDieselTrain() {
    testDieselTrain = new DieselTrain(4, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testDieselTrain.setVehicleSubject(vehicleConcreteSubjectStub);
    testDieselTrain.update();
    testDieselTrain.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "4" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: DIESEL_TRAIN_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 6" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests whether the correct information is generated for each vehicle in the simulator.
   */
  @Test
  public void testProvideInfoMovingVehicle() {
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testVehicle.setVehicleSubject(vehicleConcreteSubjectStub);
    testVehicle.update();
    testVehicle.update();
    testVehicle.update();
    testVehicle.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.243774,44.972392)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0, 0, 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the correct info is generated when the trip is completed.
   */
  @Test
  public void testProvideInfoCompleteTrip() {
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testVehicle.setVehicleSubject(vehicleConcreteSubjectStub);
    testVehicle.update();
    testVehicle.update();
    testVehicle.update();
    testVehicle.update();
    testVehicle.update();
    testVehicle.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "";
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the correct info is generated when the trip has just started.
   */
  @Test
  public void testProvideInfoStartOfTrip() {
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testVehicle.setVehicleSubject(vehicleConcreteSubjectStub);
    testVehicle.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the update method works properly with a passenger loaded into it.
   */
  @Test
  public void testVehicleUpdateWithPassenger() {
    Passenger testPassenger = new Passenger(1, "Arfan");
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testVehicle.setVehicleSubject(vehicleConcreteSubjectStub);
    testVehicle.loadPassenger(testPassenger);
    testVehicle.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 1" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the vehicle update method works properly without passengers.
   */
  @Test
  public void testVehicleWithPassengerUnloaded() {
    Passenger testPassenger = new Passenger(1, "Arfan");
    WebServerSession webServerSessionDouble = spy(WebServerSession.class);
    doNothing().when(webServerSessionDouble).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject vehicleConcreteSubjectStub = mock(VehicleConcreteSubject.class);
    when(vehicleConcreteSubjectStub.getSession()).thenReturn(webServerSessionDouble);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testVehicle.setVehicleSubject(vehicleConcreteSubjectStub);
    testVehicle.loadPassenger(testPassenger);
    testVehicle.update();
    testVehicle.update();
    testVehicle.update();
    testVehicle.provideInfo();
    verify(webServerSessionDouble).sendJson(messageCaptor.capture());
    JsonObject testOutput = messageCaptor.getValue();
    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.243774,44.972392)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0, 0, 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Tests if the vehicle responds properly if the speed of the vehicle is negative.
   */
  @Test
  public void testVehicleWithNegativeSpeed() {
    testSmallBus = new SmallBus(2, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, -1.0);
    testSmallBus.update();
    assertEquals(-93.235071, testSmallBus.getPosition().getLongitude());
    assertEquals(44.97358, testSmallBus.getPosition().getLatitude());
  }

  /**
   * Tests if the vehicle's update method works properly if there is an issue on the line.
   */
  @Test
  public void testWithLineIssue() {
    Issue testIssue = new Issue();
    testIssue.createIssue();
    testSmallBus = new SmallBus(2, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn, testIssue), 3, 1.0);
    testSmallBus.update();
    assertEquals(-93.235071, testSmallBus.getPosition().getLongitude());
    assertEquals(44.97358, testSmallBus.getPosition().getLatitude());
  }

  /**
   * Clean up our variables after each test.
   */
  @AfterEach
  public void cleanUpEach() {
    testVehicle = null;
    testSmallBus = null;
    testLargeBus = null;
    testElectricTrain = null;
    testDieselTrain = null;
  }

}

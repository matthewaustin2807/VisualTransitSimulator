package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DieselTrainDecoratorTest {
  private Route testRouteOut;
  private Route testRouteIn;
  private Line testLine;
  private Train testDieselTrain;
  private Train testDieselTrainIssue;

  /**
   * Sets up the variables used in the tests.
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

    testLine = new Line(10000, "testLine", Line.BUS_LINE,
        testRouteOut, testRouteIn, new Issue());

    testDieselTrain = new DieselTrain(1, testLine, 3, 1.0);
  }

  /**
   * Tests if the constructor works properly.
   */
  @Test
  public void constructorTest() {
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(1, testDecorator.getId());
    assertEquals(10000, testDecorator.getLine().getId());
  }

  /**
   * Tests getRedValue().
   */
  @Test
  public void getRedValueTest() {
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(255, testDecorator.getRedValue());
  }

  /**
   * Tests getGreenValue().
   */
  @Test
  public void getGreenValueTest() {
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(204, testDecorator.getGreenValue());
  }

  /**
   * Tests getBlueValue().
   */
  @Test
  public void getBlueValueTest() {
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(51, testDecorator.getBlueValue());
  }

  /**
   * Tests getAlphaValue().
   */
  @Test
  public void getAlphaValueTest() {
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(255, testDecorator.getAlphaValue());
    testDieselTrain.getLine().createIssue();
    boolean test = testDieselTrain.getLine().isIssueExist();
    testDieselTrain.setHasBeenInABrokenLine();
    testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(155, testDecorator.getAlphaValue());
  }

  /**
   * Tests if updateDistance function works properly.
   */
  @Test
  public void testReport() {
    Passenger testPassenger = new Passenger(1, "test");
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    testDecorator.loadPassenger(testPassenger);
    try {
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testDecorator.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Diesel Train Info Start####" + System.lineSeparator()
              + "ID: 1" + System.lineSeparator()
              + "Name: testRouteOut1" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Capacity: 3" + System.lineSeparator()
              + "Position: 44.97358,-93.235071" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 1" + System.lineSeparator()
              + "####Passenger Info Start####" + System.lineSeparator()
              + "Name: test" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on vehicle: 1" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Diesel Train Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test the co2 calculation for a train.
   */
  @Test
  public void testCurrentCO2Emission() {
    DieselTrainDecorator testDecorator = new DieselTrainDecorator(testDieselTrain);
    assertEquals(6, testDecorator.getCurrentCO2Emission());
    Passenger testPassenger1 = new Passenger(3, "testPassenger1");
    testDecorator.loadPassenger(testPassenger1);
    assertEquals(8, testDecorator.getCurrentCO2Emission());
  }

  /**
   * Clean up our variables after each test.
   */
  @AfterEach
  public void cleanUpEach() {
    testDieselTrain = null;
  }

}

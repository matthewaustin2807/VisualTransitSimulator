package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusFactoryTest {
  private StorageFacility storageFacility;
  private BusFactory busFactory;
  private BusFactory testBusFactory;

  @BeforeEach
  public void setUp() {
    storageFacility = new StorageFacility(2, 1, 0, 0);
    busFactory = new BusFactory(storageFacility, new Counter(), 9);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testConstructor() {
    assertTrue(busFactory.getGenerationStrategy() instanceof BusStrategyDay);
  }

  @Test
  public void testConstructorNight() {
    testBusFactory = new BusFactory(storageFacility, new Counter(), 0);
    assertTrue(testBusFactory.getGenerationStrategy() instanceof BusStrategyNight);
  }

  @Test
  public void testConstructorWithLargeTimeValue() {
    testBusFactory = new BusFactory(storageFacility, new Counter(), 1000);
    assertTrue(testBusFactory.getGenerationStrategy() instanceof BusStrategyNight);
  }

  /**
   * Testing if generated vehicle is working according to strategy to create Large Bus.
   */
  @Test
  public void testGenerateLargeBus() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Line line = new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue());

    Vehicle vehicle = busFactory.generateVehicle(line);
    assertTrue(vehicle instanceof LargeBus);
  }

  /**
   * Testing if generated vehicle is working according to strategy to create Small Bus.
   */
  @Test
  public void testGenerateSmallBus() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
            stopsOut, distancesOut, generatorOut);

    Line line = new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
            new Issue());

    storageFacility = new StorageFacility(2, 4, 0, 0);
    testBusFactory = new BusFactory(storageFacility, new Counter(), 9);
    testBusFactory.generateVehicle(line);
    testBusFactory.generateVehicle(line);
    Vehicle vehicle = testBusFactory.generateVehicle(line);
    assertTrue(vehicle instanceof SmallBus);
  }

  /**
   * Testing if generated vehicle is null when stock is empty.
   */
  @Test
  public void testGenerateVehicleNull() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
            stopsOut, distancesOut, generatorOut);

    Line line = new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
            new Issue());

    storageFacility = new StorageFacility(0, 0, 0, 0);
    testBusFactory = new BusFactory(storageFacility, new Counter(), 9);
    Vehicle vehicle = testBusFactory.generateVehicle(line);
    assertEquals(null, vehicle);
  }

  /**
   * Testing if Large Bus got returned.
   */
  @Test
  public void testReturnVehicleLargeBus() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Bus testBus = new LargeBus(1, new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0);

    assertEquals(2, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(1, busFactory.getStorageFacility().getLargeBusesNum());
    busFactory.returnVehicle(testBus);
    assertEquals(2, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(2, busFactory.getStorageFacility().getLargeBusesNum());

  }

  /**
   * Testing if Small Bus got returned.
   */
  @Test
  public void testReturnVehicleSmallBus() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
            stopsOut, distancesOut, generatorOut);

    Bus testBus = new SmallBus(1, new Line(10000, "testLine", "BUS", testRouteOut, testRouteIn,
            new Issue()), 3, 1.0);

    assertEquals(2, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(1, busFactory.getStorageFacility().getLargeBusesNum());
    busFactory.returnVehicle(testBus);
    assertEquals(3, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(1, busFactory.getStorageFacility().getLargeBusesNum());

  }

  /**
   * Testing if Null got returned.
   */
  @Test
  public void testReturnNull() {
    assertEquals(2, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(1, busFactory.getStorageFacility().getLargeBusesNum());
    busFactory.returnVehicle(null);
    assertEquals(2, busFactory.getStorageFacility().getSmallBusesNum());
    assertEquals(1, busFactory.getStorageFacility().getLargeBusesNum());
  }

  /**
   * Cleans up the slate after each test.
   */
  @AfterEach
  public void cleanUp() {
    busFactory = null;
    storageFacility = null;
    testBusFactory = null;
  }
}

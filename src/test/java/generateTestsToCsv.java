import org.junit.Before;
import org.junit.Test;
import simulation.Simulation;
import simulation.Wind;
import simulation.WindIndicator;
import yacht.Yacht;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class generateTestsToCsv {
    SecureRandom random = new SecureRandom();
    Simulation simulation = new Simulation();
    Yacht yacht = new Yacht();
    Wind trueWind = new Wind();
    double[] data;


    @Before
    public void before() {
        trueWind.setSpeed(5);
//        trueWind.setDirection(300);
//        yacht.setRequiredCourseAzimuth(70, trueWind.getDirection());
//        yacht.sail.setCurrentStateOfSail(StatesOfSail.STARBOARD);
        yacht.process(0, 0, trueWind);
    }

    @Test
    public void show_wind_gradient() {
        //Given
        String filepath = "outputData\\show_wind_gradient.csv";
        WriteData writeData = new WriteData(new String[]{"height", "wind speed"});
        WindIndicator windIndicator = new WindIndicator();
        //When
        for (int height = 0; height < 20; height++) {
            windIndicator.measureWind(trueWind, yacht, height);
            data = new double[]{height, windIndicator.getSpeed()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }

    @Test
    public void show_apparent_wind_45() {
        //Given
        trueWind.setDirection(45);
        String filepath = "outputData\\show_apparent_wind_45.csv";
        WriteData writeData = new WriteData(new String[]{"SOG", "TWS", "TWS at foot", "TWS at head", "TWA", "TWA at foot", "TWA at head"});
        //When
        for (int i = 0; i < 20; i++) {
            simulation.go(yacht, trueWind);
            data = new double[]{yacht.getVelocity(),
                    trueWind.getSpeed(), yacht.windIndicatorAtFoot.getSpeed(), yacht.windIndicatorAtHead.getSpeed(),
                    trueWind.getDirection(), yacht.windIndicatorAtFoot.getDirection(), yacht.windIndicatorAtHead.getDirection()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }

    @Test
    public void show_apparent_wind_150() {
        //Given
        trueWind.setDirection(150);
        String filepath = "outputData\\show_apparent_wind_150.csv";
        WriteData writeData = new WriteData(new String[]{"SOG", "TWS", "TWS at foot", "TWS at head", "TWA", "TWA at foot", "TWA at head"});
        //When
        for (int i = 0; i < 20; i++) {
            simulation.go(yacht, trueWind);
            data = new double[]{yacht.getVelocity(),
                    trueWind.getSpeed(), yacht.windIndicatorAtFoot.getSpeed(), yacht.windIndicatorAtHead.getSpeed(),
                    trueWind.getDirection(), yacht.windIndicatorAtFoot.getDirection(), yacht.windIndicatorAtHead.getDirection()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }

    @Test
    public void keep_close_to_wind() {
        //Given
        trueWind.setDirection(45);
        String filepath = "outputData\\keep_close_to_wind.csv";
        WriteData writeData = new WriteData(new String[]{"TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i < 60; i++) {
            simulation.go(yacht, trueWind);
            data = new double[]{trueWind.getSpeed(), trueWind.getDirection(), yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }

    @Test
    public void keep_in_the_beam() {
        //Given
        trueWind.setDirection(90);
        String filepath = "outputData\\keep_in_the_beam.csv";
        WriteData writeData = new WriteData(new String[]{"TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i < 60; i++) {
            simulation.go(yacht, trueWind);
            data = new double[]{trueWind.getSpeed(), trueWind.getDirection(), yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }

    @Test
    public void keep_in_the_quarter() {
        //Given
        trueWind.setDirection(135);
        String filepath = "outputData\\keep_in_the_quarter.csv";
        WriteData writeData = new WriteData(new String[]{"TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i < 60; i++) {
            simulation.go(yacht, trueWind);
            data = new double[]{trueWind.getSpeed(), trueWind.getDirection(), yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }

    @Test
    public void keep_in_the_run() {
        //Given
        trueWind.setDirection(180);
        String filepath = "outputData\\keep_in_the_run.csv";
        WriteData writeData = new WriteData(new String[]{"TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i < 60; i++) {
            simulation.go(yacht, trueWind);
            data = new double[]{trueWind.getSpeed(), trueWind.getDirection(), yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
        }
        //Then
    }


}

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

import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

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
        for (int height = 0; height <= 20; height++) {
            windIndicator.measureWind(trueWind, yacht, height);
            data = new double[]{height, windIndicator.getApparentWind().getSpeed()};
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
        trueWind.setAzimuthDirection(45);
        String filepath = "outputData\\show_apparent_wind_45.csv";
        WriteData writeData = new WriteData(new String[]{"SOG", "TWS", "AWS at foot", "AWS at head", "TWA", "AWA at foot", "AWA at head"});
        //When
        for (int i = 0; i <= 30; i++) {
            yacht.setVelocity((double) i / 10);
            simulation.goSample(yacht, trueWind);
            yacht.setVelocity((double) i / 10);
            data = new double[]{yacht.getVelocity(),
                    trueWind.getSpeed(), yacht.windIndicatorAtFoot.getApparentWind().getSpeed(), yacht.windIndicatorAtHead.getApparentWind().getSpeed(),
                    trueWind.getAzimuthDirection(), yacht.windIndicatorAtFoot.getApparentWind().getRelativeDirection(), yacht.windIndicatorAtHead.getApparentWind().getRelativeDirection()};
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
        trueWind.setAzimuthDirection(150);
        String filepath = "outputData\\show_apparent_wind_150.csv";
        WriteData writeData = new WriteData(new String[]{"SOG", "TWS", "AWS at foot", "AWS at head", "TWA", "AWA at foot", "AWA at head"});
        //When
        for (int i = 0; i <= 30; i++) {
            yacht.setVelocity((double) i / 10);
            simulation.goSample(yacht, trueWind);
            yacht.setVelocity((double) i / 10);
            data = new double[]{yacht.getVelocity(),
                    trueWind.getSpeed(), yacht.windIndicatorAtFoot.getApparentWind().getSpeed(), yacht.windIndicatorAtHead.getApparentWind().getSpeed(),
                    trueWind.getAzimuthDirection(), yacht.windIndicatorAtFoot.getApparentWind().getRelativeDirection(), yacht.windIndicatorAtHead.getApparentWind().getRelativeDirection()};
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
        trueWind.setAzimuthDirection(45);
        String filepath = "outputData\\keep_close_to_wind.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
        //Then
    }

    @Test
    public void keep_in_the_beam() {
        //Given
        trueWind.setAzimuthDirection(90);
        String filepath = "outputData\\keep_in_the_beam.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
        //Then
    }

    @Test
    public void keep_in_the_quarter() {
        //Given
        trueWind.setAzimuthDirection(135);
        String filepath = "outputData\\keep_in_the_quarter.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
        //Then
    }

    @Test
    public void keep_in_the_run() {
        //Given
        trueWind.setAzimuthDirection(180);
        String filepath = "outputData\\keep_in_the_run.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
        //Then
    }

    @Test
    public void change_COG_const_wind_45to150() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(45);
        yacht.setRequiredCourseAzimuth(45, trueWind.getAzimuthDirection());
        String filepath = "outputData\\change_COG_const_wind_45to150.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(150, trueWind.getAzimuthDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void change_COG_const_wind_150to45() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(150);
        yacht.setRequiredCourseAzimuth(150, trueWind.getAzimuthDirection());
        String filepath = "outputData\\change_COG_const_wind_150to45.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(45, trueWind.getAzimuthDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void change_COG_const_wind_doTack() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(45);
        yacht.setRequiredCourseAzimuth(45, trueWind.getAzimuthDirection());
        String filepath = "outputData\\change_COG_const_wind_doTack.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(315, trueWind.getAzimuthDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void change_COG_const_wind_doGybe() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(150);
        yacht.setRequiredCourseAzimuth(150, trueWind.getAzimuthDirection());
        String filepath = "outputData\\change_COG_const_wind_doGybe.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(210, trueWind.getAzimuthDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void requiredCOG_inIrons_startCOG60_startSOG2() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(60);
        yacht.setRequiredCourseAzimuth(60, trueWind.getAzimuthDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG60_startSOG2.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(10, trueWind.getAzimuthDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void requiredCOG_inIrons_startCOG60_startSOG2_doTack() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(60);
        yacht.setRequiredCourseAzimuth(60, trueWind.getAzimuthDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG60_startSOG2_doTack.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(350, trueWind.getAzimuthDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void requiredCOG_inIrons_startCOG0_startSOG2() {
        //Given
        yacht.setVelocity(2);
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG0_startSOG2.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void requiredCOG_inIrons_startCOG0_startSOG0() {
        //Given
        yacht.setVelocity(0);
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG0_startSOG0.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void const_COG_changingWindDirection() {
        //Given
        trueWind.setSpeed(8);
        trueWind.setAzimuthDirection(360);
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\const_COG_changingWindDirection_8.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder", "Vmg"});
        //When
        for (int i = 0; i <= 180; i++) {
            data = new double[]{i, trueWind.getSpeed(), 360 - trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle(),
                    yacht.getVelocity() * cos(toRadians(360 - trueWind.getAzimuthDirection()))};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            trueWind.setAzimuthDirection(trueWind.getAzimuthDirection() - 1);
            yacht.setCurrentCourseAzimuth(0);
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void const_COG_changingWindDirection_throughIrons() {
        //Given
        trueWind.setSpeed(5);
        trueWind.setAzimuthDirection(270);
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\const_COG_changingWindDirection_throughIrons.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 360; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            trueWind.setAzimuthDirection(trueWind.getAzimuthDirection() + 0.5);
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void const_COG_changingWindSpeed() {
        //Given
        trueWind.setSpeed(0);
        trueWind.setAzimuthDirection(90);
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\const_COG_changingWindSpeed_90.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            trueWind.setSpeed(trueWind.getSpeed() + 0.1);
            simulation.goSecond(yacht, trueWind);
        }
    }

    @Test
    public void const_COG_changingWind() {
        //Given
        trueWind.setSpeed(random.nextDouble() * 4 + 3);
        trueWind.setAzimuthDirection(random.nextInt(360));
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\const_COG_changingWind.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 300; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            for (int j = 0; j < 1 / Simulation.samplingPeriod; j++) {
                trueWind.setSpeed(trueWind.getSpeed() + (random.nextDouble() - 0.5) * 0.25);
                trueWind.setAzimuthDirection(trueWind.getAzimuthDirection() + (random.nextDouble() - 0.5) * 2);
                simulation.goSample(yacht, trueWind);
            }
        }
    }

    @Test
    public void changing_COG_changingWind() {
        //Given
        trueWind.setSpeed(random.nextDouble() * 4 + 3);
        trueWind.setAzimuthDirection(random.nextInt(360));
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getAzimuthDirection());
        String filepath = "outputData\\changing_COG_changingWind.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        for (int i = 0; i <= 300; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getAzimuthDirection(),
                    yacht.getVelocity(), yacht.getRequiredCourseAzimuth(), yacht.getCurrentCourseAzimuth(),
                    yacht.sail.getCurrentTrimAngle(), yacht.sail.getCurrentTwistAngle(), yacht.rudder.getCurrentAngle()};
            writeData.addLine(data);
            try {
                Files.deleteIfExists(Paths.get(filepath));
                writeData.writeArrayToCSV(filepath);
            } catch (IOException e) {
                System.err.println("IOException occurred.");
                System.exit(1);
            }
            if (i % 60 == 0)
                yacht.setRequiredCourseAzimuth(random.nextInt(360), trueWind.getAzimuthDirection());
            for (int j = 0; j < 1 / Simulation.samplingPeriod; j++) {
                trueWind.setSpeed(trueWind.getSpeed() + (random.nextDouble() - 0.5) * 0.25);
                trueWind.setAzimuthDirection(trueWind.getAzimuthDirection() + (random.nextDouble() - 0.5) * 2);
                simulation.goSample(yacht, trueWind);
            }
        }
    }

}

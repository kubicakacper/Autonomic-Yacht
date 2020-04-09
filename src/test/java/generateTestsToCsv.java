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
        trueWind.setDirection(45);
        String filepath = "outputData\\show_apparent_wind_45.csv";
        WriteData writeData = new WriteData(new String[]{"SOG", "TWS", "AWS at foot", "AWS at head", "TWA", "AWA at foot", "AWA at head"});
        //When
        for (int i = 0; i <= 30; i++) {
            yacht.setVelocity((double) i / 10);
            simulation.goSample(yacht, trueWind);
            yacht.setVelocity((double) i / 10);
            data = new double[]{yacht.getVelocity(),
                    trueWind.getSpeed(), yacht.windIndicatorAtFoot.getApparentWind().getSpeed(), yacht.windIndicatorAtHead.getApparentWind().getSpeed(),
                    trueWind.getDirection(), yacht.windIndicatorAtFoot.getApparentWind().getDirection(), yacht.windIndicatorAtHead.getApparentWind().getDirection()};
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
        WriteData writeData = new WriteData(new String[]{"SOG", "TWS", "AWS at foot", "AWS at head", "TWA", "AWA at foot", "AWA at head"});
        //When
        for (int i = 0; i <= 30; i++) {
            yacht.setVelocity((double) i / 10);
            simulation.goSample(yacht, trueWind);
            yacht.setVelocity((double) i / 10);
            data = new double[]{yacht.getVelocity(),
                    trueWind.getSpeed(), yacht.windIndicatorAtFoot.getApparentWind().getSpeed(), yacht.windIndicatorAtHead.getApparentWind().getSpeed(),
                    trueWind.getDirection(), yacht.windIndicatorAtFoot.getApparentWind().getDirection(), yacht.windIndicatorAtHead.getApparentWind().getDirection()};
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
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        trueWind.setDirection(90);
        String filepath = "outputData\\keep_in_the_beam.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        trueWind.setDirection(135);
        String filepath = "outputData\\keep_in_the_quarter.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        trueWind.setDirection(180);
        String filepath = "outputData\\keep_in_the_run.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 20; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(45, trueWind.getDirection());
        String filepath = "outputData\\change_COG_const_wind_45to150.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(150, trueWind.getDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(150, trueWind.getDirection());
        String filepath = "outputData\\change_COG_const_wind_150to45.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(45, trueWind.getDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(45, trueWind.getDirection());
        String filepath = "outputData\\change_COG_const_wind_doTack.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(315, trueWind.getDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(150, trueWind.getDirection());
        String filepath = "outputData\\change_COG_const_wind_doGybe.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(210, trueWind.getDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(60, trueWind.getDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG60_startSOG2.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(10, trueWind.getDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(60, trueWind.getDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG60_startSOG2_doTack.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            if (i == 10)
                yacht.setRequiredCourseAzimuth(350, trueWind.getDirection());
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(0, trueWind.getDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG0_startSOG2.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        yacht.setRequiredCourseAzimuth(0, trueWind.getDirection());
        String filepath = "outputData\\requiredCOG_inIrons_startCOG0_startSOG0.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 60; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
        trueWind.setDirection(random.nextInt(360));
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getDirection());
        String filepath = "outputData\\const_COG_changingWindDirection.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 300; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
                trueWind.setDirection(trueWind.getDirection() + (random.nextDouble() - 0.5) * 2);
                simulation.goSample(yacht, trueWind);
            }
        }
    }

    @Test
    public void const_COG_changingWindSpeed() {
        //Given
        trueWind.setSpeed(random.nextDouble() * 4 + 3);
        trueWind.setDirection(90);
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getDirection());
        String filepath = "outputData\\const_COG_changingWindSpeed.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 300; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
                simulation.goSample(yacht, trueWind);
            }
        }
    }

    @Test
    public void const_COG_changingWind() {
        //Given
        trueWind.setSpeed(random.nextDouble() * 4 + 3);
        trueWind.setDirection(random.nextInt(360));
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getDirection());
        String filepath = "outputData\\const_COG_changingWind.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        //When
        for (int i = 0; i <= 300; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
                trueWind.setDirection(trueWind.getDirection() + (random.nextDouble() - 0.5) * 2);
                simulation.goSample(yacht, trueWind);
            }
        }
    }

    @Test
    public void changing_COG_changingWind() {
        //Given
        trueWind.setSpeed(random.nextDouble() * 4 + 3);
        trueWind.setDirection(random.nextInt(360));
        yacht.setCurrentCourseAzimuth(0);
        yacht.setRequiredCourseAzimuth(0, trueWind.getDirection());
        String filepath = "outputData\\changing_COG_changingWind.csv";
        WriteData writeData = new WriteData(new String[]{"time", "TWS", "TWA", "SOG", "set COG", "actual COG", "sail trim", "sail twist", "rudder"});
        for (int i = 0; i <= 300; i++) {
            data = new double[]{i, trueWind.getSpeed(), trueWind.getDirection(),
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
                yacht.setRequiredCourseAzimuth(random.nextInt(360), trueWind.getDirection());
            for (int j = 0; j < 1 / Simulation.samplingPeriod; j++) {
                trueWind.setSpeed(trueWind.getSpeed() + (random.nextDouble() - 0.5) * 0.25);
                trueWind.setDirection(trueWind.getDirection() + (random.nextDouble() - 0.5) * 2);
                simulation.goSample(yacht, trueWind);
            }
        }
    }

}

package simulation;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yacht.Yacht;

import static java.lang.Math.log;

@ToString
@EqualsAndHashCode
public class Simulation {
    public static final double samplingPeriod = 0.05;       //in seconds
    public static final double waterDensity = 1026;        //kg/m^3
    public static final double airDensity = 1.225;         //kg/m^3

    Wind wind;

    public Simulation() {
        wind = new Wind(0, 0);
    }

    public Simulation(double windSpeed, double windDirection) {
        wind = new Wind(windSpeed, windDirection);
    }
    //wind speed at specified height: speedAtWaterLevel * windGradient

    private static double log2(double a) {
        return log(a) / log(2);
    }

    static double windGradient(double height) {
        return log2(height + 1) * 0.2 + 1;
    }

    public void goSecond(Yacht yacht, Wind trueWind) {
        for (int i = 0; i < 1 / samplingPeriod; i++) {

            Wind apparentWindAtFoot = yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
            yacht.sail.sailController.countControlValueAtFoot(apparentWindAtFoot.getDirection(), yacht.sail);
            yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
            yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
            yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());

            Wind apparentWindAtHead = yacht.windIndicatorAtHead.measureWind(trueWind, yacht, yacht.sail.getHeadHeight());
            yacht.sail.sailController.countControlValueAtHead(apparentWindAtHead.getDirection(), yacht.sail);
            yacht.sail.sheetEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtHead());
            yacht.sail.sheetEngine.setCurrentVelocity(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
            yacht.sail.sheet.countLengthOverMin(yacht.sail.sheetEngine.getCurrentVelocity());

            yacht.rudder.rudderController.countControlValue(yacht);
            yacht.rudder.rudderEngineController.setStateOfEngine(yacht.rudder.rudderController.getCurrentControlValue());
            yacht.rudder.rudderEngine.setCurrentAngleVelocity(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());

            double thrustForce = yacht.sail.countThrustForce(yacht);
            double sideForce = yacht.rudder.countSideForce(yacht.rudder.rudderEngine.getCurrentVelocity(), yacht);
            yacht.process(thrustForce, sideForce, trueWind);
        }
    }

    public void goSample(Yacht yacht, Wind trueWind) {
        Wind apparentWindAtFoot = yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
        yacht.sail.sailController.countControlValueAtFoot(apparentWindAtFoot.getDirection(), yacht.sail);
        yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
        yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
        yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());

        Wind apparentWindAtHead = yacht.windIndicatorAtHead.measureWind(trueWind, yacht, yacht.sail.getHeadHeight());
        yacht.sail.sailController.countControlValueAtHead(apparentWindAtHead.getDirection(), yacht.sail);
        yacht.sail.sheetEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtHead());
        yacht.sail.sheetEngine.setCurrentVelocity(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
        yacht.sail.sheet.countLengthOverMin(yacht.sail.sheetEngine.getCurrentVelocity());

        yacht.rudder.rudderController.countControlValue(yacht);
        yacht.rudder.rudderEngineController.setStateOfEngine(yacht.rudder.rudderController.getCurrentControlValue());
        yacht.rudder.rudderEngine.setCurrentAngleVelocity(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());

        double thrustForce = yacht.sail.countThrustForce(yacht);
        double sideForce = yacht.rudder.countSideForce(yacht.rudder.rudderEngine.getCurrentVelocity(), yacht);
        yacht.process(thrustForce, sideForce, trueWind);
    }
}

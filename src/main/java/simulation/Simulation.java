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
        wind = new Wind();
    }
    //wind speed at specified height: speedAtWaterLevel * windGradient

    private static double log2(double a) {
        return log(a) / log(2);
    }

    static double windGradient(double height) {
        return log2(height + 1) * 0.2 + 1;
    }

    public void goSecond(Yacht yacht, Wind trueWind) {
        for (int i = 0; i < 1 / samplingPeriod; i++)
            goSample(yacht, trueWind);
    }

    public void goSample(Yacht yacht, Wind trueWind) {
        yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
        yacht.sail.sailController.countControlValueAtFoot(yacht.windIndicatorAtFoot.getApparentWind().getRelativeDirection(), yacht.sail);
        yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
        yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
        yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());

        yacht.windIndicatorAtHead.measureWind(trueWind, yacht, yacht.sail.getHeadHeight());
        yacht.sail.sailController.countControlValueAtHead(yacht.windIndicatorAtHead.getApparentWind().getRelativeDirection(), yacht.sail);
        yacht.sail.sheetEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtHead());
        yacht.sail.sheetEngine.setCurrentVelocity(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
        yacht.sail.sheet.countLengthOverMin(yacht.sail.sheetEngine.getCurrentVelocity());

        yacht.rudder.rudderController.countControlValue(yacht);
        yacht.rudder.rudderEngineController.setStateOfEngine(yacht.rudder.rudderController.getCurrentControlValue());
        yacht.rudder.rudderEngine.setCurrentAngleVelocity(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());

        yacht.sail.countThrustForce(yacht);
        yacht.rudder.countSideForce(yacht.rudder.rudderEngine.getCurrentVelocity(), yacht);
        yacht.process(yacht.getThrustForce(), yacht.getSideForce(), trueWind);
}
}

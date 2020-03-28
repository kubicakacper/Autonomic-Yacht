package yacht;

import lombok.Data;
import simulation.Simulation;
import simulation.Wind;
import simulation.WindIndicator;
import yacht.rudder.Rudder;
import yacht.sail.Sail;
import yacht.sail.StatesOfSail;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

@Data
public class Yacht {

    private final double mass = 500;                                //in kg
    private final double momentOfInertia = 2000;                     //in basic SI unit
    private final double distanceRudderFromCenterOfRotation = 2;    //in meters
    private final double closestCourseAgainstWind = 37;             // concerns TRUE wind
    public Sail sail;
    public Rudder rudder;
    private double requiredCourseAzimuth;
    public WindIndicator windIndicatorAtFoot;
    public WindIndicator windIndicatorAtHead;
    private double courseAgainstWind;   // left:"-", right:"+".
    private double speedAgainstWind;    // apparent wind
    private double velocity;
    private double acceleration;
    private double angleVelocity;   // angle velocity of turn in basic SI ...; left:"-", right:"+".
    private double angleAcceleration;
    private double followedCourseAzimuth;
    private double currentCourseAzimuth;

    public Yacht() {
        this.sail = new Sail();
        this.rudder = new Rudder();
        this.windIndicatorAtFoot = new WindIndicator();
        this.windIndicatorAtHead = new WindIndicator();
    }

    public Yacht(double sailArea, double sailFootHeight, double sailHeadHeight, StatesOfSail stateOfSail,
                 double sailControllerProportionalCoefficientForTrim, double sailControllerProportionalCoefficientForTwist,
                 double carEngineControllerHysteresis, double carEngineControllerOffset,
                 double carEngineMaxVelocity,
                 double carDistanceFromMast,
                 double sheetEngineControllerHysteresis, double sheetEngineControllerOffset,
                 double sheetEngineMaxVelocity,
                 int sheetGearRatio,
                 double rudderArea,
                 double rudderControllerProportionalCoefficient, double rudderControllerIntegralCoefficient, double rudderControllerDerivativeCoefficient,
                 double rudderEngineControllerHysteresis, double rudderEngineControllerOffset,
                 double rudderEngineMaxVelocity) {

        windIndicatorAtFoot = new WindIndicator();
        windIndicatorAtHead = new WindIndicator();

        sail = new Sail(sailArea, sailFootHeight, sailHeadHeight, stateOfSail,
                sailControllerProportionalCoefficientForTrim, sailControllerProportionalCoefficientForTwist,
                carEngineControllerHysteresis, carEngineControllerOffset,
                carEngineMaxVelocity,
                carDistanceFromMast,
                sheetEngineControllerHysteresis, sheetEngineControllerOffset,
                sheetEngineMaxVelocity,
                sheetGearRatio
        );


        rudder = new Rudder(rudderArea,
                rudderControllerProportionalCoefficient, rudderControllerIntegralCoefficient, rudderControllerDerivativeCoefficient,
                rudderEngineControllerHysteresis, rudderEngineControllerOffset,
                rudderEngineMaxVelocity);
    }

    //this function disables Lombok automatically generated setter with one parameter
    private void setRequiredCourseAzimuth(double requiredCourseAzimuth) {
    }

    public void setRequiredCourseAzimuth(double requiredCourseAzimuth, double trueWindDirection) {
        this.requiredCourseAzimuth = requiredCourseAzimuth;
        double absDifference = abs(requiredCourseAzimuth - trueWindDirection);
        double circleAbsDifference = absDifference;
        if (absDifference > 180)
            circleAbsDifference = 360 - absDifference;
        if (circleAbsDifference >= closestCourseAgainstWind)
            setFollowedCourseAzimuth(requiredCourseAzimuth);
        else {
            double difference = requiredCourseAzimuth - trueWindDirection;
            double circleDifference = difference;
            if (difference > 180)
                circleDifference = difference - 360;
            else if (difference < -180)
                circleDifference = difference + 360;
            double courseAzimuth;
            if (circleDifference < 0) {
                courseAzimuth = trueWindDirection - closestCourseAgainstWind;
                if (courseAzimuth < 0) {
                    courseAzimuth += 360;
                }
            } else {  //if circleDifference >= 0
                courseAzimuth = trueWindDirection + closestCourseAgainstWind;
                if (courseAzimuth > 360)
                    courseAzimuth -= 360;
            }
            setFollowedCourseAzimuth(courseAzimuth);
        }
    }

    public void setCourseAgainstWind(double apparentWindDirection) {
/*        double temp = abs(getCourseAzimuth() - windDirection);
        if(temp > 180)
            temp = 360 - temp;
        this.courseAgainstWind = temp;*/
        this.courseAgainstWind = abs(apparentWindDirection);
    }

    public void process(double sideForce, double thrustForce, Wind trueWind) {
        setAcceleration(thrustForce / mass);
        setVelocity(getVelocity() + getAcceleration() * Simulation.samplingPeriod);
        double deceleration = 100 * pow(getVelocity(), 2) / mass;
        setVelocity(getVelocity() - deceleration * Simulation.samplingPeriod);

        setAngleAcceleration(sideForce * distanceRudderFromCenterOfRotation / momentOfInertia);
        setAngleVelocity(getAngleVelocity() + getAngleAcceleration() * Simulation.samplingPeriod);
        double angleDeceleration = 5000 * pow(getAngleVelocity(), 2) / momentOfInertia;
        setAngleVelocity(getAngleVelocity() - angleDeceleration * Simulation.samplingPeriod);
        setCurrentCourseAzimuth(getCurrentCourseAzimuth() + getAngleVelocity() * Simulation.samplingPeriod);

        windIndicatorAtFoot.measureWind(trueWind, this, sail.getFootHeight());
        windIndicatorAtHead.measureWind(trueWind, this, sail.getHeadHeight());
        setCourseAgainstWind(windIndicatorAtFoot.getDirection() * 0.75 + windIndicatorAtHead.getDirection() * 0.25);
        setSpeedAgainstWind(windIndicatorAtFoot.getSpeed() * 0.75 + windIndicatorAtHead.getSpeed() * 0.25);
    }
}
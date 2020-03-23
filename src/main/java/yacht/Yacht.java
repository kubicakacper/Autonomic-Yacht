package yacht;

import lombok.*;
import simulation.Simulation;
import simulation.WindIndicator;
import yacht.rudder.RudderController;
import yacht.rudder.RudderEngine;
import yacht.rudder.RudderEngineController;
import yacht.sail.ReadSailCoefficients;
import yacht.sail.StatesOfSail;
import yacht.sail.car.Car;
import yacht.sail.car.CarEngine;
import yacht.sail.car.CarEngineController;
import yacht.sail.sheet.Sheet;
import yacht.sail.sheet.SheetEngine;
import yacht.sail.sheet.SheetEngineController;

import java.util.LinkedList;
import java.util.OptionalDouble;

import static java.lang.Math.*;

@Data
public class Yacht {

    private final double mass;  //e.g. 100
    private final double momentOfInertia;   //e.g. 400
    private final double distanceRudderFromCenterOfRotation;
    private final double closestCourseAgainstWind = 37;     // concerns TRUE wind
    Sail sail;
    Rudder rudder;
    private double requiredCourseAzimuth;
    private double courseAzimuth;
    private double courseAgainstWind;   // left:"-", right:"+".
    private double speedAgainstWind;    // apparent wind
    private double velocity;
    private double acceleration;
    private double angleVelocity;   // angle velocity of turn in basic SI ...; left:"-", right:"+".
    private double angleAcceleration;
    private WindIndicator windIndicatorAtFoot;
    private WindIndicator windIndicatorAtHead;

    public Yacht(double mass, double momentOfInertia, double distanceRudderFromCenterOfRotation,
                 double sailArea, double sailFootHeight, double sailHeadHeight, StatesOfSail stateOfSail,
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

        this.mass = mass;
        this.momentOfInertia = momentOfInertia;
        this.distanceRudderFromCenterOfRotation = distanceRudderFromCenterOfRotation;

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

    public void setRequiredCourseAzimuth(double requiredCourseAzimuth, double trueWindDirection) {
        this.requiredCourseAzimuth = requiredCourseAzimuth;
        double absDifference = abs(requiredCourseAzimuth - trueWindDirection);
        double circleAbsDifference = absDifference;
        if (absDifference > 180)
            circleAbsDifference = 360 - absDifference;
        if (circleAbsDifference >= closestCourseAgainstWind)
            setCourseAzimuth(requiredCourseAzimuth);
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
            setCourseAzimuth(courseAzimuth);
        }
    }

    public void setCourseAgainstWind(double apparentWindDirection) {
/*        double temp = abs(getCourseAzimuth() - windDirection);
        if(temp > 180)
            temp = 360 - temp;
        this.courseAgainstWind = temp;*/
        this.courseAgainstWind = abs(apparentWindDirection);
    }

    public void process(double sideForce, double thrustForce, double windSpeed, double windDirection) {
        setAcceleration(thrustForce / mass);
        setVelocity(getVelocity() + getAcceleration() * Simulation.samplingPeriod);
        double deceleration = 100 * pow(getVelocity(), 2) / mass;
        setVelocity(getVelocity() - deceleration * Simulation.samplingPeriod);

        setAngleAcceleration(sideForce * distanceRudderFromCenterOfRotation / momentOfInertia);
        setAngleVelocity(getAngleVelocity() + getAngleAcceleration() * Simulation.samplingPeriod);
        double angleDeceleration = 5000 * pow(getAngleVelocity(), 2) / momentOfInertia;
        setAngleVelocity(getAngleVelocity() - angleDeceleration * Simulation.samplingPeriod);

        windIndicatorAtFoot.process(windSpeed, windDirection, getVelocity(), getCourseAzimuth(), sail.getFootHeight());
        windIndicatorAtHead.process(windSpeed, windDirection, getVelocity(), getCourseAzimuth(), sail.getHeadHeight());
        setCourseAgainstWind(windIndicatorAtFoot.getDirection() * 0.75 + windIndicatorAtHead.getDirection() * 0.25);
        setSpeedAgainstWind(windIndicatorAtFoot.getSpeed() * 0.75 + windIndicatorAtHead.getSpeed() * 0.25);
    }

    @Data
    public class Sail {

        private final double area;
        private final double footHeight;
        private final double headHeight;
        private final double maxTrimAngle = 80.0;   // = Car.maxPositionInDegrees
        private final double maxTwistAngle = 30.0;
        protected double[] liftCoefficientArray; //lift coefficients depending on ANGLE OF ATTACK
        protected double[] dragCoefficientArray;
        SailController sailController;
        CarEngineController carEngineController;
        CarEngine carEngine;
        Car car;
        SheetEngineController sheetEngineController;
        SheetEngine sheetEngine;
        Sheet sheet;
        private StatesOfSail currentStateOfSail;   //port / starboard
        private double currentTrimAngle;   // in degrees    //left: "-"; right: "+".
        private double currentTwistAngle;  // in degrees
        private double currentHeadPosition; //in degrees    //trimAngle + twistAngle

        public Sail(double area, double footHeight, double headHeight, StatesOfSail stateOfSail,
                    double sailControllerProportionalCoefficientForTrim, double sailControllerProportionalCoefficientForTwist,
                    double carEngineControllerHysteresis, double carEngineControllerOffset,
                    double carEngineMaxVelocity,
                    double carDistanceFromMast,
                    double sheetEngineControllerHysteresis, double sheetEngineControllerOffset,
                    double sheetEngineMaxVelocity,
                    int sheetGearRatio) {

            this.area = area;
            if (headHeight < footHeight) {
                System.out.println("Head of sail must be higher than foot!");
                System.out.println("Their height is going to be exchanged.");
                double temp = headHeight;
                headHeight = footHeight;
                footHeight = temp;
            }
            this.footHeight = footHeight;
            this.headHeight = headHeight;
            this.currentStateOfSail = stateOfSail;

            this.liftCoefficientArray = new double[91];
            this.dragCoefficientArray = new double[91];

            ReadSailCoefficients.openFile("sailCoefficients");
            ReadSailCoefficients.readRecords(liftCoefficientArray, dragCoefficientArray);
            ReadSailCoefficients.closeFile();

            sailController = new SailController(sailControllerProportionalCoefficientForTrim, sailControllerProportionalCoefficientForTwist);

            carEngineController = new CarEngineController(carEngineControllerHysteresis, carEngineControllerOffset);
            carEngine = new CarEngine(carEngineMaxVelocity);
            car = new Car(carDistanceFromMast);

            sheetEngineController = new SheetEngineController(sheetEngineControllerHysteresis, sheetEngineControllerOffset);
            sheetEngine = new SheetEngine(sheetEngineMaxVelocity);
            sheet = new Sheet(sheetGearRatio);

        }

        public double getArea() {
            return area;
        }

        public double getFootHeight() {
            return footHeight;
        }

        public double getHeadHeight() {
            return headHeight;
        }

        public double getCurrentTrimAngle() {
            return currentTrimAngle;
        }

        void setCurrentTrimAngle(double currentTrimAngle) {
            if (abs(currentTrimAngle) <= maxTrimAngle)
                this.currentTrimAngle = currentTrimAngle;
            else
                System.out.println("Trim angle must be greater than " + -maxTwistAngle + " and lower than " + maxTrimAngle + " degrees!");
        }

        public double getCurrentTwistAngle() {
            return currentTwistAngle;
        }

        void setCurrentTwistAngle(double currentTwistAngle) {
            if (currentTwistAngle >= 0.0 && currentTwistAngle <= maxTwistAngle && currentTrimAngle + currentTwistAngle <= 90.0)
                this.currentTwistAngle = currentTwistAngle;
            else if (currentTrimAngle + currentTwistAngle > 90.0)
                System.out.println("Trim angle plus twist angle must be lower than 90 degrees!");
            else
                System.out.println("Twist angle must be positive and lower than " + maxTwistAngle + " degrees!");
        }

        public double getCurrentHeadPosition() {
            return currentHeadPosition;
        }

        void setCurrentHeadPosition(double currentTwistAngle, double currentTrimAngle) {
            this.currentHeadPosition = currentTrimAngle + currentTwistAngle;
        }

        public double countThrustForce() {
            setCurrentTrimAngle(car.getCurrentPositionInDegrees());   //left: "-"; right: "+".
            double sailLength = getHeadHeight() - getFootHeight();
            setCurrentTwistAngle(sqrt(sheet.getCurrentLengthOverMin()
                    * (2 * sailLength - sheet.getCurrentLengthOverMin())) / car.getDistanceFromMast());
            setCurrentHeadPosition(getCurrentTrimAngle(), getCurrentTwistAngle());
            int approxAngleOfAttack = (int) round((abs(windIndicatorAtFoot.getDirection()) - abs(getCurrentTrimAngle())) * 0.75 + abs(abs(windIndicatorAtHead.getDirection() - getCurrentHeadPosition())) * 0.25);
            if (approxAngleOfAttack < 0)
                approxAngleOfAttack = 0;
            double liftCoefficient = liftCoefficientArray[approxAngleOfAttack];
            double dragCoefficient = dragCoefficientArray[approxAngleOfAttack];

            return 0.5 * Simulation.airDensity * getArea() * pow(getSpeedAgainstWind(), 2)
                    * (sin(toRadians(getCourseAgainstWind())) * liftCoefficient - cos(toRadians(getCourseAgainstWind())) * dragCoefficient);
        }

        @Data
        public class SailController {    //SailController operates only on apparent wind!

            private final int timeTakenIntoAccountWhileCountingAverageWindInSeconds = 5;
            private final int[] trimAnglesForMaxThrust;
            private double proportionalCoefficientForTrim;
            private double proportionalCoefficientForTwist;
            private double windDirectionAtFoot;        //yacht reference frame
            private double windDirectionAtHead;        //yacht reference frame
            private LinkedList<Double> windDirectionAtFootHistory;
            private LinkedList<Double> windDirectionAtHeadHistory;
            private double averageWindDirectionAtFoot;      //reduces wind fluctuations
            private double averageWindDirectionAtHead;      //reduces wind fluctuations

            public SailController(double proportionalCoefficientForTrim, double proportionalCoefficientForTwist) {
                this.proportionalCoefficientForTrim = proportionalCoefficientForTrim;
                this.proportionalCoefficientForTwist = proportionalCoefficientForTwist;
                windDirectionAtFootHistory = new LinkedList<>();         //double[(int) (10/getSamplingPeriodInSeconds())];
                windDirectionAtHeadHistory = new LinkedList<>();
                trimAnglesForMaxThrust = new int[181];           //for angles from 0 to 180 degrees

                for (int i = 0; i < trimAnglesForMaxThrust.length; i++) {
                    double maxThrustAngle = 0;
                    for (int j = 0; j < liftCoefficientArray.length; j++) {
                        double temp = sin(toRadians(i)) * liftCoefficientArray[j] - cos(toRadians(i)) * dragCoefficientArray[j];
                        if (j == 0 || temp >= maxThrustAngle) {
                            trimAnglesForMaxThrust[i] = j;
                            maxThrustAngle = temp;
                        }// constant:  0.5 * Simulation.airDensity * outer.getArea() * outer.outer.getVelocity() are omitted
                    }
                    //    System.out.println(maxThrustAngle);
                }//SPRAWDŹ JAK WYGLĄDA WYNIK TEGO, CZY MA TO SENS, CZY ZMIANA KĄTA TRYMU BĘDZIE PŁYNNA
            }

            /*
            jacht otrzymuje kierunek wiatru od WindIndicator w jachtowym układzie odniesienia (-180 - +180)
            i przekazuje SailControllerowi
            */
            private double countAverageAtFoot(double newWindDirection) {
                if (!windDirectionAtFootHistory.isEmpty()) {
                    OptionalDouble averageWindDirection = windDirectionAtFootHistory.stream()
                            .mapToDouble(Double::doubleValue)
                            .average();
                    if (averageWindDirection.isPresent()) {
                        double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
                        if (newWindDirection > average + 180)
                            newWindDirection -= 360;
                        if (newWindDirection < average - 180)
                            newWindDirection += 360;
                    }
                }
                windDirectionAtFootHistory.addLast(newWindDirection);
                if (windDirectionAtFootHistory.size() > timeTakenIntoAccountWhileCountingAverageWindInSeconds / Simulation.samplingPeriod)
                    windDirectionAtFootHistory.removeFirst();
                OptionalDouble averageWindDirection = windDirectionAtFootHistory.stream()
                        .mapToDouble(Double::doubleValue)
                        .average();
                if (averageWindDirection.isPresent()) {
                    double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
                    if (average > 180)
                        average -= 360;
                    if (average < -180)
                        average += 360;
                    return average;
                } else
                    return 0.0;
            }

            private double countAverageAtHead(double newWindDirection) {
                if (!windDirectionAtHeadHistory.isEmpty()) {
                    OptionalDouble averageWindDirection = windDirectionAtHeadHistory.stream()
                            .mapToDouble(Double::doubleValue)
                            .average();
                    if (averageWindDirection.isPresent()) {
                        double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
                        if (newWindDirection > average + 180)
                            newWindDirection -= 360;
                        if (newWindDirection < average - 180)
                            newWindDirection += 360;
                    }
                }
                windDirectionAtHeadHistory.addLast(newWindDirection);
                if (windDirectionAtHeadHistory.size() > timeTakenIntoAccountWhileCountingAverageWindInSeconds / Simulation.samplingPeriod)
                    windDirectionAtHeadHistory.removeFirst();
                OptionalDouble averageWindDirection = windDirectionAtHeadHistory.stream()
                        .mapToDouble(Double::doubleValue)
                        .average();
                if (averageWindDirection.isPresent()) {
                    double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
                    if (average > 180)
                        average -= 360;
                    if (average < -180)
                        average += 360;
                    return average;
                } else
                    return 0.0;
            }

            //This function receives measured apparent wind and counts control variable, which is required sail trim
            //It uses coefficient arrays defined by Sail outer class
            int countRequiredTrim(double newWindDirectionAtFoot) {    //function call param: Yacht.WindIndicator.process().direction
                double newAverageWindDirection = countAverageAtFoot(newWindDirectionAtFoot);
                int windDirection = (int) abs(round(newAverageWindDirection));
                return trimAnglesForMaxThrust[windDirection];
            }

            //This function receives measured apparent wind and counts control variable, which is required sail twist
            int countRequiredTwist(double newWindDirectionAtHead) {    //function call param: Yacht.WindIndicator.process().direction
                double newAverageWindDirection = countAverageAtHead(newWindDirectionAtHead);
                int windDirection = (int) abs(round(newAverageWindDirection));
                return trimAnglesForMaxThrust[windDirection] - (int) getCurrentTrimAngle();
            }

            double countControlValueAtFoot(double newWindDirection, double measuredTrim) {
                return getProportionalCoefficientForTrim() * (countRequiredTrim(newWindDirection) - measuredTrim);
            }

            double countControlValueAtHead(double newWindDirection, double measuredTwist) {
                return getProportionalCoefficientForTwist() * (countRequiredTwist(newWindDirection) - measuredTwist);
            }

        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public class Rudder {

        private final double area;
        private final double maxAngle = 30.0;
        RudderController rudderController;
        RudderEngineController rudderEngineController;
        RudderEngine rudderEngine;
        private double currentAngle;

        public Rudder(double area,
                      double rudderControllerProportionalCoefficient, double rudderControllerIntegralCoefficient, double rudderControllerDerivativeCoefficient,
                      double rudderEngineControllerHysteresis, double rudderEngineControllerOffset,
                      double rudderEngineMaxVelocity) {
            this.area = area;
            rudderController = new RudderController(rudderControllerProportionalCoefficient, rudderControllerIntegralCoefficient, rudderControllerDerivativeCoefficient);
            rudderEngineController = new RudderEngineController(rudderEngineControllerHysteresis, rudderEngineControllerOffset);
            rudderEngine = new RudderEngine(rudderEngineMaxVelocity);
        }

        public void setCurrentAngle(double currentAngle) {
            if (abs(currentAngle) <= getMaxAngle())
                this.currentAngle = currentAngle;
            else if (currentAngle > getMaxAngle())
                this.currentAngle = getMaxAngle();
            else
                this.currentAngle = -getMaxAngle();
        }

        public double countSideForce(double angleVelocity) {//angle velocity in m/s^2 // + W PRAWO, - W LEWO
            setCurrentAngle(getCurrentAngle() + angleVelocity * Simulation.samplingPeriod); // function borders angle between -maxAngle & maxAngle
/*            if (getCurrentAngle() > getMaxAngle())
                setCurrentAngle(getMaxAngle());
            else if (getCurrentAngle() < -getMaxAngle())
                setCurrentAngle(-getMaxAngle());*/
            return 0.5 * Simulation.waterDensity * getArea() * pow(getVelocity(), 2) * 0.025 * getCurrentAngle();
        }
    }
}

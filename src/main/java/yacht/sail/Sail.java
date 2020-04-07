package yacht.sail;

import lombok.Data;
import simulation.Simulation;
import yacht.Yacht;
import yacht.sail.car.Car;
import yacht.sail.car.CarEngine;
import yacht.sail.car.CarEngineController;
import yacht.sail.sheet.Sheet;
import yacht.sail.sheet.SheetEngine;
import yacht.sail.sheet.SheetEngineController;

import java.util.Arrays;

import static java.lang.Math.*;

@Data
public class Sail {

    private final double area;
    private final double footHeight;
    private final double headHeight;
    private final double maxTrimAngle = 80.0;   // = Car.maxPositionInDegrees
    private final double maxTwistAngle = 10.0;
    public SailController sailController;
    public CarEngineController carEngineController;
    public CarEngine carEngine;
    public Car car;
    public SheetEngineController sheetEngineController;
    public SheetEngine sheetEngine;
    public Sheet sheet;
    protected double[] liftCoefficientArray; //lift coefficients depending on ANGLE OF ATTACK
    protected double[] dragCoefficientArray;
    private StatesOfSail currentStateOfSail;   //port / starboard
    private double currentTrimAngle;   // in degrees    //left: "-"; right: "+".
    private double currentTwistAngle;  // in degrees
    private double currentHeadPosition; //in degrees    //trimAngle + twistAngle

    public Sail() {
        this.liftCoefficientArray = new double[91];
        this.dragCoefficientArray = new double[91];

        ReadSailCoefficients.openFile("sailCoefficients.txt");
        ReadSailCoefficients.readRecords(liftCoefficientArray, dragCoefficientArray);
        ReadSailCoefficients.closeFile();

        area = 2;
        footHeight = 1;
        headHeight = 4;
        sailController = new SailController(this);
        carEngineController = new CarEngineController();
        carEngine = new CarEngine();
        car = new Car();
        sheetEngineController = new SheetEngineController();
        sheetEngine = new SheetEngine();
        sheet = new Sheet();
        currentStateOfSail = StatesOfSail.PORT;
    }

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

        liftCoefficientArray = new double[91];
        dragCoefficientArray = new double[91];

        ReadSailCoefficients.openFile("sailCoefficients.txt");
        ReadSailCoefficients.readRecords(liftCoefficientArray, dragCoefficientArray);
        ReadSailCoefficients.closeFile();

        System.out.println(Arrays.toString(liftCoefficientArray));

        sailController = new SailController(sailControllerProportionalCoefficientForTrim, sailControllerProportionalCoefficientForTwist, this);

        carEngineController = new CarEngineController(carEngineControllerHysteresis, carEngineControllerOffset);
        carEngine = new CarEngine(carEngineMaxVelocity);
        car = new Car(carDistanceFromMast);

        sheetEngineController = new SheetEngineController(sheetEngineControllerHysteresis, sheetEngineControllerOffset);
        sheetEngine = new SheetEngine(sheetEngineMaxVelocity);
        sheet = new Sheet(sheetGearRatio);

    }

    void setCurrentTrimAngle(double currentTrimAngle) {
        if (abs(currentTrimAngle) <= maxTrimAngle)
            this.currentTrimAngle = currentTrimAngle;
        else
            System.out.println("Trim angle must be greater than " + -maxTrimAngle + " and lower than " + maxTrimAngle + " degrees!");
    }

    void setCurrentTwistAngle(double currentTwistAngle) {
        if (currentTwistAngle >= 0.0 && currentTwistAngle <= maxTwistAngle && currentTrimAngle + currentTwistAngle <= 90.0)
            this.currentTwistAngle = currentTwistAngle;
        else if (currentTrimAngle + currentTwistAngle > 90.0)
            this.currentTwistAngle = 90 - currentTwistAngle;
        else if (currentTwistAngle < 0)
            this.currentTwistAngle = 0;
        else //if currentTwistAngle > max && currentTrimAngle + currentTwistAngle < 90.0
            this.currentTwistAngle = maxTwistAngle;
    }

    void setCurrentHeadPosition(double currentTwistAngle, double currentTrimAngle) {
        this.currentHeadPosition = abs(currentTrimAngle) + currentTwistAngle;
    }

    public double countThrustForce(Yacht yacht) {
        setCurrentTrimAngle(car.getCurrentPositionInDegrees());   //left: "-"; right: "+".
        double sailLength = getHeadHeight() - getFootHeight();
        setCurrentTwistAngle(toDegrees(atan2(sqrt(sheet.getCurrentLengthOverMin() * (2 * sailLength - sheet.getCurrentLengthOverMin())), car.getDistanceFromMast())));
        setCurrentHeadPosition(getCurrentTrimAngle(), getCurrentTwistAngle());
        int approxAngleOfAttack = (int) round((abs(yacht.windIndicatorAtFoot.getApparentWind().getDirection() + getCurrentTrimAngle())) * 0.75 + abs(abs(yacht.windIndicatorAtHead.getApparentWind().getDirection() + getCurrentHeadPosition())) * 0.25);
        if (approxAngleOfAttack > 90)
            approxAngleOfAttack = 180 - approxAngleOfAttack;
        if (approxAngleOfAttack < 0)
            approxAngleOfAttack = 0;
        double liftCoefficient = liftCoefficientArray[approxAngleOfAttack];
        double dragCoefficient = dragCoefficientArray[approxAngleOfAttack];
        return 0.5 * Simulation.airDensity * getArea() * pow(yacht.getSpeedAgainstWind(), 2)
                * (sin(toRadians(yacht.getCourseAgainstWind())) * liftCoefficient - cos(toRadians(yacht.getCourseAgainstWind())) * dragCoefficient);
    }


}


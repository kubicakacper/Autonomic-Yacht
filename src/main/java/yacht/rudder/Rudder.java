package yacht.rudder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import simulation.Simulation;
import yacht.Yacht;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Rudder {

    private final double area;
    private final double maxAngle = 30.0;
    public RudderController rudderController;
    public RudderEngineController rudderEngineController;
    public RudderEngine rudderEngine;
    private double currentAngle;

    public Rudder() {
        area = 0.5;
        rudderController = new RudderController();
        rudderEngineController = new RudderEngineController();
        rudderEngine = new RudderEngine();
    }

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

    public void countSideForce(double angleVelocity, Yacht yacht) {//angle velocity in m/s^2 // + W PRAWO, - W LEWO
        setCurrentAngle(getCurrentAngle() + angleVelocity * Simulation.samplingPeriod); // function borders angle between -maxAngle & maxAngle
        yacht.setSideForce(0.5 * Simulation.waterDensity * getArea() * pow(yacht.getVelocity(), 2) * 0.025 * getCurrentAngle());
    }
}

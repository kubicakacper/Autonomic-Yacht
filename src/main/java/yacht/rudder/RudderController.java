package yacht.rudder;

import lombok.Data;
import simulation.Simulation;
import yacht.Yacht;

import static java.lang.Math.abs;

@Data
public class RudderController {

    private double proportionalCoefficient;
    private double integralCoefficient;
    private double derivativeCoefficient;
    private double errorIntegral;
    private double preError;
    private double requiredAngle;
    private double currentControlValue;

    public RudderController(double proportionalCoefficient, double integralCoefficient, double derivativeCoefficient) {
        this.proportionalCoefficient = proportionalCoefficient;
        this.integralCoefficient = integralCoefficient;
        this.derivativeCoefficient = derivativeCoefficient;
    }

    public RudderController() {
        proportionalCoefficient = 5;
        integralCoefficient = 1;
        derivativeCoefficient = 20;
    }

    public void countRequiredAngle(Yacht yacht) {
        double error = yacht.getFollowedCourseAzimuth() - yacht.getCurrentCourseAzimuth();
        if (error > 180)
            error -= 360;
        else if (error < -180)
            error += 360;
        errorIntegral *= 1 - Simulation.samplingPeriod / 20;
        errorIntegral += error * Simulation.samplingPeriod;
        double controlValue = getProportionalCoefficient() * error
                + getIntegralCoefficient() * getErrorIntegral() * Simulation.samplingPeriod
                + getDerivativeCoefficient() * (error - getPreError()) / Simulation.samplingPeriod;
        setRequiredAngle(controlValue / abs(yacht.getVelocity()));
        setPreError(error);
    }

    public void countControlValue(Yacht yacht) {
        countRequiredAngle(yacht);
        setCurrentControlValue(getRequiredAngle() - yacht.rudder.getCurrentAngle());
    }
}
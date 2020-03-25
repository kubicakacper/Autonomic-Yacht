package yacht.rudder;

import lombok.Data;
import simulation.Simulation;

@Data
public class RudderController {

    private double proportionalCoefficient;
    private double integralCoefficient;
    private double derivativeCoefficient;
    private double errorIntegral;           //initialized as 0.0
    private double preError;                //initialized as 0.0

    public RudderController(double proportionalCoefficient, double integralCoefficient, double derivativeCoefficient) {
        this.proportionalCoefficient = proportionalCoefficient;
        this.integralCoefficient = integralCoefficient;
        this.derivativeCoefficient = derivativeCoefficient;
    }

    public RudderController() {
        proportionalCoefficient = 1;
        integralCoefficient = 0.1;
        derivativeCoefficient = 0.5;
    }

    public double countAndReturnControlValue(double requiredCourse, double measuredCourse) {
        double error = requiredCourse - measuredCourse;
        if (error > 180)
            error -= 360;
        else if (error < -180)
            error += 360;
        errorIntegral *= 1 - Simulation.samplingPeriod / 100;
        errorIntegral += error * Simulation.samplingPeriod;
        double controlValue = getProportionalCoefficient() * error
                + getIntegralCoefficient() * getErrorIntegral() * Simulation.samplingPeriod
                + getDerivativeCoefficient() * (error - getPreError()) / Simulation.samplingPeriod;
        setPreError(error);
        return controlValue;
    }
}


//IM SZYBCIEJ PŁYNIESZ TYM MNIEJ WYCHYLAJ PŁETWĘ!!!!!
//mój drogi, to jest PID, więc nikogo nie interesuje jak wychylona jest płetwa, liczy się tylko to czy wychylać ją bardziej czy mniej
//jeśli chcesz reagować na zmianę prędkości, to zrób feedforward, ale czy trzeba tak utrudniać?

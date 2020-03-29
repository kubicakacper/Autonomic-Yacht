package yacht.rudder;

import lombok.Data;
import simulation.Simulation;

@Data
public class RudderController {

    private double proportionalCoefficient;
    private double integralCoefficient;
    private double derivativeCoefficient;
    private double errorIntegral;
    private double preError;
    private double currentControlValue;

    public RudderController(double proportionalCoefficient, double integralCoefficient, double derivativeCoefficient) {
        this.proportionalCoefficient = proportionalCoefficient;
        this.integralCoefficient = integralCoefficient;
        this.derivativeCoefficient = derivativeCoefficient;
    }

    public RudderController() {
        proportionalCoefficient = 0.01;
        integralCoefficient = 0;
        derivativeCoefficient = 0;
    }

    public void countControlValue(double requiredCourse, double measuredCourse) {
        double error = requiredCourse - measuredCourse;
        if (error > 180)
            error -= 360;
        else if (error < -180)
            error += 360;
        errorIntegral *= 1 - Simulation.samplingPeriod / 100;
        errorIntegral += error * Simulation.samplingPeriod;
        setCurrentControlValue(getProportionalCoefficient() * error
                + getIntegralCoefficient() * getErrorIntegral() * Simulation.samplingPeriod
                + getDerivativeCoefficient() * (error - getPreError()) / Simulation.samplingPeriod);
        setPreError(error);
    }
}


//IM SZYBCIEJ PŁYNIESZ TYM MNIEJ WYCHYLAJ PŁETWĘ!!!!!
//mój drogi, to jest PID, więc nikogo nie interesuje jak wychylona jest płetwa, liczy się tylko to czy wychylać ją bardziej czy mniej
//jeśli chcesz reagować na zmianę prędkości, to zrób feedforward, ale czy trzeba tak utrudniać?

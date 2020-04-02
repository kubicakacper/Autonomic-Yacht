package simulation;

import lombok.EqualsAndHashCode;
import lombok.ToString;

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
}

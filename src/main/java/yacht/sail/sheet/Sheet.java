package yacht.sail.sheet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import simulation.Simulation;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Sheet {    //klasa należy do Sail

    private final double maxLengthOverMin = 0.2;
    private final int gearRatio;
    private double currentLengthOverMin;    // minimal length of the line (sheet) is when boom is oriented horizontally - in this position boom is at minimal level;
    // current length over min is the difference between current height and minimal height (measured above the car).

    public void setCurrentLengthOverMin(double newLengthOverMinInMeters) {
        if (newLengthOverMinInMeters >= 0 && newLengthOverMinInMeters <= maxLengthOverMin)
            this.currentLengthOverMin = newLengthOverMinInMeters;
        else if (newLengthOverMinInMeters < 0)
            this.currentLengthOverMin = 0;
        else
            this.currentLengthOverMin = maxLengthOverMin;
    }

    public double countLengthOverMin(double engineVelocity) {
        setCurrentLengthOverMin(getCurrentLengthOverMin() + engineVelocity * Simulation.samplingPeriod / getGearRatio());
        return getCurrentLengthOverMin();
    }
}

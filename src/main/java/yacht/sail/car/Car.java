package yacht.sail.car;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import simulation.Simulation;

import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Car {  //belongs to Sail

    private final double distanceFromMast;      //in meters
    private final double maxAnglePosition = 80.0;
    private double currentPositionInDegrees;     //left: "-"; right: "+".

    public Car() {
        distanceFromMast = 0.5;
    }

    public void setCurrentPositionInDegrees(double newPositionInDegrees) {
        if (abs(newPositionInDegrees) <= maxAnglePosition)
            this.currentPositionInDegrees = newPositionInDegrees;
        else if (newPositionInDegrees > maxAnglePosition)
            this.currentPositionInDegrees = maxAnglePosition;
        else
            this.currentPositionInDegrees = -maxAnglePosition;
    }

    public void countPositionInDegrees(double engineVelocity) {    //if car goes left, velocity must be negative!
        double angleVelocityInRadians = engineVelocity / distanceFromMast;
        setCurrentPositionInDegrees(getCurrentPositionInDegrees() + toDegrees(angleVelocityInRadians) * Simulation.samplingPeriod);
    }
}

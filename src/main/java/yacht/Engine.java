package yacht;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static java.lang.Math.abs;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Engine {

    private final double maxVelocity;
    private double currentVelocity;

    public Engine(double maxVelocity) {
        if (maxVelocity > 0)
            this.maxVelocity = maxVelocity;
        else {
            this.maxVelocity = 1;
            System.out.println("Engine maximum velocity must be greater than 0!");
        }
    }

    public void setCurrentVelocity(double currentVelocity) {
        if (abs(currentVelocity) <= getMaxVelocity())
            this.currentVelocity = currentVelocity;
        else {
            System.out.println("Engine is not strong enough to work so fast.");
            System.out.println("Engine maximum velocity is " + getMaxVelocity());
            this.currentVelocity = getMaxVelocity();
        }
    }
}

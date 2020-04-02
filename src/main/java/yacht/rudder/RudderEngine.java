package yacht.rudder;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yacht.Engine;

@ToString
@EqualsAndHashCode(callSuper = true)
public class RudderEngine extends Engine {

    public RudderEngine(double maxVelocity) {
        super(maxVelocity);
    }

    public RudderEngine() {
        super(20);
    }

    public double setCurrentAngleVelocity(StatesOfRudderEngine requiredStateOfRudderEngine) { //converts engine power to force
        switch (requiredStateOfRudderEngine) {
            case LEFT:
                super.setCurrentVelocity(super.getMaxVelocity() * (-1));
                break;
            case STAND_BY:
                super.setCurrentVelocity(0.0);
                break;
            case RIGHT:
                super.setCurrentVelocity(super.getMaxVelocity() * 1);
                break;
            default:
                System.out.println("No such state of rudder engine!");
        }
        return super.getCurrentVelocity();  // in degrees/sec.
    }

}
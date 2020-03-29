package yacht.sail.car;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yacht.Engine;

@ToString
@EqualsAndHashCode(callSuper = true)
public class CarEngine extends Engine {     //trim  //belongs to Sail

    public CarEngine(double maxVelocity) {
        super(maxVelocity);
    }

    public CarEngine() {
        super(0.4);
    }

    //while tacking or gybing, CarEngineController is off (see TO_PORT or TO_STARBOARD case)
    public void setCurrentVelocity(StatesOfCarEngine StateOfCarEngine) {
        switch (StateOfCarEngine) {
            case LEFT_FAST:
                super.setCurrentVelocity(super.getMaxVelocity() * (-1));
                break;
            case LEFT:
                super.setCurrentVelocity(super.getMaxVelocity() * (-0.25));
                break;
            case STAND_BY:
                super.setCurrentVelocity(0.0);
                break;
            case RIGHT:
                super.setCurrentVelocity(super.getMaxVelocity() * 0.25);
                break;
            case RIGHT_FAST:
                super.setCurrentVelocity(super.getMaxVelocity() * 1);
                break;
            default:
                System.out.println("No such state of car engine!");
        }
    }
}
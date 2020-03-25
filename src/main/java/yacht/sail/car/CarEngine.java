package yacht.sail.car;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yacht.Engine;
import yacht.sail.StatesOfSail;

@ToString
@EqualsAndHashCode(callSuper = true)
public class CarEngine extends Engine {     //trim  //belongs to Sail

    public CarEngine(double maxVelocity) {
        super(maxVelocity);
    }

    //while tacking or gybing, CarEngineController is off (see TO_PORT or TO_STARBOARD case)
    public double setCurrentVelocity(StatesOfCarEngine requiredStateOfCarEngine, StatesOfSail stateOfSail) {
        switch (stateOfSail) {
            case STARBOARD:
                switch (requiredStateOfCarEngine) { //left tack
                    case SHEET_IN_FAST:
                        super.setCurrentVelocity(super.getMaxVelocity() * (-1));
                        break;
                    case SHEET_IN_SLOW:
                        super.setCurrentVelocity(super.getMaxVelocity() * (-0.25));
                        break;
                    case STAND_BY:
                        super.setCurrentVelocity(0.0);
                        break;
                    case SHEET_OUT_SLOW:
                        super.setCurrentVelocity(super.getMaxVelocity() * 0.25);
                        break;
                    case SHEET_OUT_FAST:
                        super.setCurrentVelocity(super.getMaxVelocity() * 1);
                        break;
                    default:
                        System.out.println("No such state of car engine!");
                }
                break;
            case PORT:
                switch (requiredStateOfCarEngine) {
                    case SHEET_IN_FAST:
                        super.setCurrentVelocity(super.getMaxVelocity() * 1);
                        break;
                    case SHEET_IN_SLOW:
                        super.setCurrentVelocity(super.getMaxVelocity() * 0.25);
                        break;
                    case STAND_BY:
                        super.setCurrentVelocity(0.0);
                        break;
                    case SHEET_OUT_SLOW:
                        super.setCurrentVelocity(super.getMaxVelocity() * (-0.25));
                        break;
                    case SHEET_OUT_FAST:
                        super.setCurrentVelocity(super.getMaxVelocity() * (-1));
                        break;
                    default:
                        System.out.println("No such state of car engine!");
                }
                break;
            case TO_STARBOARD:
                super.setCurrentVelocity(super.getMaxVelocity() * 1);
                break;
            case TO_PORT:
                super.setCurrentVelocity(super.getMaxVelocity() * (-1));
                break;
            default:
                System.out.println("No such state of sail!");
        }
        return super.getCurrentVelocity();
    }
}

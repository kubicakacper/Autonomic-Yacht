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

    public double setCurrentVelocity(StatesOfCarEngine requiredStateOfCarEngine, StatesOfSail stateOfSail) {
        if (stateOfSail == StatesOfSail.STARBOARD) {
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
        } else if (stateOfSail == StatesOfSail.PORT) {
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
        } else {
            System.out.println("Sail state is changing - the yacht is turning.");
            System.out.println("It is not possible to maneuver the car engine!");
        }
        return super.getCurrentVelocity();
    }
}

package yacht.sail.sheet;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yacht.Engine;

@ToString
@EqualsAndHashCode(callSuper = true)
public class SheetEngine extends Engine {    //twist

    public SheetEngine(double maxVelocity) {
        super(maxVelocity);
    }

    public SheetEngine() {
        super(0.0005);
    }

    public void setCurrentVelocity(StatesOfSheetEngine requiredStateOfSheetEngine) {
        switch (requiredStateOfSheetEngine) {
            case HAUL:
                super.setCurrentVelocity(super.getMaxVelocity() * (-1));
                break;
            case STAND_BY:
                super.setCurrentVelocity(0.0);
                break;
            case EASE:
                super.setCurrentVelocity(super.getMaxVelocity() * 1);
                break;
            default:
                System.out.println("No such state of sheet engine!");
        }
    }
}
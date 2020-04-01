package yacht.sail.car;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import yacht.sail.StatesOfSail;

@Getter
@ToString
@EqualsAndHashCode
public class CarEngineController {

    private double hysteresis;
    private double offset;
    private StatesOfCarEngine currentStateOfEngine;

    public CarEngineController(double hysteresis, double offset) {
        this.hysteresis = hysteresis;
        this.offset = offset;
        currentStateOfEngine = StatesOfCarEngine.STAND_BY;
    }

    public CarEngineController() {
        hysteresis = 0.05;
        offset = 0.1;
        currentStateOfEngine = StatesOfCarEngine.STAND_BY;
    }

    public void setHysteresis(double hysteresis) {
        if (hysteresis >= 0)
            this.hysteresis = hysteresis;
        else
            System.out.println("Hysteresis must be positive!");
    }

    public void setOffset(double offset) {
        if (hysteresis >= 0)
            this.offset = offset;
        else
            System.out.println("Offset must be positive!");
    }

    public void setStateOfEngine(double controlValue, StatesOfSail stateOfSail) {
        switch (stateOfSail) {
            case TO_STARBOARD:
                currentStateOfEngine = StatesOfCarEngine.RIGHT_FAST;
                break;
            case TO_PORT:
                currentStateOfEngine = StatesOfCarEngine.LEFT_FAST;
                break;
            case STARBOARD:
            case PORT:
                switch (currentStateOfEngine) {
                    case LEFT_FAST:
                        currentStateOfEngine = StatesOfCarEngine.LEFT;
                        break;
                    case LEFT:
                        if (controlValue > -(offset - hysteresis))
                            currentStateOfEngine = StatesOfCarEngine.STAND_BY;
                        break;
                    case STAND_BY:
                        if (controlValue < -(offset + hysteresis))
                            currentStateOfEngine = StatesOfCarEngine.LEFT;
                        else if (controlValue > offset + hysteresis)
                            currentStateOfEngine = StatesOfCarEngine.RIGHT;
                        break;
                    case RIGHT:
                        if (controlValue < offset - hysteresis)
                            currentStateOfEngine = StatesOfCarEngine.STAND_BY;
                        break;
                    case RIGHT_FAST:
                        currentStateOfEngine = StatesOfCarEngine.RIGHT;
                        break;
                    default:
                        System.out.println("No such state of car engine!");
                }
                break;
            default:
                System.out.println("No such state of sail!");
        }
    }
}

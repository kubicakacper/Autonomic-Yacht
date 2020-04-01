package yacht.rudder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class RudderEngineController {

    private double hysteresis;
    private double offset;
    private StatesOfRudderEngine currentStateOfRudderEngine;

    public RudderEngineController(double hysteresis, double offset) {
        this.hysteresis = hysteresis;
        this.offset = offset;
        currentStateOfRudderEngine = StatesOfRudderEngine.STAND_BY;
    }

    public RudderEngineController() {
        hysteresis = 0.5;
        offset = 2;
        currentStateOfRudderEngine = StatesOfRudderEngine.STAND_BY;
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

    public void setStateOfEngine(double controlVariable) {
        switch (currentStateOfRudderEngine) {
            case LEFT:
                if (controlVariable > -(offset - hysteresis))
                    currentStateOfRudderEngine = StatesOfRudderEngine.STAND_BY;
                break;
            case STAND_BY:
                if (controlVariable < -(offset + hysteresis))
                    currentStateOfRudderEngine = StatesOfRudderEngine.LEFT;
                else if (controlVariable > offset + hysteresis)
                    currentStateOfRudderEngine = StatesOfRudderEngine.RIGHT;
                break;
            case RIGHT:
                if (controlVariable < offset - hysteresis)
                    currentStateOfRudderEngine = StatesOfRudderEngine.STAND_BY;
                break;
            default:
                System.out.println("No such state of rudder engine!");
        }
    }
}

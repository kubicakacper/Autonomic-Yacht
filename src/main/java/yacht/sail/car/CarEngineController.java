package yacht.sail.car;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class CarEngineController {

    private double hysteresis;
    private double offset;
    private StatesOfCarEngine currentStateOfCarEngine;

    public CarEngineController(double hysteresis, double offset) {
        this.hysteresis = hysteresis;
        this.offset = offset;
        currentStateOfCarEngine = StatesOfCarEngine.STAND_BY;
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

    public StatesOfCarEngine setStateOfEngine(double controlVariable) {
        switch (currentStateOfCarEngine) {
            case SHEET_IN_SLOW:
                if (controlVariable > -(offset - hysteresis))
                    currentStateOfCarEngine = StatesOfCarEngine.STAND_BY;
                break;
            case STAND_BY:
                if (controlVariable < -(offset + hysteresis))
                    currentStateOfCarEngine = StatesOfCarEngine.SHEET_IN_SLOW;
                else if (controlVariable > offset + hysteresis)
                    currentStateOfCarEngine = StatesOfCarEngine.SHEET_OUT_SLOW;
                break;
            case SHEET_OUT_SLOW:
                if (controlVariable < offset - hysteresis)
                    currentStateOfCarEngine = StatesOfCarEngine.STAND_BY;
                break;
            default:
                System.out.println("No such state of rudder engine!");
        }
        return currentStateOfCarEngine;
    }
}

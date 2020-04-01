package yacht.sail.sheet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class SheetEngineController {

    private double hysteresis;
    private double offset;
    private StatesOfSheetEngine currentStateOfEngine;

    public SheetEngineController(double hysteresis, double offset) {
        this.hysteresis = hysteresis;
        this.offset = offset;
        currentStateOfEngine = StatesOfSheetEngine.STAND_BY;
    }

    public SheetEngineController() {
        hysteresis = 0.05;
        offset = 0.1;
        currentStateOfEngine = StatesOfSheetEngine.STAND_BY;
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

    public void setStateOfEngine(double controlValue) {
        switch (currentStateOfEngine) {
            case HAUL:
                if (controlValue > -(offset - hysteresis))
                    currentStateOfEngine = StatesOfSheetEngine.STAND_BY;
                break;
            case STAND_BY:
                if (controlValue < -(offset + hysteresis))
                    currentStateOfEngine = StatesOfSheetEngine.HAUL;
                else if (controlValue > offset + hysteresis)
                    currentStateOfEngine = StatesOfSheetEngine.EASE;
                break;
            case EASE:
                if (controlValue < offset - hysteresis)
                    currentStateOfEngine = StatesOfSheetEngine.STAND_BY;
                break;
            default:
                System.out.println("No such state of rudder engine!");
        }
    }
}

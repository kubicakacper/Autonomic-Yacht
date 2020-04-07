package simulation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.Math.abs;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wind {

    private double speed;           //in m/s    //in case of true wind: speed at water level
    private double direction;       //as azimuth, in degrees

    public void setSpeed(double speed) {
        if (speed < 0) {
            speed = abs(speed);
            setDirection(getDirection() + 180);   //tu pewnie null ppointer bo nie ma Direction jeszcze przy konstruktorze
        }
        this.speed = speed;
    }

    public void setDirection(double direction) {
        while (direction < 0)
            direction += 360;
        while (direction >= 360)
            direction -= 360;
        this.direction = direction;
    }
}
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
    private double azimuthDirection;       //as azimuth, in degrees
    private double relativeDirection = 0;       //as azimuth, in degrees

    public void setSpeed(double speed) {
        if (speed < 0) {
            speed = abs(speed);
            setAzimuthDirection(getAzimuthDirection() + 180);
            setRelativeDirection(getRelativeDirection() + 180);
        }
        this.speed = speed;
    }

    public void setAzimuthDirection(double direction) {
        while (direction < 0)
            direction += 360;
        while (direction >= 360)
            direction -= 360;
        this.azimuthDirection = direction;
    }

    public void setRelativeDirection(double direction) {
        while (direction < -180)
            direction += 360;
        while (direction > 180)
            direction -= 360;
        this.relativeDirection = direction;
    }
}
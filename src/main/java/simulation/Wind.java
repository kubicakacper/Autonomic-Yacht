package simulation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class Wind {

    private double speed;           //in m/s    //in case of true wind: speed at water level
    private double direction;       //as azimuth, in degrees

}

//later: wind_change...
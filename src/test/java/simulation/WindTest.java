package simulation;

import org.junit.Before;
import org.junit.Test;
import yacht.Yacht;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class WindTest {

    SecureRandom random = new SecureRandom();
    Wind trueWind;
    WindIndicator windIndicator = new WindIndicator();
    Yacht yacht = new Yacht();

    @Before
    public void before() {
        trueWind = new Wind(random.nextDouble() * 20, random.nextInt(360), 0);
    }

    @Test
    public void should_count_wind_gradient_correctly() {
        //Given
        double height7 = 7;                                 //expected gradient: *1.6
        double heightRandom = random.nextDouble() * 16 + 15;  //expected gradient: [1.8, 2.0]
        WindIndicator windIndicatorRandom = new WindIndicator();
        //When
        windIndicator.measureWind(trueWind, yacht, height7);
        windIndicatorRandom.measureWind(trueWind, yacht, heightRandom);
        //Then
        assertEquals(windIndicator.getApparentWind().getSpeed(), trueWind.getSpeed() * 1.6, 0.01);
        if (windIndicator.getApparentWind().getRelativeDirection() >= 0)
            assertEquals(windIndicator.getApparentWind().getRelativeDirection(), trueWind.getAzimuthDirection(), 0.01);
        else
            assertEquals(windIndicator.getApparentWind().getRelativeDirection() + 360, trueWind.getAzimuthDirection(), 0.01);

        assertTrue(windIndicatorRandom.getApparentWind().getSpeed() > trueWind.getSpeed() * 1.8 && windIndicatorRandom.getApparentWind().getSpeed() < trueWind.getSpeed() * 2.0);
        if (windIndicatorRandom.getApparentWind().getRelativeDirection() >= 0)
            assertEquals(windIndicatorRandom.getApparentWind().getRelativeDirection(), trueWind.getAzimuthDirection(), 0.01);
        else
            assertEquals(windIndicatorRandom.getApparentWind().getRelativeDirection() + 360, trueWind.getAzimuthDirection(), 0.01);
    }

}

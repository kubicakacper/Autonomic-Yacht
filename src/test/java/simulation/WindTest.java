package simulation;

import org.junit.Before;
import org.junit.Test;
import yacht.Yacht;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class WindTest {

    Random random = new SecureRandom();
    Wind trueWind;
    WindIndicator windIndicator;
    Yacht yacht;

    @Before
    public void before() {
        trueWind = new Wind(random.nextDouble() * 20, random.nextInt(360));
        windIndicator = new WindIndicator();
        yacht = new Yacht();
    }

    @Test
    public void should_count_wind_gradient_correctly() {
        //Given
        double height7 = 7;                                 //expected gradient: *1.6
        double heightRandom = random.nextDouble() * 16 + 15;  //expected gradient: [1.8, 2.0]
        //When
        Wind wind7 = windIndicator.measureWind(trueWind, yacht, height7);
        Wind windRandom = windIndicator.measureWind(trueWind, yacht, heightRandom);
        //Then
        assertEquals(wind7.getSpeed(), trueWind.getSpeed() * 1.6, 0.01);
        if (wind7.getDirection() > 0)
            assertEquals(wind7.getDirection(), trueWind.getDirection(), 0.01);
        else
            assertEquals(wind7.getDirection() + 360, trueWind.getDirection(), 0.01);

        assertTrue(windRandom.getSpeed() > trueWind.getSpeed() * 1.8 && windRandom.getSpeed() < trueWind.getSpeed() * 2.0);
        if (windRandom.getDirection() > 0) {
            assertEquals(windRandom.getDirection(), trueWind.getDirection(), 0.01);
        } else {
            assertEquals(windRandom.getDirection() + 360, trueWind.getDirection(), 0.01);
        }
    }

}

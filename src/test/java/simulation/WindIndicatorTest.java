package simulation;

import org.junit.Before;
import org.junit.Test;
import yacht.Yacht;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WindIndicatorTest {

    Wind trueWind;
    Yacht yacht = new Yacht();    //in case of Builder: yacht = new Yacht.builder().velocity(10)...build()
    WindIndicator windIndicator = new WindIndicator();
    SecureRandom random = new SecureRandom();

    @Before
    public void before() {
        trueWind = new Wind(random.nextDouble() * 20, random.nextInt(360));
        yacht.setVelocity(random.nextDouble() * 6);
    }

    @Test
    public void should_count_wind_correctly_INIRONS() {
        //Given
        yacht.setFollowedCourseAzimuth(trueWind.getDirection());
        //When
        Wind apparentWind = windIndicator.measureWind(trueWind, yacht, 0);
        //Then
        assertEquals(apparentWind.getSpeed(), trueWind.getSpeed() + yacht.getVelocity(), 0.01);
        assertEquals(trueWind.getDirection(), yacht.getFollowedCourseAzimuth(), 0.01);
        assertEquals(apparentWind.getDirection(), 0, 0.01);
    }

    @Test
    public void should_count_wind_correctly_INTHEBEAM() {
        //Given
        yacht.setFollowedCourseAzimuth(trueWind.getDirection() + 90);
        //When
        Wind apparentWind = windIndicator.measureWind(trueWind, yacht, 0);
        //Then
        assertEquals(apparentWind.getSpeed(), Math.sqrt(Math.pow(trueWind.getSpeed(), 2) + Math.pow(yacht.getVelocity(), 2)), 0.01);
    }

    @Test
    public void should_count_wind_correctly_INTHERUN() {
        //Given
        yacht.setFollowedCourseAzimuth(trueWind.getDirection() + 180);
        //When
        Wind apparentWind = windIndicator.measureWind(trueWind, yacht, 0);
        //Then
        assertEquals(apparentWind.getSpeed(), Math.abs(trueWind.getSpeed() - yacht.getVelocity()), 0.01);
        assertEquals(trueWind.getDirection(), (yacht.getFollowedCourseAzimuth() + 180) % 360, 0.01);
        apparentWind.setDirection(Math.round(apparentWind.getDirection()));
        assertTrue(apparentWind.getDirection() == 0 || apparentWind.getDirection() == 180);
    }
}

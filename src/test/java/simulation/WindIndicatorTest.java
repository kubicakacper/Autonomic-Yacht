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
        trueWind = new Wind(random.nextDouble() * 20, random.nextInt(360), 0);
        yacht.setVelocity(random.nextDouble() * 6);
    }

    @Test
    public void should_count_wind_correctly_INIRONS() {
        //Given
        yacht.setFollowedCourseAzimuth(trueWind.getAzimuthDirection());
        //When
        windIndicator.measureWind(trueWind, yacht, 0);
        //Then
        assertEquals(windIndicator.getApparentWind().getSpeed(), trueWind.getSpeed() + yacht.getVelocity(), 0.01);
        assertEquals(trueWind.getAzimuthDirection(), yacht.getFollowedCourseAzimuth(), 0.01);
        assertEquals(windIndicator.getApparentWind().getRelativeDirection(), 0, 0.01);
    }

    @Test
    public void should_count_wind_correctly_INTHEBEAM() {
        //Given
        yacht.setFollowedCourseAzimuth(trueWind.getAzimuthDirection() + 90);
        //When
        windIndicator.measureWind(trueWind, yacht, 0);
        //Then
        assertEquals(windIndicator.getApparentWind().getSpeed(), Math.sqrt(Math.pow(trueWind.getSpeed(), 2) + Math.pow(yacht.getVelocity(), 2)), 0.01);
    }

    @Test
    public void should_count_wind_correctly_INTHERUN() {
        //Given
        yacht.setFollowedCourseAzimuth(trueWind.getAzimuthDirection() + 180);
        //When
        windIndicator.measureWind(trueWind, yacht, 0);
        //Then
        assertEquals(windIndicator.getApparentWind().getSpeed(), Math.abs(trueWind.getSpeed() - yacht.getVelocity()), 0.01);
        assertEquals(trueWind.getAzimuthDirection(), (yacht.getFollowedCourseAzimuth() + 180) % 360, 0.01);
        windIndicator.getApparentWind().setRelativeDirection(Math.round(windIndicator.getApparentWind().getRelativeDirection()));
        assertTrue(windIndicator.getApparentWind().getRelativeDirection() == 0 || windIndicator.getApparentWind().getRelativeDirection() == 180);
    }
}

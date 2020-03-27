package yacht;

import org.junit.Before;
import org.junit.Test;
import simulation.Wind;
import yacht.sail.StatesOfSail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TackAndGybeTest {

    Yacht yacht = new Yacht();
    Wind trueWind = new Wind();

    @Before
    public void before() {
        yacht.setCourseAzimuth(0);
    }

    @Test
    public void should_stay_on_port_when_wind_is_60() {
        //Given
        trueWind.setDirection(60);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.PORT);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.PORT);
    }

    @Test
    public void should_stay_on_starboard_when_sind_is_300() {
        //Given
        trueWind.setDirection(300);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.STARBOARD);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.STARBOARD);
    }

    @Test
    public void should_change_to_starboard_when_wind_is_300() {
        //Given
        trueWind.setDirection(300);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.PORT);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.TO_STARBOARD);
    }

    @Test
    public void should_change_to_port_when_wind_is_60() {
        //Given
        trueWind.setDirection(60);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.STARBOARD);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.TO_PORT);
    }

    @Test
    public void should_stay_to_starboard_when_car_is_5() {
        //Given
        yacht.sail.car.setCurrentPositionInDegrees(5);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.TO_STARBOARD);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.TO_STARBOARD);
    }

    @Test
    public void should_stay_to_port_when_car_is_m50() {
        //Given
        yacht.sail.car.setCurrentPositionInDegrees(-50);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.TO_PORT);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.TO_PORT);
    }

    @Test
    public void should_set_starboard_when_car_is_m15() {
        //Given
        yacht.sail.car.setCurrentPositionInDegrees(-15);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.TO_STARBOARD);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.STARBOARD);
    }

    @Test
    public void should_set_port_when_car_is_15() {
        //Given
        yacht.sail.car.setCurrentPositionInDegrees(15);
        yacht.sail.setCurrentStateOfSail(StatesOfSail.TO_PORT);
        //When
        yacht.sail.sailController.countRequiredTrim(trueWind.getDirection());
        //Then
        assertSame(yacht.sail.getCurrentStateOfSail(), StatesOfSail.PORT);
    }

    @Test
    public void should_set_carEngineVelocity_to_m1_when_to_port() {
        //Given
        yacht.sail.setCurrentStateOfSail(StatesOfSail.TO_PORT);
        //When
        yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfCarEngine(), yacht.sail.getCurrentStateOfSail());
        //Then
        assertEquals(yacht.sail.carEngine.getCurrentVelocity(), yacht.sail.carEngine.getMaxVelocity() * (-1), 0.01);
    }
}

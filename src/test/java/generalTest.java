import org.junit.Before;
import org.junit.Test;
import simulation.Simulation;
import simulation.Wind;
import yacht.Yacht;

import java.security.SecureRandom;

public class generalTest {
    SecureRandom random = new SecureRandom();
    Yacht yacht = new Yacht();
    Wind trueWind = new Wind();


    @Before
    public void before() {
//        trueWind.setSpeed(4);
//        trueWind.setDirection(300);
//        yacht.setRequiredCourseAzimuth(70, trueWind.getDirection());
//        yacht.sail.setCurrentStateOfSail(StatesOfSail.STARBOARD);
        yacht.process(0, 0, trueWind);
    }

    @Test
    public void should_change_trim_correctly_when_set_trueWind_and_courseAzimuth() {
        for (int i = 0; i < 6000; i++) { //1 minute
            Wind apparentWindAtFoot = yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
            yacht.sail.sailController.countControlValueAtFoot(apparentWindAtFoot.getDirection(), yacht.sail);
            yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
            yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
            yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());
            double thrustForce = yacht.sail.countThrustForce(yacht);
            yacht.process(thrustForce, 0, trueWind);

            if (i % 10 == 9) {
                System.out.print("yacht.velocity: ");
                System.out.println(yacht.getVelocity());
            }
        }
    }

    @Test
    public void should_change_twist_correctly_when_set_trueWind_and_courseAzimuth() {
        for (int i = 0; i < 600; i++) { //1 minute
            Wind apparentWindAtFoot = yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
            yacht.sail.sailController.countControlValueAtFoot(apparentWindAtFoot.getDirection(), yacht.sail);
            yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
            yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
            yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());

            Wind apparentWindAtHead = yacht.windIndicatorAtHead.measureWind(trueWind, yacht, yacht.sail.getHeadHeight());
            yacht.sail.sailController.countControlValueAtHead(apparentWindAtHead.getDirection(), yacht.sail);
            yacht.sail.sheetEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtHead());
            yacht.sail.sheetEngine.setCurrentVelocity(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
            yacht.sail.sheet.countLengthOverMin(yacht.sail.sheetEngine.getCurrentVelocity());

            double thrustForce = yacht.sail.countThrustForce(yacht);
            yacht.process(thrustForce, 0, trueWind);

            if (i % 10 == 9) {
                System.out.print("apparentWindAtFoot: ");
                System.out.println(apparentWindAtFoot.toString());
                System.out.print("yacht.sail.sailController.controlValueAtFoot: ");
                System.out.println(yacht.sail.sailController.getCurrentControlValueAtFoot());
                System.out.print("yacht.sail.carEngineController.currentStateOfCarEngine: ");
                System.out.println(yacht.sail.carEngineController.getCurrentStateOfEngine());
                System.out.print("yacht.sail.carEngine.velocity: ");
                System.out.println(yacht.sail.carEngine.getCurrentVelocity());
                System.out.print("car position in degrees: ");
                System.out.println(yacht.sail.car.getCurrentPositionInDegrees());
                System.out.print("apparentWindAtHead: ");
                System.out.println(apparentWindAtHead.toString());
                System.out.print("current trim angle: ");
                System.out.println(yacht.sail.getCurrentTrimAngle());
                System.out.print("current twist angle: ");
                System.out.println(yacht.sail.getCurrentTwistAngle());

                System.out.print("yacht.sail.sailController.controlValueAtHead: ");
                System.out.println(yacht.sail.sailController.getCurrentControlValueAtHead());
                System.out.print("yacht.sail.sheetEngineController.currentStateOfSheetEngine: ");
                System.out.println(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
                System.out.print("yacht.sail.sheetEngine.velocity: ");
                System.out.println(yacht.sail.sheetEngine.getCurrentVelocity());
                System.out.print("sheet.getCurrentLengthOverMin: ");
                System.out.println(yacht.sail.sheet.getCurrentLengthOverMin());
                System.out.print("thrust force: ");
                System.out.println(thrustForce);
                System.out.print("yacht.velocity: ");
                System.out.println(yacht.getVelocity());
//                System.out.print("-------->: ");
//                System.out.println(Math.cos(Math.toRadians(trueWind.getDirection())) * yacht.getVelocity());

//                trueWind.setDirection(trueWind.getDirection()+5);
                System.out.println("--------------");
            }
        }
    }

    @Test
    public void should_change_orientation_correctly_when_set_wind_and_required_course() {
        //Given

        //When
        for (int i = 0; i < 600; i++) { //1 minute

            Wind apparentWindAtFoot = yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
            yacht.sail.sailController.countControlValueAtFoot(apparentWindAtFoot.getDirection(), yacht.sail);
            yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
            yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
            yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());

            Wind apparentWindAtHead = yacht.windIndicatorAtHead.measureWind(trueWind, yacht, yacht.sail.getHeadHeight());
            yacht.sail.sailController.countControlValueAtHead(apparentWindAtHead.getDirection(), yacht.sail);
            yacht.sail.sheetEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtHead());
            yacht.sail.sheetEngine.setCurrentVelocity(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
            yacht.sail.sheet.countLengthOverMin(yacht.sail.sheetEngine.getCurrentVelocity());

            yacht.rudder.rudderController.countControlValue(yacht);
            yacht.rudder.rudderEngineController.setStateOfEngine(yacht.rudder.rudderController.getCurrentControlValue());
            yacht.rudder.rudderEngine.setCurrentAngleVelocity(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());

            double thrustForce = yacht.sail.countThrustForce(yacht);
            double sideForce = yacht.rudder.countSideForce(yacht.rudder.rudderEngine.getCurrentVelocity(), yacht);
            yacht.process(thrustForce, sideForce, trueWind);

            if (i % 10 == 9) {
                System.out.print("yacht.getRequiredCourseAzimuth:                                    ");
                System.out.println(yacht.getRequiredCourseAzimuth());
                System.out.print("yacht.rudder.rudderController.getRequiredAngle:                    ");
                System.out.println(yacht.rudder.rudderController.getRequiredAngle());
                System.out.print("yacht.rudder.rudderController.getCurrentControlValue:              ");
                System.out.println(yacht.rudder.rudderController.getCurrentControlValue());
                System.out.print("yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine: ");
                System.out.println(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());
                System.out.print("yacht.rudder.getCurrentAngle:                                      ");
                System.out.println(yacht.rudder.getCurrentAngle());
                System.out.print("sideForce:                                                         ");
                System.out.println(sideForce);
                System.out.print("yacht.getCurrentCourseAzimuth:                                     ");
                System.out.println(yacht.getCurrentCourseAzimuth());
//                System.out.print("yacht.velocity:                                         ");
//                System.out.println(yacht.getVelocity());
                System.out.println("-----");
//                if (i % 10 == 9)
//                yacht.setRequiredCourseAzimuth(yacht.getRequiredCourseAzimuth()+1, trueWind.getDirection());
            }
        }
        //Then
    }

    @Test
    public void should_return_state_of_yacht_every_1_sec_when_trueWind_is_constant() {
        //Given

        //When
        for (int i = 0; i < 60 * (1 / Simulation.samplingPeriod); i++) { //1 minute
            Wind apparentWindAtFoot = yacht.windIndicatorAtFoot.measureWind(trueWind, yacht, yacht.sail.getFootHeight());
            yacht.sail.sailController.countControlValueAtFoot(apparentWindAtFoot.getDirection(), yacht.sail);
            yacht.sail.carEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtFoot(), yacht.sail.getCurrentStateOfSail());
            yacht.sail.carEngine.setCurrentVelocity(yacht.sail.carEngineController.getCurrentStateOfEngine());
            yacht.sail.car.countPositionInDegrees(yacht.sail.carEngine.getCurrentVelocity());

            Wind apparentWindAtHead = yacht.windIndicatorAtHead.measureWind(trueWind, yacht, yacht.sail.getHeadHeight());
            yacht.sail.sailController.countControlValueAtHead(apparentWindAtHead.getDirection(), yacht.sail);
            yacht.sail.sheetEngineController.setStateOfEngine(yacht.sail.sailController.getCurrentControlValueAtHead());
            yacht.sail.sheetEngine.setCurrentVelocity(yacht.sail.sheetEngineController.getCurrentStateOfEngine());
            yacht.sail.sheet.countLengthOverMin(yacht.sail.sheetEngine.getCurrentVelocity());

            yacht.rudder.rudderController.countControlValue(yacht);
            yacht.rudder.rudderEngineController.setStateOfEngine(yacht.rudder.rudderController.getCurrentControlValue());
            yacht.rudder.rudderEngine.setCurrentAngleVelocity(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());

            double thrustForce = yacht.sail.countThrustForce(yacht);
            double sideForce = yacht.rudder.countSideForce(yacht.rudder.rudderEngine.getCurrentVelocity(), yacht);
            yacht.process(thrustForce, sideForce, trueWind);

            if (i % (30 / Simulation.samplingPeriod) == 0) {
                trueWind.setSpeed(random.nextDouble() * 7 + 3);
                trueWind.setDirection(random.nextInt(360));
                yacht.setRequiredCourseAzimuth(random.nextInt(360), trueWind.getDirection());
                yacht.setRequiredCourseAzimuth(yacht.getRequiredCourseAzimuth() + 5, trueWind.getDirection());
                System.out.print("trueWind.getSpeed:                      ");
                System.out.println(trueWind.getSpeed());
                System.out.print("trueWind.getDirection:                  ");
                System.out.println(trueWind.getDirection());
            }

            if (i % (1 / Simulation.samplingPeriod) == 9) {
                System.out.print("yacht.getFollowedCourseAzimuth:                                    ");
                System.out.println(yacht.getFollowedCourseAzimuth());
//                System.out.print("yacht.rudder.rudderController.getRequiredAngle:                    ");
//                System.out.println(yacht.rudder.rudderController.getRequiredAngle());
//                System.out.print("yacht.rudder.getCurrentAngle:           ");
//                System.out.println(yacht.rudder.getCurrentAngle());
//                System.out.print("yacht.rudder.rudderController.getCurrentControlValue:              ");
//                System.out.println(yacht.rudder.rudderController.getCurrentControlValue());
//                System.out.print("yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine: ");
//                System.out.println(yacht.rudder.rudderEngineController.getCurrentStateOfRudderEngine());
//                System.out.print("sideForce:                                                         ");
//                System.out.println(sideForce);
                System.out.print("yacht.getCurrentCourseAzimuth:                                     ");
                System.out.println(yacht.getCurrentCourseAzimuth());
                System.out.print("yacht.getVelocity:                      ");
                System.out.println(yacht.getVelocity());
                System.out.println("-----");
            }
        }
        //Then
    }
}
package yacht.sail;

import lombok.Data;
import simulation.Simulation;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;

import static java.lang.Math.*;

@Data
public class SailController {    //SailController operates only on apparent wind!

    private final int timeTakenIntoAccountWhileCountingAverageWindInSeconds = 5;
    private final int[] trimAnglesForMaxThrust;
    private double proportionalCoefficientForTrim;
    private double proportionalCoefficientForTwist;
    private double windDirectionAtFoot;        //yacht reference frame
    private double windDirectionAtHead;        //yacht reference frame
    private LinkedList<Double> windDirectionAtFootHistory;
    private LinkedList<Double> windDirectionAtHeadHistory;
    private double averageWindDirectionAtFoot;      //reduces wind fluctuations
    private double averageWindDirectionAtHead;      //reduces wind fluctuations
    private double currentControlValueAtFoot;
    private double currentControlValueAtHead;

    public SailController(Sail sail) {
        proportionalCoefficientForTrim = 0.1;
        proportionalCoefficientForTwist = 0.1;
        windDirectionAtFootHistory = new LinkedList<>();         //double[(int) (10/getSamplingPeriodInSeconds())];
        windDirectionAtHeadHistory = new LinkedList<>();
        trimAnglesForMaxThrust = new int[181];           //for angles from 0 to 180 degrees

        for (int i = 0; i < trimAnglesForMaxThrust.length; i++) {
            trimAnglesForMaxThrust[i] = i / 2;
        }
/*        for (int i = 0; i < trimAnglesForMaxThrust.length; i++) {
            double maxThrustAngle = 0;
            for (int j = 0; j < sail.liftCoefficientArray.length; j++) {
                double temp = sin(toRadians(i)) * sail.liftCoefficientArray[j] - cos(toRadians(i)) * sail.dragCoefficientArray[j];
                if (j == 0 || temp >= maxThrustAngle) {
                    trimAnglesForMaxThrust[i] = j;
                    maxThrustAngle = temp;
                }// constant:  0.5 * Simulation.airDensity * outer.getArea() * outer.outer.getVelocity() are omitted
            }
        }*/
    }

    public SailController(double proportionalCoefficientForTrim, double proportionalCoefficientForTwist, Sail sail) {
        this.proportionalCoefficientForTrim = proportionalCoefficientForTrim;
        this.proportionalCoefficientForTwist = proportionalCoefficientForTwist;
        windDirectionAtFootHistory = new LinkedList<>();         //double[(int) (10/getSamplingPeriodInSeconds())];
        windDirectionAtHeadHistory = new LinkedList<>();
        trimAnglesForMaxThrust = new int[181];           //for angles from 0 to 180 degrees

        for (int i = 0; i < trimAnglesForMaxThrust.length; i++) {
            double maxThrustAngle = 0;
            for (int j = 0; j < sail.liftCoefficientArray.length; j++) {
                double temp = sin(toRadians(i)) * sail.liftCoefficientArray[j] - cos(toRadians(i)) * sail.dragCoefficientArray[j];
                if (j == 0 || temp >= maxThrustAngle) {
                    trimAnglesForMaxThrust[i] = j;
                    maxThrustAngle = temp;
                }// constant:  0.5 * Simulation.airDensity * outer.getArea() * outer.outer.getVelocity() are omitted
            }
        }//SPRAWDŹ JAK WYGLĄDA WYNIK TEGO, CZY MA TO SENS, CZY ZMIANA KĄTA TRYMU BĘDZIE PŁYNNA
    }

    /*
    jacht otrzymuje kierunek wiatru od WindIndicator w jachtowym układzie odniesienia (-180 - +180)
    i przekazuje SailControllerowi
    */
    private double countAverageAtFoot(double newWindDirection) throws NoSuchElementException {
        if (!windDirectionAtFootHistory.isEmpty()) {
            OptionalDouble averageWindDirection = windDirectionAtFootHistory.stream()
                    .mapToDouble(Double::doubleValue)
                    .average();
            if (averageWindDirection.isPresent()) {
                double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
                if (newWindDirection > average + 180)
                    newWindDirection -= 360;
                if (newWindDirection < average - 180)
                    newWindDirection += 360;
            }
        }
        windDirectionAtFootHistory.addLast(newWindDirection);
        if (windDirectionAtFootHistory.size() > timeTakenIntoAccountWhileCountingAverageWindInSeconds / Simulation.samplingPeriod)
            windDirectionAtFootHistory.removeFirst();
        OptionalDouble averageWindDirection = windDirectionAtFootHistory.stream()
                .mapToDouble(Double::doubleValue)
                .average();
        if (averageWindDirection.isPresent()) {
            double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
            if (average > 180)
                average -= 360;
            if (average < -180)
                average += 360;
            return average;
        } else
            throw new NoSuchElementException("Method 'countAverageAtFoot' is unable to return a value.");
    }

    private double countAverageAtHead(double newWindDirection) throws NoSuchElementException {
        if (!windDirectionAtHeadHistory.isEmpty()) {
            OptionalDouble averageWindDirection = windDirectionAtHeadHistory.stream()
                    .mapToDouble(Double::doubleValue)
                    .average();
            if (averageWindDirection.isPresent()) {
                double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
                if (newWindDirection > average + 180)
                    newWindDirection -= 360;
                if (newWindDirection < average - 180)
                    newWindDirection += 360;
            }
        }
        windDirectionAtHeadHistory.addLast(newWindDirection);
        if (windDirectionAtHeadHistory.size() > timeTakenIntoAccountWhileCountingAverageWindInSeconds / Simulation.samplingPeriod)
            windDirectionAtHeadHistory.removeFirst();
        OptionalDouble averageWindDirection = windDirectionAtHeadHistory.stream()
                .mapToDouble(Double::doubleValue)
                .average();
        if (averageWindDirection.isPresent()) {
            double average = averageWindDirection.getAsDouble();   //throws NoSuchElementException if there is no value
            if (average > 180)
                average -= 360;
            if (average < -180)
                average += 360;
            return average;
        } else
            throw new NoSuchElementException("Method 'countAverageAtHead' is unable to return a value.");
    }

    //This function receives measured apparent wind and counts control variable, which is required sail trim
    //It uses coefficient arrays defined by Sail outer class
    //This function also handles tacking and gybing
    public int countRequiredTrim(double newWindDirectionAtFoot, Sail sail) {    //function call param: Yacht.WindIndicator.process().direction
        double newAverageWindDirection = countAverageAtFoot(newWindDirectionAtFoot);
        if (newAverageWindDirection < -20 && newAverageWindDirection > -135) {
            if (sail.getCurrentStateOfSail() == StatesOfSail.PORT || sail.getCurrentStateOfSail() == StatesOfSail.TO_PORT)
                sail.setCurrentStateOfSail(StatesOfSail.TO_STARBOARD);
        } else if (newAverageWindDirection > 20 && newAverageWindDirection < 135) {
            if (sail.getCurrentStateOfSail() == StatesOfSail.STARBOARD || sail.getCurrentStateOfSail() == StatesOfSail.TO_STARBOARD)
                sail.setCurrentStateOfSail(StatesOfSail.TO_PORT);
        }
        if (sail.getCurrentStateOfSail() == StatesOfSail.TO_STARBOARD && sail.car.getCurrentPositionInDegrees() > 10)
            sail.setCurrentStateOfSail(StatesOfSail.STARBOARD);
        if (sail.getCurrentStateOfSail() == StatesOfSail.TO_PORT && sail.car.getCurrentPositionInDegrees() < -10)
            sail.setCurrentStateOfSail(StatesOfSail.PORT);

        int windDirection = (int) abs(round(newAverageWindDirection));
        if (newAverageWindDirection < 0)
            return trimAnglesForMaxThrust[windDirection];
        else
            return -trimAnglesForMaxThrust[windDirection];

    }

    //This function receives measured apparent wind and counts control variable, which is required sail twist
    public int countRequiredTwist(double newWindDirectionAtHead, Sail sail) {    //function call param: Yacht.WindIndicator.process().direction
        if (sail.getCurrentStateOfSail() == StatesOfSail.TO_PORT || sail.getCurrentStateOfSail() == StatesOfSail.TO_STARBOARD)
            return 0;
        double newAverageWindDirection = countAverageAtHead(newWindDirectionAtHead);
        int windDirection = (int) abs(round(newAverageWindDirection));
        return trimAnglesForMaxThrust[windDirection] - (int) sail.getCurrentTrimAngle();
    }

    public void countControlValueAtFoot(double newWindDirection, Sail sail) {
        setCurrentControlValueAtFoot(getProportionalCoefficientForTrim() * (countRequiredTrim(newWindDirection, sail) - sail.getCurrentTrimAngle()));
    }

    public void countControlValueAtHead(double newWindDirection, Sail sail) {
        setCurrentControlValueAtHead(getProportionalCoefficientForTwist() * (countRequiredTwist(newWindDirection, sail) - sail.getCurrentTwistAngle()));
    }

}

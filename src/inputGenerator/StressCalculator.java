/**
 * 
 */
package inputGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Shaza
 *
 */
public class StressCalculator {
	protected StressCalculator() {
		super();
		truckPotential = new BigDecimal(Param.TRUCK_POTENTIAL_PER_HOUR_REALISTIC);
		truckPotential = truckPotential.multiply(new BigDecimal(1000)); //converting to mm3
		truckPotential = truckPotential.setScale(2, RoundingMode.HALF_UP);
		truckPotential = truckPotential.divide(new BigDecimal(3600), RoundingMode.HALF_UP);
	}
	BigDecimal truckPotential;// per second

	BigDecimal getStress (BigDecimal load, int noOfTrucks) {
		BigDecimal divisor = new BigDecimal(noOfTrucks);
		divisor = divisor.multiply(truckPotential);
		BigDecimal stress = load.divide(divisor, RoundingMode.HALF_UP);
		
		return stress;
	}
}

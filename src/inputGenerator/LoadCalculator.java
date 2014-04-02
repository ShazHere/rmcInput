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
public class LoadCalculator {
	
	final int SIZE = 86400;//Param.END_DATETIME.getSecondOfDay()-Param.START_DATETIME.getSecondOfDay();
	boolean[] loadArr = new boolean [SIZE];
	BigDecimal loadValue = null;
	public LoadCalculator() {
		super();
	}
	/**
	 * @param orderRelated
	 * @return load value in mm3/s (millimeter cube per second)
	 */
	public BigDecimal getLoad(OrderRelated orderRelated) {
		int startSecond = 0;
		int endSecond = 0;
		for(OrderRelated.OrderDetails ord : orderRelated.getOrderDetails()) {
			startSecond = ord.getStartTime().getSecondOfDay();
			endSecond = startSecond +  //here implicitly, unloading rate of 10m3 / hr is used..
					+ (int)((orderRelated.getPossibleDeliveriesForQuantity(ord.getQuantity())*3600));
			setLoaded(startSecond, endSecond);
		}
		
		int loadedSeconds = getLoadedSeconds();
		BigDecimal load = new BigDecimal(orderRelated.getTotalOrdersQuantity()/loadedSeconds);
		load = load.multiply(new BigDecimal(1000));
		load = load.setScale(1, RoundingMode.HALF_UP);
		loadValue = load;
		return load;
	}
	
	protected BigDecimal getLoadValue(){
//		if (loadValue == null)
//			System.out.println("LOADVALUE is null");
//		
		return loadValue;
	}
	/**
	 * @return no. of seconds in the array with value true
	 */
	private int getLoadedSeconds() {
		int loadedSec = 0;
		for (int i = 0;i < SIZE; i++) {
			if (loadArr [i] == true) {
				loadedSec++;
			}
		}
		return loadedSec;
	}
	private void setLoaded(int startSecond, int endSecond) {
		assert startSecond<endSecond == true : "StartSecond is greater than endScond";
		assert (startSecond < SIZE && endSecond < SIZE) == true : "startSecond or endSecond are greater than SIZE";
		for (int i = startSecond; i <= endSecond; i++) {
			loadArr[i] = true;
		}
		
	}
	
}

/**
 * 
 */
package inputGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;

import shaz.rmc.pdpExtended.delMasInitial.GlobalParameters;

/**
 * @author Shaza
 *
 */
public class OrderRelated {
	
	private ArrayList<OrderDetails> orderDetails;
	
	private LoadCalculator loadCal;

	private int retrieveQuantityIndex;
	private int retrieveStartTimeIndex;
	
	OrderRelated(int noOfOrders) {
		loadCal =  new LoadCalculator();
		orderDetails = new ArrayList<OrderRelated.OrderDetails>();
		retrieveQuantityIndex = 0;
		retrieveStartTimeIndex = 0;
		RandomGenerator rng = new MersenneTwister();
		for (int i = 0; i < noOfOrders; i++) {
			double orderQuantity = getRandomQuantity(Param.AVG_ORDER_SIZE, rng);
			DateTime orderStarttime =getRandomTimeAround(makeMaximumPossibleStartTime(orderQuantity).getSecondOfDay() - Param.START_DATETIME.getSecondOfDay() , rng); 
			orderDetails.add(new OrderDetails(orderQuantity, orderStarttime, 3));
		}
	}
	
	public double getNextOrderQuantity() {
		return orderDetails.get(retrieveQuantityIndex++).getQuantity();
		
	}
	
	public DateTime getNextOrderStartTime() {
		return orderDetails.get(retrieveStartTimeIndex++).getStartTime();
	}
	
	public BigDecimal getCombineLoad() {
		if (loadCal.getLoadValue() == null)
			return loadCal.getLoad(this);
		else 
			return loadCal.getLoadValue();
	}
	
	/**
	 * @author Shaza
	 * Details about single order, only those details that varry for different orders
	 */
	public class OrderDetails {
		private double quantity;
		private DateTime startTime;
		
		public OrderDetails(double quantity, DateTime startTime, double load) {
			super();
			this.quantity = quantity;
			this.startTime = startTime;
		}

		public double getQuantity() {
			return quantity;
		}

		public DateTime getStartTime() {
			return startTime;
		}
	
	}
	
	protected double getTotalOrdersQuantity() {
		double totalQuantity = 0;
		for (OrderDetails od : this.orderDetails) {
			totalQuantity += od.getQuantity();
		}
		return totalQuantity;
	}
	private double getRandomQuantity(double averageOrderSize, RandomGenerator rng) {
		double nb = round(GaussianGenerator.getRandomQuantityAround(averageOrderSize,rng));
		while(nb < 0) {
			nb = round(GaussianGenerator.getRandomQuantityAround(averageOrderSize,rng));
		}
		return nb;
	}
	
	private static DateTime getRandomTimeAround(long second, RandomGenerator rng) {
		long asSeconds = GaussianGenerator.getRandomTimeAround(second,rng);
		return Param.START_DATETIME.plusSeconds((int)asSeconds);
	}
	

	public static DateTime makeMaximumPossibleStartTime(double quantity) {
		int possibleDeliveries = getPossibleDeliveriesForQuantity(quantity);
		DateTime maxST = Param.END_DATETIME.minusHours((int) (possibleDeliveries+1)); //1 is aded as a precautions..coz ther is also some booking time required

		return maxST;
	}

	/**
	 * @param q
	 * @return
	 */
	protected static int getPossibleDeliveriesForQuantity(double q) {
		int possibleDeliveries =  (int)Math.ceil((q*1000) / GlobalParameters.FIXED_CAPACITY);
		return possibleDeliveries;
	}

	private double round(double nb) {
		return (double) Math.round(nb*4)/4d;
	}
	protected ArrayList<OrderDetails> getOrderDetails() {
		return orderDetails; //TODO: return defensive copy..
	}
}

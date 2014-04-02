/**
 * 
 */
package inputGenerator;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;

/**
 * @author Shaza
 * @ 10/09/2013
 */
/**
 * NO. of trucks range from 4 to 20, it also define scale of the problem
 * No. of orders range from 2 to 25
 * average load per order is AVG_ORDER_SIZE, and for each order its is taken from gaussian distribution with mean AVG_ORDER_SIZE
 * and variance ORDERSIZE_VARIANCE
 * 
 * StartTime of order is decided between start of day and LIMIT. where LIMIT is calculated by considering 
 * unloading rate * total concrete of order + 1 hour
 * 
 * truck break downs are handled via simualation itself
 * 
 * load and stress are defined on paper. General sense is load is amount of concrete per loaded second. 
 * Stress is load /(no.of trucks*potential per truck) 
 */
public class Param {

	private Param(){} //Just to make sure its objects are never initiated
	
	public static final int TOTAL_FILES = 10; //No of files required. They could be for specfic stress scale value or generic output like 
	//generate 1000 files, in which stress&scale will vary
	
	public static final String DATA_FOLDER= "/Users/Shaza/Documents/RMC_Data_2013/";
	public static final String RESULT_FOLDER = "/Users/Shaza/Documents/RMC_Data_2013/input/";
	public static final String INPUT_FILE = "sampleInput.xml";
	
	//public static final int TOTAL_ORDERS = getTotalNoOfOrders(new MersenneTwister()); 
	public static final int AVG_ORDER_SIZE = 40;
	public static final double ORDERSIZE_VARIANCE = 22;
	
	public static final int TOTAL_PS = 3; // always 3 ps in the region
	public static final int FIXED_DISTANCE_FROM_ALL_PS = 15; // in min, actually whatever value doesn't matter
	public static final int DISCHARGE_RATE_PERHOUR = 10000; //means 10m3 per hour
	
	public static final double TIME_VARIANCE = 100;
	public static final int TRUCK_POTENTIAL_PER_HOUR_REALISTIC = 5; //means 5m3 per hour

	public static final DateTime START_DATETIME = new DateTime(2011, 1, 10, 7, 0, 0 ,0, GregorianChronology.getInstance()); //07AM on 10Jan, 2011
	
	public static final DateTime END_DATETIME = new DateTime(2011, 1, 10, 23, 55, 0, 0, GregorianChronology.getInstance());//10PM on 12Jan, 2011
	
	
	public static int getTotalNoOfOrders(RandomGenerator rng) {
		int totalOrders = 0;
		while (totalOrders < 2) {
			totalOrders = rng.nextInt(33);
		}
		return totalOrders;
	}
}

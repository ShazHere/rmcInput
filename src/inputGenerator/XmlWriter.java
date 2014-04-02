/**
 * 
 */
package inputGenerator;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * @author Shaza
 *
 */
public class XmlWriter {
	private int noOfOrders;
	private final int noOfPS;
	private final int fixedDistanceFromAllPS;
	private BigDecimal stress;
	private int noOfTrucks;
	private RandomGenerator rng;
	protected XmlWriter(RandomGenerator rng) {
		super();
		noOfOrders = Param.getTotalNoOfOrders(rng);
		noOfPS = Param.TOTAL_PS; 
		fixedDistanceFromAllPS = Param.FIXED_DISTANCE_FROM_ALL_PS;
		this.rng = rng;
	}

		
	protected String makeXML() {
		
		StringBuffer result = new StringBuffer("");
		noOfOrders = Param.getTotalNoOfOrders(rng); //for each file, different no. of orders used.. 
		
		result = write(0,"<?xml version=\"1.0\" encoding=\"utf-8\"?>", result);
		result = write(0,"<ConcretePlanning xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">", result);
		OrderRelated or = new OrderRelated(noOfOrders);
		result = writeOrders(result, rng, or);
		result = writeVehicles(result);
		result = writeStations(result);
		result = writeOutput(result);
		result = writeScenario(result , or);
		result = write(0,"</ConcretePlanning>",result);
		
		return result.toString();
	}
	


	private StringBuffer writeOrders(StringBuffer result, RandomGenerator rng, OrderRelated or) {
		result = write(1,"<Orders>", result);
		for (int i = 0; i < noOfOrders; i++) {
			result = writeOrder(i, result, rng, or);
		}
		result = write(1,"</Orders>", result);
		return result;
	}

	private StringBuffer writeOrder(int code, StringBuffer result, RandomGenerator rng, OrderRelated or) {
		result = write(2,"<Order>", result);
		result = write(2,"<OrderCode>" + code + "</OrderCode>", result);
	
		result = writeConstructionYard(code, result, rng);
		
		result = write(2,"<PumpLineLengthRequired>0</PumpLineLengthRequired>", result);
		result = write(2,"<From>" + or.getNextOrderStartTime() + "</From>", result);
		result = write(2,"<TotalVolumeM3>" + or.getNextOrderQuantity() +"</TotalVolumeM3>", result);
		result = write(2,"<RequiredDischargeM3PerHour>8.00</RequiredDischargeM3PerHour>", result);
		result = write(2,"<PreferredStationCode>119/kallo1</PreferredStationCode>", result);
		result = write(2,"<MaximumVolumeAllowed>true</MaximumVolumeAllowed>", result);
		result = write(2,"<IsPickUp>false</IsPickUp>", result);
		result = write(2,"<Priority>0</Priority>", result);
		result = write(2,"</Order>", result);
		return result;
	}
	
	private StringBuffer writeConstructionYard(int code, StringBuffer result, RandomGenerator rng) {
		result = write(3,"<ConstructionYard>", result);
		result = write(4,"<ConstructionYardCode>" + code + "</ConstructionYardCode>", result);
		result = write(4,"<WaitingMinutes>5</WaitingMinutes>", result);
		result = write(4,"<StationDurations>", result);
	     
		result = writeStationDurations(result, rng);
	          
		result =  write(4,"</StationDurations>", result);
		result =  write(3,"</ConstructionYard>", result);
		return result;
	}

	private StringBuffer writeStationDurations(StringBuffer result, RandomGenerator rng) {
		int code;
		//Map<Integer, Integer> drivingTimes = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < noOfPS; i++) {
			code = noOfOrders + i;
			//int drivingMinutes = getRandomDrivingTime(rng, dTimes);
			//drivingTimes.put(code,drivingMinutes);
			result = write(5,"<StationDuration>", result);
			result = write(6,"<StationCode>" + code + "</StationCode>", result);
			result = write(6,"<DrivingMinutes>" +fixedDistanceFromAllPS + "</DrivingMinutes>", result);
			result = write(6,"<Direction>From</Direction>", result);
			result = write(5,"</StationDuration>", result);
		}
		for (int i = 0; i < noOfPS; i++) {
			code = noOfOrders + i;
			result = write(5,"<StationDuration>", result);
			result = write(6,"<StationCode>" + code + "</StationCode>", result);
			result = write(6,"<DrivingMinutes>" +fixedDistanceFromAllPS + "</DrivingMinutes>", result);
			result = write(6,"<Direction>To</Direction>", result);
			result = write(5,"</StationDuration>", result);
		}
		return result;
	}
	
	
	private  StringBuffer writeVehicles(StringBuffer result) {
		result = write(1,"<Vehicles>", result);
		noOfTrucks = rng.nextInt(16) + 4; //selecting noOfTruck between 4 and 20
		for (int i = 0; i < noOfTrucks; i++) { // only one truck since currently i m not accomodating stress
			result = writeVehicle(result);
		}
		result = write(1,"</Vehicles>", result);
		return result;
	}	
	
	private  StringBuffer writeVehicle(StringBuffer result) {
		result = write(2,"<Vehicle>", result);
		result = write(3,"<VehicleCode>M62</VehicleCode>", result);
		result = write(3,"<VehicleType>Pump</VehicleType>", result);
		result = write(3,"<PumpLineLength>0</PumpLineLength>", result);
		result = write(3,"<NormalVolume>" + 10 + "</NormalVolume>", result);
		result = write(3,"<MaximumVolume>" + 10 + "</MaximumVolume>", result);
		result = write(3,"<DischargeM3PerHour>8</DischargeM3PerHour>", result);
		result = write(3,"<NextAvailableStartDateTime>2011-01-10T14:45:26.0466756+01:00</NextAvailableStartDateTime>", result);
		result = write(2,"</Vehicle>", result);
		return result;
	}
	
	private StringBuffer writeStations(StringBuffer result) {
		result = write(1,"<Stations>", result);
		for (int i = 0; i < noOfPS; i++) {
			result = writeStation(1 + i, result);
		}
		result = write(1,"</Stations>", result);
		return result;
	}

	private StringBuffer writeStation(int code,StringBuffer result) {
		result = write(2,"<Station>", result);
		result = write(3,"<StationCode>" + code + "</StationCode>", result);
		result = write(3,"<LoadingMinutes>5</LoadingMinutes>", result);
		result = write(2,"</Station>", result);
		return result;
	}

	private StringBuffer writeOutput(StringBuffer result) {
		result = write(1,"<Output>", result);
		result = write(2,"<CalculationDateTime>0001-01-01T00:00:00</CalculationDateTime>", result);
		result = write(2,"<CalculationMinutes>0</CalculationMinutes>", result);
		result = write(2,"<TotalResults>0</TotalResults>", result);
		result =  write(2,"<Results />", result);
		result = write(1,"</Output>", result);
		return result;
	}
	private StringBuffer writeScenario(StringBuffer result, OrderRelated or) {
		result = write(1,"<ScenarioRelated>", result);
		result = write(2,"<Load>"+ or.getCombineLoad() +"</Load>", result);
		stress = new StressCalculator().getStress(or.getCombineLoad(), noOfTrucks);
		result = write(2,"<Stress>"+ stress +"</Stress>", result);
		result = write(1,"</ScenarioRelated>", result);
		return result;
	}
	private StringBuffer write(int nbTabs, String string, StringBuffer result) {
		String print = "";
		for (int i = 0; i < nbTabs; i++) {
			print += "\t ";
		}
		return result.append(print + string + " \r\n");
	}


	protected BigDecimal getStress() {
		return this.stress;
	}


	protected int getScale() {
		return noOfTrucks;
	}
	
	
}

package inputGenerator;

import org.apache.commons.math3.random.RandomGenerator;


public class GaussianGenerator {

	
	public static double getRandomQuantityAround(double mean, RandomGenerator rng) {
		return getGaussian(mean, Param.ORDERSIZE_VARIANCE,rng);
	}

	public static long getRandomTimeAround(long second, RandomGenerator rng) {
		return (long) getGaussian(second/2, second/2,rng);  //Param.TIME_VARIANCE
	}
	
	public static double getGaussian(double mean, double variance, RandomGenerator rng){
//		double chk = rng.nextGaussian();
//		chk = mean + rng.nextGaussian() * variance;
//		double chk2 = rng.nextGaussian();
//		chk2 = mean + rng.nextGaussian() * variance;
//		double chk3 = rng.nextGaussian();
//		chk3 = mean + rng.nextGaussian() * variance;
//		double returnn = rng.nextGaussian();
//		returnn = mean + rng.nextGaussian() * variance;
		double value;
		do {
			value = mean + rng.nextGaussian() * variance;
		}
		while (value > mean + variance || value < mean - variance);
		
		return value;
	}

}

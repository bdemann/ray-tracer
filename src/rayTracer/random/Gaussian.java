package rayTracer.random;

import java.util.Random;

public class Gaussian {


	public static void main(String[] args) {
		double[] result = {0,0};
		int sampleSize = 10000;
		for(int i = 0; i < sampleSize; i++){
			double[] temp = gaussian(i);
			result[0] += temp[0];
			result[1] += temp[1];
//			result += random(i);
		}
		System.out.println(result[0]/sampleSize);
		System.out.println(result[1]/sampleSize);
	}
	
	public static double rand(double max, double min) {
		Random rand = new Random();
		double exponent = -(Math.pow(rand.nextDouble() - .5, 2) * 20);
		double gaussian = Math.exp(exponent);
		double result = gaussian * (max - min) + min;
		return result;
	}
	
	public static double[] gaussian(long seed){
		Random rand = new Random();
		double x1, x2, w, y1, y2;
		 
        do {
                x1 = 2.0 * rand.nextDouble() - 1.0;
                x2 = 2.0 * rand.nextDouble() - 1.0;
                w = x1 * x1 + x2 * x2;
        } while ( w >= 1.0 );

        w = Math.sqrt( (-2.0 * Math.log( w ) ) / w );
        y1 = x1 * w * .25;
        y2 = x2 * w * .25;
        double[]result = {y1, y2};
        return result;
	}
	
	public static double random(long seed) {
		Random rand = new Random();
		return rand.nextDouble();
	}
}

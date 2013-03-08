package wav;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class GenerateWave {

	
	public static double[] square(int fre,int freq, int sampleSize){
		double[] signal = new double[sampleSize];
		
		
		for(int index =0 ; index<sampleSize; index++){
			double sec = idx2TimeSec(fre, index);
			
			double cos = Math.cos((2*Math.PI*freq)*sec);
			
			//signal[index] = new Long(Math.round(cos)).shortValue();
			signal[index]=cos;
		}
		
		
		return signal;
	}
	
	public static double idx2TimeSec(int fre,int index){
		double time = ((double) index)/((double) fre);
		return time;
	}
	
	public static double[] sum(double[][] toSum){
		double[] sum = new double[toSum[0].length];
		for(double[] serie : toSum){
			for(int i = 0 ; i< serie.length;i++){
				sum[i]+=serie[i];
			}
		}
		
		return sum;
	}
	
	public static short[] toShort(double[] in){
		short[] result = new short[in.length];
		
		for(int i = 0;i<in.length;i++){
			result[i] = new Double(in[i]).shortValue();
		}
		
		return result;
	}
	
	public static double[] todouble(short[] in){
		double[] result = new double[in.length];
		
		for(int i = 0;i<in.length;i++){
			result[i] = new Double(in[i]);
		}
		
		return result;
	}
	
	public static double[] todouble(float[] in){
		double[] result = new double[in.length];
		
		for(int i = 0;i<in.length;i++){
			result[i] = new Double(in[i]);
		}
		
		return result;
	}
	
	
	
	
	
	public static int maxID(double[] in){
		int result = 0;
		double max = Double.MIN_VALUE;
		for(int i = 0;i<in.length;i++){
			if(in[i] >= max){
				result=i;
				max = in[i];
			}
		}
		
		return result;
	}
	
	
	public static double[] normalize(double[] in){
		double[] result = new double[in.length];
		int maxid = GenerateWave.maxID(in);
		double maxValue = in[maxid];
		for(int i = 0;i<in.length;i++){
			result[i] = in[i]/maxValue;
		}
		
		return result;
	}
	
	
	public static short[] byte2short(byte[] byteArray){
		short[] result = new short[byteArray.length/2];
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		ShortBuffer ib = bb.asShortBuffer();

		for(int iter = 0 ; iter<byteArray.length/2  ;   iter++){
			result[iter] = ib.get(iter);
		}

		return result;
	}
	
}


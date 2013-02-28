package wav;

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
			result[i] = new Double(in[i]*10000).shortValue();
		}
		
		return result;
	}
}

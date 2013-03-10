package jfloppy.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;

public class JFloppyAudioTB {

	
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
	public static int minID(double[] in){
		int result = 0;
		double min = Double.MAX_VALUE;
		for(int i = 0;i<in.length;i++){
			if(in[i] <= min){
				result=i;
				min = in[i];
			}
		}
		
		return result;
	}
	
	
	public static double[] normalize(double[] in){
		double[] result = new double[in.length];
		int maxid = JFloppyAudioTB.maxID(in);
		double maxValue = in[maxid];
		for(int i = 0;i<in.length;i++){
			result[i] = in[i]/maxValue;
		}
		
		return result;
	}
	
	
	public static float[] extractFloatDataFromAmplitudeByteArray(AudioFormat format, byte[] audioBytes) {
		// convert
		float[] audioData = null;
		if (format.getSampleSizeInBits() == 16) {
			int nlengthInSamples = audioBytes.length / 2;
			audioData = new float[nlengthInSamples];
			if (format.isBigEndian()) {
				for (int i = 0; i < nlengthInSamples; i++) {
					/* First byte is MSB (high order) */
					int MSB = audioBytes[2 * i];
					/* Second byte is LSB (low order) */
					int LSB = audioBytes[2 * i + 1];
					audioData[i] = MSB << 8 | (255 & LSB);
				}
			}
			else {
				for (int i = 0; i < nlengthInSamples; i++) {
					/* First byte is LSB (low order) */
					int LSB = audioBytes[2 * i];
					/* Second byte is MSB (high order) */
					int MSB = audioBytes[2 * i + 1];
					audioData[i] = MSB << 8 | (255 & LSB);
				}
			}
		}
		else if (format.getSampleSizeInBits() == 8) {
			int nlengthInSamples = audioBytes.length;
			audioData = new float[nlengthInSamples];
			if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
				for (int i = 0; i < audioBytes.length; i++) {
					audioData[i] = audioBytes[i];
				}
			}
			else {
				for (int i = 0; i < audioBytes.length; i++) {
					audioData[i] = audioBytes[i] - 128;
				}
			}
		}// end of if..else
		// System.out.println("PCM Returned===============" +
		// audioData.length);
		return audioData;
	}
	
}


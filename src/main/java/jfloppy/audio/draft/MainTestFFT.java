package jfloppy.audio.draft;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JFrame;

import jfloppy.audio.JFloppyAudioTB;

import com.musicg.dsp.FastFourierTransform;

import panel.MultipleTimeTrace;

public class MainTestFFT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		MultipleTimeTrace mtt = new MultipleTimeTrace();


		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(mtt);
		f.pack();
		f.setVisible(true);

		int sampleSize = 4096;
		int[] freqsToGenerate = {400,800,1200};
		
		ArrayList<Integer> toGEN = new ArrayList<Integer>();
		for(int i = 100; i<10000;i*=1.2){
			toGEN.add(i);
		}
		freqsToGenerate = new int[toGEN.size()];
		
		for(int i=0; i < freqsToGenerate.length ; i ++){
			freqsToGenerate[i] = toGEN.get(i);
		}
		
		int generatedCount = freqsToGenerate.length;
		
		
		double[][] generated = new double[generatedCount][sampleSize];
		
		
		for(int freqid = 0; freqid < generatedCount ;freqid+= 1){
			int freq = freqsToGenerate[freqid];
			double[] wave = JFloppyAudioTB.square(44000, freq, sampleSize);
			
			generated[freqid]=wave;
			
			for(int i=0; i < wave.length ; i ++){
				//mtt.addValueInSecureThread(i, la[i], "f="+freq, 2000);
			}
		}

		double[] summed = JFloppyAudioTB.sum(generated);
		for(int i=0; i < summed.length ; i ++){
			//mtt.addValueInSecureThread(i, summed[i], "sum", 2000);
		}
		
		System.out.println("summed size"+summed.length);
		double[] absoluteSpectrogram ;
		// for each frame in signals, do fft on it
		FastFourierTransform fft = new FastFourierTransform();
		
		
		long start = System.currentTimeMillis();
		absoluteSpectrogram=fft.getMagnitudes(summed);
		long finish = System.currentTimeMillis();
		
		System.out.println("in "+(finish-start));
		
		for(int i=0; i < absoluteSpectrogram.length ; i ++){
			mtt.addValueLater(i, absoluteSpectrogram[i], "specOfSum", 2000);
		}
		
		int foundPeak = 0;
		for(int i = 1 ; i<absoluteSpectrogram.length-1 ; i++){
			if(absoluteSpectrogram[i-1] < absoluteSpectrogram[i]  && absoluteSpectrogram[i] > absoluteSpectrogram[i+1]){
				//peak
				System.out.println(freqsToGenerate[foundPeak]+" "+i);
				foundPeak++;
			}
			
		}
		
		
	}

}

package jfloppy.audio.draft;

import jfloppy.audio.SpectroPanel;


public class MainRecordSpecShow {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		String fileName = "./wavbase/flopB2khz.wav";
		Long durationMS = 5000L;
		
		int spectrumFrameSize = 2048*2*2;
		int overlap = 0;
		LoadSpectrum loadSpectrum = new LoadSpectrum(fileName, spectrumFrameSize, overlap);
		
		SpectroPanel spectrumShow = new SpectroPanel();
		
		FusionSpectrum fusion = new FusionSpectrum() {
			
			@Override
			public double[] getFusionnedSpectrum(double[][] spectrumIn) {
				double [] total = new double[spectrumIn[0].length];
				int totalFrame  = spectrumIn.length;
				
				
				for(int frame = totalFrame/3 ;frame < totalFrame*3/4; frame++){
					double [] freqArray = spectrumIn[frame];
					for(int freqid =0 ; freqid < spectrumIn[0].length; freqid++){
						double freqvalue = freqArray[freqid];
						total[freqid]+= freqvalue;
					}
				}
				
				return total;
			}
		};
		
		FusionSpectrum middleOne = new FusionSpectrum() {
			
			@Override
			public double[] getFusionnedSpectrum(double[][] spectrumIn) {
				return spectrumIn[spectrumIn.length/2];
			}
		};
		
		FusionSpectrum sumOfNinMiddle = new FusionSpectrum() {
			
			@Override
			public double[] getFusionnedSpectrum(double[][] spectrumIn) {
				
				int n = 2;
				
				double [] total = new double[spectrumIn[0].length];
				int middleFrame  = spectrumIn.length/2;
				
				
				for(int frame = middleFrame-n ;frame <  middleFrame+n; frame++){
					double [] freqArray = spectrumIn[frame];
					for(int freqid =0 ; freqid < spectrumIn[0].length; freqid++){
						double freqvalue = freqArray[freqid];
						total[freqid]+= freqvalue;
					}
				}
				
				return total;
			}
		};
		
		SaveWaveWitPulseAudioLinux.save(fileName, durationMS);
		
		double [][] spectrum = loadSpectrum.getSpectrum();

		double[] fusionned = fusion.getFusionnedSpectrum(spectrum);
		
		spectrumShow.plotSpectrum(fusionned,"fusion");
		
		spectrumShow.plotSpectrum(middleOne.getFusionnedSpectrum(spectrum),"middle");
	
		spectrumShow.plotSpectrum(sumOfNinMiddle.getFusionnedSpectrum(spectrum), "middle2");
	}

}

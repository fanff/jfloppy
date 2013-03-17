package jfloppy.audio.tools;

import java.util.ArrayList;

public class PeakDetection extends SpeakerListener {

	double peakTreshold = 0d;
	@Override
	public void listen(double[] audioData) {
		
		double[] peakResults = new double[audioData.length];
		
		int extand = 7;
		for(int i = extand ; i<audioData.length-extand ; i++){
			double ati = audioData[i];
			
			if(ispeakHere(audioData, extand,i)){
				peakResults[i] = ati ;
			}else{
				peakResults[i] = 0 ;
			}
		}
		
		forAllListenerSpeak(peakResults);

	}
	
	public boolean ispeakHere (double[] audioData , int extend ,int pos){
		int[] dis = getpks(extend);
		
		boolean isPeak = true;
		
		for(int di : dis ){
			if(audioData[pos] > audioData[pos+di]){
				
			}else{
				isPeak = false;
			}
		}
		
		return isPeak;
	}
	
	public int[] getpks (int extand){

		int size = (extand ) * 2;
		int[] pks = new int[size];
		
		int pos = 0;
		for(int i = 1 ; i<= extand ; i++ ){
			pks[pos] = i;
			pks[pos+1] = -i;
			
			pos += 2;
		}
		return pks;
	}

}

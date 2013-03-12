package jfloppy.audio.tools;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import com.musicg.math.statistics.Sum;

public class SumOfTheLastN extends SpeakerListener {

	ArrayBlockingQueue<double[]> lastn = null;
	
	
	public SumOfTheLastN(int n) {
		super();
		lastn = new ArrayBlockingQueue<double[]>(n);
	}


	@Override
	public void listen(double[] audioData) {
		if(lastn.remainingCapacity()== 0){
			lastn.poll();
		}
		lastn.add(audioData);
		
		double[] meanofN = calcMean();
		
		for (IfaceAudioDataListener listener : listeners) {
			listener.listen(meanofN);
		}
	}
	
	protected double[] calcMean(){
		double[] mean =null;
		for (double[] previous : lastn) {
			if(mean == null){
				mean=Arrays.copyOf(previous, previous.length);
			}else{
				for (int i = 0; i < previous.length; i++) {
					double d = previous[i];
					mean[i] +=d;
				}
			}
			
		}
		
		return mean;
	}

}

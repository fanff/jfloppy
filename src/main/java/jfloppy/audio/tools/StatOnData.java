package jfloppy.audio.tools;

import com.musicg.math.statistics.Mean;
import com.musicg.math.statistics.StandardDeviation;

public class StatOnData extends SpeakerListener {

	Mean mean = new Mean();
	StandardDeviation deviation = new StandardDeviation();
	@Override
	public void listen(double[] audioData) {
		
		double audioDataCount = (double)audioData.length;
		mean.setValues(audioData);
		double meanvalue = mean.evaluate();
		
		deviation.setValues(audioData);
		double standarddeviation = deviation.evaluate();
		
		forAllListenerSpeak(new double[]{audioDataCount,meanvalue,standarddeviation});
		
	}

}

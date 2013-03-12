package jfloppy.audio.tools;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.musicg.dsp.FastFourierTransform;

public class FFTTransform extends SpeakerListener {

	FastFourierTransform fft = new FastFourierTransform();
	
	@Override
	public void listen(double[] audioData) {
		//System.out.println("fft");
		
		double[] magnitudes = fft.getMagnitudes(Arrays.copyOf(audioData, audioData.length));
		
		//System.out.println(""+magnitudes[0]);
		forAllListenerSpeak(magnitudes);

	}

}

package jfloppy.audio.tools;

import jfloppy.audio.JFloppyAudioTB;

public class Signed2Unsigned extends SpeakerListener{

	@Override
	public void listen(double[] audioData) {
		double minValue = audioData[JFloppyAudioTB.minID(audioData)];
		
		double[] outputData = new double[audioData.length];
		for (int i = 0; i < audioData.length; i++) {
			outputData[i] = audioData[i]+minValue;
		}

		for (IfaceAudioDataListener listener : listeners) {
			listener.listen(outputData);
		}
	}

}

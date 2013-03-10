package jfloppy.audio.tools;

import jfloppy.audio.JFloppyAudioTB;

public class Normalize extends SpeakerListener{

	@Override
	public void listen(double[] audioData) {
		double[] norm = JFloppyAudioTB.normalize(audioData);
		for (IfaceAudioDataListener listener : listeners) {
			listener.listen(norm);
		}
		
	}

	
}

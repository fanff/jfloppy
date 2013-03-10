package jfloppy.audio.tools;

public abstract class SpeakerListener extends AbstractAudioSpeaker implements
		IfaceAudioDataListener {

	@Override
	public abstract void listen(double[] audioData) ;

}

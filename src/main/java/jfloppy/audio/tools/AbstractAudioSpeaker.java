package jfloppy.audio.tools;

import java.util.ArrayList;

public abstract class AbstractAudioSpeaker implements IfaceAudioDataSpeaker {

	ArrayList<IfaceAudioDataListener> listeners = new ArrayList<IfaceAudioDataListener>();
	
	
	
	public AbstractAudioSpeaker() {
	}

	@Override
	public void registerListener(IfaceAudioDataListener listener) {
		listeners.add(listener);
		
	}

}

package jfloppy.audio.tools;

import java.util.concurrent.LinkedBlockingQueue;

public class AudioDataBuffer extends SpeakerListener{

	LinkedBlockingQueue<double[]> audioDataBuffer = new LinkedBlockingQueue<double[]>();
	@Override
	public void listen(double[] audioData) {
		try {
			audioDataBuffer.put(audioData);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void doSpeak(){
		double[] audioData = audioDataBuffer.poll();
		if(audioData!=null){
			System.out.println("listenerCount = "+listeners.size());
			System.out.println("buffersize = "+audioDataBuffer.size());
			for (IfaceAudioDataListener listener : listeners) {
				listener.listen(audioData);
			}
		}

	}

}

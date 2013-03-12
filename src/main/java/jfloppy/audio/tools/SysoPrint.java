package jfloppy.audio.tools;

public class SysoPrint implements IfaceAudioDataListener {

	@Override
	public void listen(double[] audioData) {
		System.out.println("audioData ["+audioData.length+"]");
		System.out.println(audioData);
	}

}

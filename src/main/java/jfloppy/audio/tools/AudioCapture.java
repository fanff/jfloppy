package jfloppy.audio.tools;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import jfloppy.audio.JFloppyAudioTB;


public class AudioCapture extends AbstractAudioSpeaker {

	boolean captureProcessFlag = true;
	
	/**
	 *  this is blocking !! 
	 * 
	 */
	public void capture() throws Exception{
		AudioFormat audioFormat  = getAudioFormat();
		DataLine.Info dataLineInfo =
			new DataLine.Info(
					TargetDataLine.class,
					audioFormat);

		TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

		System.out.println("buffersize " + targetDataLine.getBufferSize());

		targetDataLine.open(audioFormat);
		targetDataLine.start();

		byte[] tempBuffer = new byte[ 4096]; 

		//do this in a while
		while(captureProcessFlag){
			targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			//System.out.println("readAudio");
			float[] bufferAsshort = JFloppyAudioTB.extractFloatDataFromAmplitudeByteArray(getAudioFormat(), tempBuffer);
			double [] audioDataLong = JFloppyAudioTB.todouble(bufferAsshort);
			
			// speak !
			for (IfaceAudioDataListener listener : listeners) {
				listener.listen(audioDataLong);
			}
			
		}
	}
	
	
	public AudioFormat getAudioFormat(){
		float sampleRate = 16000;
		//8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		//8,16
		int channels = 1;
		//1,2
		boolean signed = true;
		//true,false
		boolean bigEndian = false;
		//true,false
		return new AudioFormat(sampleRate,
				sampleSizeInBits,
				channels,
				signed,
				bigEndian);
	}//end getAudioFormat
	
	public void stop (){
		captureProcessFlag = false;
	}


	
}

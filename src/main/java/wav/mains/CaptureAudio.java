package wav.mains;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class CaptureAudio {


	public void capture() throws Exception{
		AudioFormat audioFormat  = getAudioFormat();
		DataLine.Info dataLineInfo =
			new DataLine.Info(
					TargetDataLine.class,
					audioFormat);

		TargetDataLine targetDataLine = (TargetDataLine)
		AudioSystem.getLine(dataLineInfo);

		System.out.println("buffersize " + targetDataLine.getBufferSize());
		//do this in a thread
		targetDataLine.open(audioFormat);
		targetDataLine.start();

		byte[] tempBuffer = new byte[10000]; 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//do this in a while
		while(true){
			targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			baos.write(tempBuffer);

			System.out.println("I got" + tempBuffer[10]);
			;
		}
	}
	public AudioFormat getAudioFormat(){
		float sampleRate = 16000F;
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

	/**
	 * @param args
	 * @throws LineUnavailableException 
	 */
	public static void main(String[] args) throws Exception {
		CaptureAudio ca = new CaptureAudio();
		ca.capture();

	}

}

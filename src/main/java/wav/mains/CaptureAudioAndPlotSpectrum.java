package wav.mains;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;

import wav.GenerateWave;
import wav.ShowSpectro;

import com.musicg.dsp.FastFourierTransform;

public class CaptureAudioAndPlotSpectrum {

	FastFourierTransform fft = new FastFourierTransform();

	ShowSpectro showSpectro = new ShowSpectro();
	
	public CaptureAudioAndPlotSpectrum() {
		super();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(showSpectro);
		f.pack();
		f.setVisible(true);
	}
	public void capture() throws Exception{
		AudioFormat audioFormat  = getAudioFormat();
		DataLine.Info dataLineInfo =
			new DataLine.Info(
					TargetDataLine.class,
					audioFormat);

		TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

		System.out.println("buffersize " + targetDataLine.getBufferSize());
		//do this in a thread
		targetDataLine.open(audioFormat);
		targetDataLine.start();

		byte[] tempBuffer = new byte[ 4096]; 
		double[] absoluteSpectrogram ;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//do this in a while
		
		int spectrumCount = 0;
		while(true){
			targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			baos.write(tempBuffer);

			long start = System.currentTimeMillis();
			float[] bufferAsshort = extractFloatDataFromAmplitudeByteArray(getAudioFormat(), tempBuffer);
			double[] bufferAsdouble = GenerateWave.todouble(bufferAsshort);

			System.out.println("shortbufferSize  : "+bufferAsshort.length);
			System.out.println("soublebufferSize : "+bufferAsdouble.length);
			absoluteSpectrogram=fft.getMagnitudes(bufferAsdouble);
			long finish = System.currentTimeMillis();
			absoluteSpectrogram[0] = 0;
			double[] normalizedSpectro = GenerateWave.normalize(absoluteSpectrogram);
			
			System.out.println("max of normalized "+normalizedSpectro[GenerateWave.maxID(normalizedSpectro)]);
			System.out.println("made the fourring in"+(finish-start));

			showSpectro.plotSpectrum(normalizedSpectro, "lol"+spectrumCount);
			System.out.println("I got" + tempBuffer[10]);
			;
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

	public float[] extractFloatDataFromAmplitudeByteArray(AudioFormat format, byte[] audioBytes) {
		// convert
		float[] audioData = null;
		if (format.getSampleSizeInBits() == 16) {
			int nlengthInSamples = audioBytes.length / 2;
			audioData = new float[nlengthInSamples];
			if (format.isBigEndian()) {
				for (int i = 0; i < nlengthInSamples; i++) {
					/* First byte is MSB (high order) */
					int MSB = audioBytes[2 * i];
					/* Second byte is LSB (low order) */
					int LSB = audioBytes[2 * i + 1];
					audioData[i] = MSB << 8 | (255 & LSB);
				}
			}
			else {
				for (int i = 0; i < nlengthInSamples; i++) {
					/* First byte is LSB (low order) */
					int LSB = audioBytes[2 * i];
					/* Second byte is MSB (high order) */
					int MSB = audioBytes[2 * i + 1];
					audioData[i] = MSB << 8 | (255 & LSB);
				}
			}
		}
		else if (format.getSampleSizeInBits() == 8) {
			int nlengthInSamples = audioBytes.length;
			audioData = new float[nlengthInSamples];
			if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
				for (int i = 0; i < audioBytes.length; i++) {
					audioData[i] = audioBytes[i];
				}
			}
			else {
				for (int i = 0; i < audioBytes.length; i++) {
					audioData[i] = audioBytes[i] - 128;
				}
			}
		}// end of if..else
		// System.out.println("PCM Returned===============" +
		// audioData.length);
		return audioData;
	}


	/**
	 * @param args
	 * @throws LineUnavailableException 
	 */
	public static void main(String[] args) throws Exception {
		CaptureAudioAndPlotSpectrum ca = new CaptureAudioAndPlotSpectrum();
		ca.capture();

	}

}

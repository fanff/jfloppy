package wav;

import com.musicg.wave.Wave;
import com.musicg.wave.extension.Spectrogram;

public class LoadSpectrum {
	
	String filename = "./wavbase/flopB2khz.wav";
	
	int size = 1024;
	int overlap = 0;
	public LoadSpectrum(String filename, int size, int overlap) {
		super();
		this.filename = filename;
		this.size = size;
		this.overlap = overlap;
	}
	
	
	public double[][] getSpectrum(){
		
		// create a wave object
		Wave wave = new Wave(filename);

		// print the wave header and info
		System.out.println(wave);
		System.out.println("wave length  "+wave.length());
		
		System.out.println("waveChunkSize "+wave.getWaveHeader().getChunkSize());
		
		Spectrogram spectrogram = new Spectrogram(wave,size,overlap);
		//spectrogram[time][frequency]=intensity
		int frameCount = spectrogram.getNumFrames();
		int freqCount = spectrogram.getNumFrequencyUnit();
		
		System.out.println("NumFrame  " + frameCount);
		System.out.println("freqCount " + freqCount);
		double[][] data = spectrogram.getNormalizedSpectrogramData();
		
		return data;
	}
	

}

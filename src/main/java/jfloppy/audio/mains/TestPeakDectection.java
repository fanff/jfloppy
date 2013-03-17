package jfloppy.audio.mains;

import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.FFTTransform;
import jfloppy.audio.tools.PeakDetection;
import jfloppy.audio.tools.SumOfTheLastN;
import jfloppy.audio.tools.panels.SpectroViewer;

public class TestPeakDectection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AudioCapture acapture = new AudioCapture();
		PeakDetection detection = new PeakDetection();

		FFTTransform fft = new FFTTransform();
		SpectroViewer peakView = new SpectroViewer("view");
		SpectroViewer rawview = new SpectroViewer("Raw");
		SumOfTheLastN sum = new SumOfTheLastN(3);
		
		acapture.registerListener(sum);

		sum.registerListener(fft);
		fft.registerListener(rawview);
		fft.registerListener(detection);
		
		detection.registerListener(peakView);
		
		;
		for(int i : detection.getpks(4)){
			System.out.print(i);
			System.out.println("");
		}
		try {
			acapture.capture();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

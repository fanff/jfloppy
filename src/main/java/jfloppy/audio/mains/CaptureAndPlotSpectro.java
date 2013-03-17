package jfloppy.audio.mains;

import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.FFTTransform;
import jfloppy.audio.tools.Normalize;
import jfloppy.audio.tools.panels.SpectroViewer;

public class CaptureAndPlotSpectro {

	public static void main(String[] args) throws Exception {
		
		AudioCapture acapture = new AudioCapture();
		
		FFTTransform fftt = new FFTTransform();

		Normalize normalizer = new Normalize();
		
		SpectroViewer viewer = new SpectroViewer("viewer");

		acapture.registerListener(fftt);
		
		fftt.registerListener(normalizer);
		normalizer.registerListener(viewer);
		acapture.capture();
	}
}

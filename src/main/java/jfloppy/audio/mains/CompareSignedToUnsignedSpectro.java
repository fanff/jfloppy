package jfloppy.audio.mains;

import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.FFTTransform;
import jfloppy.audio.tools.LinearCombination;
import jfloppy.audio.tools.Normalize;
import jfloppy.audio.tools.Signed2Unsigned;
import jfloppy.audio.tools.SysoPrint;
import jfloppy.audio.tools.panels.SpectroViewer;

public class CompareSignedToUnsignedSpectro {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		
		AudioCapture acapture = new AudioCapture();

		Signed2Unsigned signedToUnsigned = new Signed2Unsigned();
		FFTTransform fftt = new FFTTransform();

		FFTTransform ffttsigned = new FFTTransform();
		
		Normalize normalizer = new Normalize();
		Normalize normalizerSigned = new Normalize();
		Normalize normalizerDiff = new Normalize();
		
		SpectroViewer viewer = new SpectroViewer("Unsigned");
		SpectroViewer viewerSigned = new SpectroViewer("Signed");
		SpectroViewer viewerDifference = new SpectroViewer("Diff");
		
		acapture.registerListener(signedToUnsigned);
		signedToUnsigned.registerListener(fftt);
		fftt.registerListener(normalizer);
		normalizer.registerListener(viewer);

		acapture.registerListener(ffttsigned);
		ffttsigned.registerListener(normalizerSigned);
		normalizerSigned.registerListener(viewerSigned);

		LinearCombination combi = new LinearCombination(
				0d, new double[]{1d,-1d});
		
		combi.registerSpeaker(0, ffttsigned);
		combi.registerSpeaker(1, fftt);
		
		combi.registerListener(normalizerDiff);
		normalizerDiff.registerListener(viewerDifference);
		
		SysoPrint printer = new SysoPrint();
		normalizerDiff.registerListener(printer);

		acapture.capture();
	}

}

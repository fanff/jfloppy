package jfloppy.audio.mains;

import jfloppy.audio.tools.ApplyMask;
import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.Normalize;
import jfloppy.audio.tools.panels.SpectroViewer;

public class TestApplyMask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AudioCapture acapture = new AudioCapture();

		SpectroViewer viewer = new SpectroViewer("view");
		
		ApplyMask mask = new ApplyMask();
		mask.setInvisibleMask(512);
		mask.setBitInMask(50, true);
		mask.setBitInMask(100, true);
		mask.setBitInMask(200, true);
		
		double [] input = new double [512]; 
		for(int i = 0 ; i< input.length ; i++){
			input[i] = 1d;
		}
		Normalize normalizer_capture = new Normalize();
		acapture.registerListener(mask);
		mask.registerListener(normalizer_capture);
		normalizer_capture.registerListener(viewer);
		
		
		mask.listen(input);
		

	}

}

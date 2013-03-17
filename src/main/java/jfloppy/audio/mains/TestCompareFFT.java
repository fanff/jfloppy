package jfloppy.audio.mains;

import javax.swing.JFrame;

import jfloppy.audio.JFloppyAudioTB;
import jfloppy.audio.tools.ApplyMask;
import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.FFTTransform;
import jfloppy.audio.tools.IfaceAudioDataListener;
import jfloppy.audio.tools.IfaceAudioDataSpeaker;
import jfloppy.audio.tools.LinearCombination;
import jfloppy.audio.tools.Normalize;
import jfloppy.audio.tools.StatOnData;
import jfloppy.audio.tools.panels.SpectroViewer;
import jfloppy.audio.tools.panels.VectorPrinterPanel;

public class TestCompareFFT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		AudioCapture acapture = new AudioCapture();

		FFTTransform fft_capture = new FFTTransform();

		Normalize normalizer_capture = new Normalize();
		acapture.registerListener(fft_capture);
		fft_capture.registerListener(normalizedSpectro("rawMic"));
		fft_capture.registerListener(statPrinter("raw"));
		
		
		ApplyMask capture_mask = new ApplyMask();
		capture_mask.setInvisibleMask(512);
		for(int pos = 35 ; pos<42 ; pos++){
			capture_mask.setBitInMask(pos, true);
		}
		fft_capture.registerListener(normalizer_capture);
		normalizer_capture.registerListener(capture_mask);
		
		
		FFTTransform fft_input = new FFTTransform();
		Normalize normalizer_input = new Normalize();
		fft_input.registerListener(normalizedSpectro("generated"));
		fft_input.registerListener(statPrinter("generated"));
		
		fft_input.registerListener(normalizer_input);

		
		LinearCombination difference = new LinearCombination(0.5d, new double[]{1d,-1d});
		difference.registerSpeaker(0, capture_mask);
		difference.registerSpeaker(1, normalizer_input);
		
		
		difference.registerListener(normalizedSpectro("difference"));
		difference.registerListener(statPrinter("difference"));
		
		double[] generatedWave = JFloppyAudioTB.generateSinus(
				44000, 820, 2048);
		fft_input.listen(generatedWave);
		
		
		
		try {
			acapture.capture();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static IfaceAudioDataListener normalizedSpectro(String spectroName){
		Normalize normalizer = new Normalize();
		SpectroViewer viewer = new SpectroViewer(spectroName);
		normalizer.registerListener(viewer);
		return normalizer;

	}

	public static IfaceAudioDataListener statPrinter(String printerName){
		VectorPrinterPanel vpp = new VectorPrinterPanel("size","mean","devi");
		JFrame f = new JFrame(printerName);
		f.add(vpp);
		f.setVisible(true);
		f.pack();

		StatOnData stat = new StatOnData();
		stat.registerListener(vpp);

		return stat;

	}

}

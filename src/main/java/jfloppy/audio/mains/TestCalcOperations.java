package jfloppy.audio.mains;

import javax.swing.JFrame;

import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.IfaceAudioDataListener;
import jfloppy.audio.tools.MultiScalar;
import jfloppy.audio.tools.StatOnData;
import jfloppy.audio.tools.SumOfTheLastN;
import jfloppy.audio.tools.panels.SpectroViewer;
import jfloppy.audio.tools.panels.VectorPrinterPanel;

public class TestCalcOperations {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		SumOfTheLastN summator = new SumOfTheLastN(5);

		
		AudioCapture acapture = new AudioCapture();

		acapture.registerListener(makeProcess1("raw",1d/10000d));
		
		acapture.registerListener(summator);

		summator.registerListener(makeProcess1("summed", 1d/100000d));
		try {
			acapture.capture();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static IfaceAudioDataListener makeProcess1(String frameName,double scalarRatio){
		VectorPrinterPanel vpp = new VectorPrinterPanel("mean","devi");
		
		
		JFrame f = new JFrame();
		f.add(vpp);
		f.setVisible(true);
		f.pack();

		
		StatOnData statOnRaw = new StatOnData();
		MultiScalar scaler = new MultiScalar(scalarRatio);
		SpectroViewer rawDataView = new SpectroViewer(frameName);
		
		statOnRaw.registerListener(scaler);
		
		scaler.registerListener(rawDataView);
		scaler.registerListener(vpp);
		return statOnRaw;

	}

}

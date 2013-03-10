package jfloppy.audio.tools;

import javax.swing.JFrame;

import jfloppy.audio.JFloppyAudioTB;
import jfloppy.audio.SpectroPanel;

public class SpectroViewer implements IfaceAudioDataListener{

	SpectroPanel spectroPanel = new SpectroPanel();
	
	JFrame f = new JFrame();
	String FrameName = "SpectroViewer";
	
	public SpectroViewer(String frameName) {
		super();
		FrameName = frameName;
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(spectroPanel);
		f.pack();
		f.setVisible(true);
		f.setTitle(FrameName);
	}



	@Override
	public void listen(double[] audioData) {
		double maxvalue = audioData[JFloppyAudioTB.maxID(audioData)];
		//System.out.println("viewer "+maxvalue);
		spectroPanel.plotSpectrum(audioData, "dafuck");
	}

	
}

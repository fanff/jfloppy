package jfloppy.audio.tools.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;

import jfloppy.audio.JFloppyAudioTB;
import jfloppy.audio.tools.IfaceAudioDataListener;

public class SpectroViewer extends JPanel implements IfaceAudioDataListener, ActionListener{

	SpectroPanel spectroPanel = new SpectroPanel();
	
	JFrame f = new JFrame();
	String FrameName = "SpectroViewer";
	JButton change = new JButton("set vscale");
	
	JPanel buttonPanel = new JPanel();
	JTextField vscale = new JTextField(10);
	public SpectroViewer(String frameName) {
		super();
		FrameName = frameName;

		vscale.setText("1.0");
		change.addActionListener(this);
		buttonPanel.add(vscale);
		buttonPanel.add(change);
		
		this.setLayout(new BorderLayout());
		this.add(spectroPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
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



	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == change){
			Double newscale = Double.valueOf(vscale.getText());
			
			spectroPanel.setVerticalScale(newscale);
		}
		
	}

	
}

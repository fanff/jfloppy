package jfloppy.audio.tools.panels;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JPanel;
public class SpectroPanel extends JPanel{

	double [] tempspectro = new double[0];
	double vscale = 1;
	public SpectroPanel() {
		super();
		this.setPreferredSize(new Dimension(300,200));
	}

	public void plotSpectrum(double [] spectrum,String name){
		int freqCount = spectrum.length;
		tempspectro = Arrays.copyOf(spectrum, freqCount);
		this.repaint();

	}
	
	public void setVerticalScale(double vscale){
		this.vscale = vscale;
	}
	
	@Override
	public void paint(Graphics g){
		int width = this.getSize().width;
		int height = this.getSize().height;
		g.clearRect(0, 0, width, height);

		double [] toshow = Arrays.copyOf(tempspectro, tempspectro.length);
		for(int i=0; i<toshow.length-1 ; i++ ){
			double valueDoubleori = toshow[i]*vscale;
			double valueDoubledest = toshow[i+1]*vscale;
			int xori = (int) Math.round( ((double)i)*width / ((double)toshow.length)     );
			int xdest = (int) Math.round( ((double)i+1)*width / ((double)toshow.length)     );
			
			
			int yori = height-((int) Math.round( ((double)height)*valueDoubleori  ));
			int ydest = height-((int) Math.round( ((double)height)*valueDoubledest  ));
			g.drawLine(xori, yori, xdest, ydest);
		}
		
	}

}

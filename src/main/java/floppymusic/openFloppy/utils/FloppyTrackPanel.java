package floppymusic.openFloppy.utils;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FloppyTrackPanel extends JPanel {
	
	JPanel placeToDraw = new JPanel();
	
	FloppyTrack track = null;
	public FloppyTrackPanel(FloppyTrack ft) {
		track = ft;
	}
	
	public void paint(Graphics g){
		if(track != null ){
			
			
			int frameHalfHeight = 50;
			int framecenter = this.getHeight()/2;
			
			int up   = framecenter - frameHalfHeight;
			int down = framecenter + frameHalfHeight;
			
			int trackLength = track.getLength()+1;
			
			int width = this.getWidth();
			
			
			g.drawLine(0, framecenter, this.getWidth(), framecenter);
			if(trackLength>0){
				for(FloppyFrame frame : track.getAllFrames()){
					long begin = frame.begin;
					long end = frame.end;
					
					int beginpos = new Long((begin*width)/trackLength).intValue();
					int endpos = new Long((end*width)/trackLength).intValue();

					g.drawRect(beginpos, up, endpos-beginpos, frameHalfHeight*2);
					g.drawString(""+frame.freq,beginpos, up-1);
				}
			}else{
				//trackEmpty
				g.drawString("Empty Track", 0, 10);
			}
			
		}else{
			// no track to draw
			g.drawString("No track", 0, 10);
		}
	}
	
	 public static void main(String[] args) throws Exception {
		 JFrame mainFrame = new JFrame();
		 
		 mainFrame.setPreferredSize(new Dimension(500,500));
		 FloppyTrack ft = new FloppyTrack();


		 ft.setFreqFromTo(5, 10, 1);
		 
		 ft.setFreqFromTo(10, 12, 2);
		 
		 ft.setFreqFromTo(13, 14, 1);
		 
		 ft.setFreqFromTo(15, 17, 2);
		 ft.setFreqFromTo(16, 18, 2);
		 
		 ft.setFreqFromTo(19, 20, 2);
		 
		 ft.setFreqFromTo(21, 25, 1);
		 ft.setFreqFromTo(27, 30, 1);
		 ft.setFreqFromTo(23, 30, 1);
		 
		 JPanel panel = new FloppyTrackPanel(ft);
		 
		 mainFrame.add(panel);
		 
		 mainFrame.setVisible(true);
		 mainFrame.pack();
		 
	 }
}

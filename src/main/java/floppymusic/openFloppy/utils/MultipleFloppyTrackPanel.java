package floppymusic.openFloppy.utils;

import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MultipleFloppyTrackPanel extends JPanel{
	SetOfFloppyTrack alltracks = null;
	public MultipleFloppyTrackPanel() {
		super();
	}

	public void setTracks(SetOfFloppyTrack ft){
		alltracks = ft;
	}
	
	public void paint(Graphics g){
		if(alltracks != null ){
			
			int trackLength = alltracks.longestTrackDuration();
			
			int trackCount = alltracks.getTrackCount();
			
			int width = this.getWidth();
			
			int height = this.getHeight();
			
			int heightByTrack = height/trackCount;
			
			//for each track
			for(int trackID = 0 ; trackID < trackCount ; trackID ++) {
				int framecenter = (heightByTrack*trackID)+heightByTrack/2;
				g.drawLine(0, framecenter, this.getWidth(), framecenter);
				
				FloppyTrack ft = alltracks.getTrack(trackID);
				if(ft!=null && trackLength != 0){
					for(FloppyFrame frame : ft.getAllFrames()){
						long begin = frame.begin;
						long end = frame.end;
						
						int beginpos = new Long((begin*width)/trackLength).intValue();
						int endpos = new Long((end*width)/trackLength).intValue();
						
						int noteH   = heightByTrack/3;
						
						g.drawRect(beginpos, framecenter-noteH, endpos-beginpos,noteH*2);
						g.drawString(""+frame.freq,beginpos, framecenter-noteH-1);
					}
				}
				
			}
		}else{
			// no track to draw
			g.drawString("No track", 0, 10);
		}
	}
	
}

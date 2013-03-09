package floppymusic.openFloppy.panel;

import java.awt.Graphics;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import floppymusic.openFloppy.floppyPlayableNote.FloppyPlayableNote;
import floppymusic.openFloppy.utils.FloppyFrame;
import floppymusic.openFloppy.utils.FloppyTrack;

public class PlayableNotePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8601230772613232879L;

	ConcurrentHashMap<String, FloppyPlayableNote> playableData = new ConcurrentHashMap<String, FloppyPlayableNote>();
	
	public void addData(String floppyID , FloppyPlayableNote fpn){
		playableData.put(floppyID, fpn);
	}
	
	@Override
	public void paint(Graphics g){

		int serieCount = playableData.size();
		
		int width = this.getWidth();
		
		int height = this.getHeight();
		int heightByfloppy = height/serieCount;
		
		int valueHeight   = heightByfloppy*2/3;

		//for each track
		int count = 0;
		
		for(Entry<String, FloppyPlayableNote> e : playableData.entrySet()) {
			
			int framefoot = (heightByfloppy*count)+heightByfloppy;
			
			
			g.drawLine(0, framefoot, this.getWidth(), framefoot);
			
			String floppyName = e.getKey();
			
			FloppyPlayableNote ft = e.getValue();
			if(ft!=null ){
				g.drawString(floppyName ,10, framefoot-valueHeight-2);

				for (int noteID = 0; noteID < 120; noteID++) {
					
					long begin = noteID ;
					long end = noteID+1;
					
					int beginpos = new Long((begin*width)/ft.noteCount()).intValue();
					int endpos = new Long((end*width)/ft.noteCount()).intValue();
					
					if(ft.canPlay(noteID)){
						//g.fillRect(x, y, width, valueHeight)
						//System.out.println("ok");
						g.fillRect(beginpos, framefoot-valueHeight, endpos - beginpos-1, valueHeight);
					}else{
						//System.out.println("not");
						g.fillRect(beginpos, framefoot-5, endpos - beginpos -1,5);
					}
				}
			}
			
			
			count++;
		}
	}
}

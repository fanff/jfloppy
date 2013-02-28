package floppymusic.openFloppy.utils;

import java.util.HashSet;

public class SetOfMidiNotes extends HashSet<FloppyFrame> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5557592013683433060L;

	
	public FloppyFrame getHighestNote(){
		FloppyFrame max =null;
		for(FloppyFrame f : this){
			
			if(max == null || f.freq > max.freq){
				max = f;
			}
		}
		
		return max;
	}
	
	public FloppyFrame getLowestNote(){
		FloppyFrame max =null;
		for(FloppyFrame f : this){
			
			if(max == null || f.freq < max.freq){
				max = f;
			}
		}
		
		return max;
	}
	
	public void applyShifting(int halftone){
		for(FloppyFrame f : this){
			f.freq += halftone;
		}
	}
	
	public void setEveryNoteDuration (int duration){
		for(FloppyFrame f : this){
			f.end = f.begin+duration;
		}
	}
	
	public void reduceEveryNoteDuration (int duration){
		for(FloppyFrame f : this){
			f.end = f.end-duration;
		}
	}
	
	public void setEveryNoteCode (int code){
		for(FloppyFrame f : this){
			f.freq = code;
		}
	}
}

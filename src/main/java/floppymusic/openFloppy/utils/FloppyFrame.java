package floppymusic.openFloppy.utils;

public class FloppyFrame {

	public int begin;
	public int end;
	public int freq;
	
	
	
	public FloppyFrame(int begin, int end, int freq) {
		super();
		this.begin = begin;
		this.end = end;
		this.freq = freq;
	}



	public long duration (){
		return end-begin;
	}
}

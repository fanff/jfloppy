package floppymusic.openFloppy.floppyPlayableNote;

public class HardwareConfig {

	FloppyPlayableNote[] allFloppyPN = new FloppyPlayableNote[6]; 
	
	public HardwareConfig() {
		
		for(int floppyID = 0 ; floppyID < 6 ; floppyID ++){
			FloppyPlayableNote fpn = FloppyPlayableNote.getInstance( String.format("fpnfiles/floppy%s.fpn",floppyID) ) ;
			allFloppyPN[floppyID] = fpn;
		}
		
	}
	
	public FloppyPlayableNote getFloppy(int i){
		FloppyPlayableNote fpn = null ;
		fpn = allFloppyPN[i];

		return fpn;
	}

	
}

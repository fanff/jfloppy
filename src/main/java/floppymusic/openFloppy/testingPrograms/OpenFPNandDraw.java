package floppymusic.openFloppy.testingPrograms;

import javax.swing.JFrame;

import floppymusic.openFloppy.floppyPlayableNote.FloppyPlayableNote;
import floppymusic.openFloppy.panel.PlayableNotePanel;

public class OpenFPNandDraw {

	public static void main(String[] args) throws InterruptedException {
		
		
		JFrame frame = new JFrame();
		PlayableNotePanel pnp = new PlayableNotePanel();

		for(int i = 0 ; i<= 5;i++){
			String fileName = "fpnfiles/floppy"+i;
			fileName+= ".fpn";
			
			
			
			FloppyPlayableNote fpn = FloppyPlayableNote.getInstance(fileName);
			pnp.addData(fileName, fpn);
			
		}
	
		frame.add(pnp);
		frame.setVisible(true);
		frame.pack();
	
	}
	
	
}

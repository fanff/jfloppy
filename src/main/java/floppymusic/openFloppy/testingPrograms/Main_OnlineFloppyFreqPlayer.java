package floppymusic.openFloppy.testingPrograms;

import floppymusic.openFloppy.OnlineFloppyFreqPlayer;
import floppymusic.openFloppy.OnlineFloppyNotePlayer;

public class Main_OnlineFloppyFreqPlayer {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		OnlineFloppyFreqPlayer offp = new OnlineFloppyFreqPlayer("/dev/ttyACM0", 6);
		offp.waitUntilInitFinish();
		
		int floppyID = 3;
		
		int delay = 0 ;
		for(delay = 0; delay <= 2200; delay += 50){
			offp.playThisDelay(floppyID, delay);
			System.out.println("delay is :" + delay);
			Thread.sleep(1000);
		}
		
		
		offp.playThisDelay(floppyID, 0);
		
		
		offp.disconnect();
		
		System.out.println("ENDPROGRAM" );
	}

}

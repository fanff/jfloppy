package floppymusic.openFloppy.testingPrograms;

import floppymusic.openFloppy.FloppyUtilFunc;
import floppymusic.openFloppy.OnlineFloppyFreqPlayer;

public class Main_SendScale {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		OnlineFloppyFreqPlayer offp = new OnlineFloppyFreqPlayer("/dev/ttyACM0", 6);
		offp.waitUntilInitFinish();
		
		int floppyID = 2;
		
		
		int note = 1 ;

		
		int delay = 0;
		for(int start = 24 ; start <= 36 ; start+=1){
			
			for( int i = start ; i<= 72 ; i += 12){
				note = i;
				delay = FloppyUtilFunc.calcCompareFromFreq(FloppyUtilFunc.prescaler,
						FloppyUtilFunc.noteToFreq(note));
				
				
				offp.playThisDelay(1,delay);
				offp.playThisDelay(2,delay);
				offp.playThisDelay(3,delay);
				System.out.println("delay is :" + note);
				Thread.sleep(1000);
			}
		}
		for(note = 1; note <= 120; note += 1){
			delay = FloppyUtilFunc.calcCompareFromFreq(FloppyUtilFunc.prescaler,
					FloppyUtilFunc.noteToFreq(note));
			
			
			offp.playThisDelay(floppyID,delay);
			System.out.println("delay is :" + note);
			Thread.sleep(1000);
		}
		
		
		offp.playThisDelay(floppyID, 0);
		
		
		offp.disconnect();
		
		System.out.println("ENDPROGRAM" );

	}

}

package floppymusic.openFloppy.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import floppymusic.openFloppy.OnlineFloppyNotePlayer;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;

public class Main_PlayFloppyTrack implements ActionListener{
	JFrame frame = null;
	JPanel panel = null;
	JButton buttonStop = new JButton("STOP");
	JButton buttonplay = new JButton("PLAY");
	String deviceName = "/dev/ttyACM0";
	String fileName = "fptsbase/work.fpts";

	int lowestPlayableNote = 1;
	int hiesstPlayableNote = 120 ;

	boolean stopplaying = false;


	public Main_PlayFloppyTrack() {
		super();
		frame = new JFrame();
		panel = new JPanel();
		
		buttonStop.addActionListener(this);
		buttonplay.addActionListener(this);

		panel.add(buttonStop);
		panel.add(buttonplay);
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	public void doProcess(){
		isPlaying = true;
		
		SetOfFloppyTrack allfloppytracks = SetOfFloppyTrack.loadFromFile(fileName);

		//SPEED FACTOR. THE GREATER -> the slower
		double overideSpeedFactor = 0;

		if(allfloppytracks == null) {
			System.err.println("floppyTracks is empty");
			return;
		}
		//creating the player
		System.out.println("Creating fmp");
		OnlineFloppyNotePlayer fmp = new OnlineFloppyNotePlayer(
				deviceName,
				6, //6 floppy 
				118159D, // a exp b -> A
				-0.05976D, // a exp b -> B
				lowestPlayableNote,
				hiesstPlayableNote

		);

		System.out.println("FMP created Waiting for init to finish");
		while (!fmp.initFinished) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		System.out.println("SENDING !!!");

		//making a set of track
		for(int loopcount = 0 ; loopcount < 1 ; loopcount++){
			//preparing the timer
			long originalTime = System.currentTimeMillis();        
			long timer = System.currentTimeMillis() - originalTime;

			int longestTrackDuration = allfloppytracks.longestTrackDuration();


			double speedfactor = allfloppytracks.getDefaultTimeFactor();
			if(overideSpeedFactor > 0d) speedfactor = overideSpeedFactor;

			while(timer <= longestTrackDuration && !stopplaying){

				fmp.playThis(0, allfloppytracks.getFreqForThisTrack(0,(int) timer) ) ;
				fmp.playThis(1, allfloppytracks.getFreqForThisTrack(1,(int) timer) ) ;
				fmp.playThis(2, allfloppytracks.getFreqForThisTrack(2,(int) timer) ) ;
				fmp.playThis(3, allfloppytracks.getFreqForThisTrack(3,(int) timer) ) ;
				fmp.playThis(4, allfloppytracks.getFreqForThisTrack(4,(int) timer) ) ;
				fmp.playThis(5, allfloppytracks.getFreqForThisTrack(5,(int) timer) ) ;


				//        	fmp.playThis(0, allfloppytracks.getFreqForThisTrack(0,(int) timer) ) ;
				//        	fmp.playThis(1, allfloppytracks.getFreqForThisTrack(1,(int) timer) ) ;
				//        	fmp.playThis(2, allfloppytracks.getFreqForThisTrack(2,(int) timer) ) ;
				//        	fmp.playThis(3, allfloppytracks.getFreqForThisTrack(3,(int) timer) ) ;
				//        	fmp.playThis(4, allfloppytracks.getFreqForThisTrack(4,(int) timer) ) ;
				//        	fmp.playThis(5, allfloppytracks.getFreqForThisTrack(5,(int) timer) ) ;
				//fmp.playThis(3, allfloppytracks.getFreqForThisTrack(1,(int) timer) ) ;
				//increase the timer
				double elapsedTimeD = (double) (System.currentTimeMillis() - originalTime ) ;
				elapsedTimeD = elapsedTimeD/speedfactor;
				timer = (int) Math.round(elapsedTimeD);
			}

		}


		System.out.println("END of playing");
		fmp.muteAllFloppy();

		fmp.disconnect();
		
		
		
		
		isPlaying = false;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Main_PlayFloppyTrack mft = new Main_PlayFloppyTrack();
		mft.doProcess();
	}
	
	protected boolean isPlaying = false;
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonStop){
			stopplaying = true;
		}else if(e.getSource() == buttonplay){
			if(!isPlaying){
				stopplaying = false;
				Runnable eeee = new Runnable() {
					@Override
					public void run() {
						doProcess();
						
					}
				};
				
				new Thread(eeee).start();
			}
			//this.doProcess();
		}

	}
}
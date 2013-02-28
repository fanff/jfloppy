package floppymusic.openFloppy;

import java.util.Hashtable;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.PulseOscillator;
import com.jsyn.unitgen.UnitOscillator;


import floppymusic.openFloppy.utils.SetOfFloppyTrack;

public class Main_PlayPerfectWave {
	public boolean initFinished = false;
	Synthesizer synth;
	
	LineOut lineOut;
	Hashtable<Integer, Integer> currentlyPlayedNote = new Hashtable<Integer, Integer>();

	static int floppycount = 20;
	UnitOscillator[] oscs = new UnitOscillator[floppycount];
	public Main_PlayPerfectWave() {
		super();
		
		for(int i = 0 ; i < floppycount ; i ++){
			currentlyPlayedNote.put(i, 0);
		}
		
		
		synth = JSyn.createSynthesizer();

		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();
		synth.add( lineOut = new LineOut() );

		for(int i = 0; i < floppycount ; i++){
			//UnitOscillator osc= new SineOscillator();
			UnitOscillator osc= new PulseOscillator();
			// Add a tone generator.
			synth.add( osc  );
			// Add a stereo audio output unit.
			
			// Connect the oscillator to both channels of the output.
			osc.output.connect( 0, lineOut.input, 0 );
			osc.output.connect( 0, lineOut.input, 1);
			
			// Set the frequency and amplitude for the sine wave.
			osc.frequency.set( 345.0 );
			osc.amplitude.set( 0 );
			
			oscs[i] = osc;
			
		}

		// We only need to start the LineOut. It will pull data from the
		// oscillator.
		lineOut.start();
		
		initFinished = true;
	}

	double calc_a = 32.703196;
	double calc_b = 0.057762265;
	
	public void playThis(int floppID,int freqID){
		
		
		//if the freqID to play is already played, return;
		if(currentlyPlayedNote.get(floppID) == freqID) return;
		
		//calculate the delay to send to the floppy
		double freq = 0.;
		UnitOscillator osc= oscs[floppID] ;

		if(freqID != 0){

			//apply some shifting
			freq = calc_a * Math.exp(calc_b*freqID);
			osc.frequency.set( freq);
			osc.amplitude.set( 0.3);
		}else{
			osc.amplitude.set( 0.);
		}
		
		System.out.println("fid "+ floppID + " freq " +freq );
		
		currentlyPlayedNote.put(floppID, freqID);
	}
	
	public void muteAllFloppy(){
		for(int floppID = 0 ; floppID < floppycount ; floppID ++){
			
			UnitOscillator osc= oscs[floppID] ;
			osc.amplitude.set( 0.);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "fptsbase/work.fpts";

		int lowestPlayableFreq = 68;
		int hiesstPlayableFreq = 80 ;

		SetOfFloppyTrack allfloppytracks = SetOfFloppyTrack.loadFromFile(fileName);

		//SPEED FACTOR. THE GREATER -> the slower
		double overideSpeedFactor = 0;

		if(allfloppytracks == null) {
			System.err.println("floppyTracks is empty");
			return;
		}
		//creating the player
		System.out.println("Creating fmp");
		Main_PlayPerfectWave fmp = new Main_PlayPerfectWave();

		System.out.println("FMP created Waiting for init to finish");
		while (!fmp.initFinished) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("SENDING !!!");

		//making a set of track


		//preparing the timer
		long originalTime = System.currentTimeMillis();        
		long timer = System.currentTimeMillis() - originalTime;

		int longestTrackDuration = allfloppytracks.longestTrackDuration();


		double speedfactor = allfloppytracks.getDefaultTimeFactor();
		if(overideSpeedFactor > 0d) speedfactor = overideSpeedFactor;

		while(timer <= longestTrackDuration){
			for(int i = 0 ; i < floppycount ; i ++){
				fmp.playThis(i, allfloppytracks.getFreqForThisTrack(i,(int) timer) ) ;
			}
			//increase the timer
			double elapsedTimeD = (double) (System.currentTimeMillis() - originalTime ) ;
			elapsedTimeD = elapsedTimeD/speedfactor;
			timer = (int) Math.round(elapsedTimeD);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("END of playing");
		fmp.muteAllFloppy();


	}


}

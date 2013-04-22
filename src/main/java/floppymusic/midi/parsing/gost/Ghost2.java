package floppymusic.midi.parsing.gost;

import java.awt.Dimension;
import java.util.HashMap;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import panel.MultipleXYTrace;

import floppymusic.midi.MidiParser;
import floppymusic.openFloppy.floppyPlayableNote.HardwareConfig;
import floppymusic.openFloppy.panel.MultipleFloppyTrackPanel;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;
import floppymusic.openFloppy.utils.SetOfMidiNotes;
import floppymusic.openFloppy.utils.TrackBuilder;

public class Ghost2 {
	
	public static void main(String[] args) {
		String midiFileName = "midbase/ghost2.mid";

		String destinationFileName = "fptsbase/work.fpts";
		//SPEED FACTOR. THE GREATER -> the slower
		double timeFactorToUse = 5D;

		//creating the six track
		int trackCount = 6;


		//open sequence
		MidiParser parser = new MidiParser();
		Sequence sequence = parser.openFile(midiFileName);
		
		//open floppy playable Notes
		HardwareConfig config = new HardwareConfig();
		
		
		int startShifting = -24;
		int stopShifting = +24;
		
		HashMap<Integer, Integer> remainingNoteByShifting = new HashMap<Integer, Integer>();
		
		for(int shifting = startShifting ; shifting<= stopShifting ; shifting++){
			SetOfFloppyTrack allTrack = new SetOfFloppyTrack(trackCount);
			int remainingNotes = extractingFunction(shifting, sequence, config,allTrack);
			
			remainingNoteByShifting.put(shifting, remainingNotes);
		}

		JFrame frame = new JFrame();
		MultipleXYTrace mxyt = new MultipleXYTrace();
		
		
		frame.add(mxyt);
		
		frame.setVisible(true);
		
		for(int shifting = startShifting ; shifting<= stopShifting ; shifting++){
			mxyt.addValue(shifting, remainingNoteByShifting.get(shifting), "opt", 0);
		}
		
		SetOfFloppyTrack allTrack = new SetOfFloppyTrack(trackCount);
		int generalShift = -11;
		int remainingNotes = extractingFunction(generalShift, sequence, config,allTrack);
		MultipleFloppyTrackPanel mftp = new MultipleFloppyTrackPanel();
		mftp.setTracks(allTrack);


		JFrame frame2 = new JFrame();
		frame2.add(mftp);
		frame2.setPreferredSize(new Dimension(500, 300));
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.pack();
		frame2.setVisible(true);

		allTrack.setDefaultTimeFactor(timeFactorToUse);
		allTrack.saveToFile(destinationFileName);
	}
	public static int extractingFunction(int generalShift, Sequence sequence, HardwareConfig config,SetOfFloppyTrack allFloppyTracks){
		
		
		
		
		
		
		SetOfMidiNotes sax = TrackBuilder.collectUsefullNote(sequence, 2);
		sax.applyShifting(generalShift);
		
		int saxnotAdded = TrackBuilder.accumulate(allFloppyTracks, sax,config);
		
		SetOfMidiNotes sax2 = TrackBuilder.collectUsefullNote(sequence, 1);
		sax2.applyShifting(generalShift);
		int sax2notAdded = TrackBuilder.accumulate(allFloppyTracks, sax2 ,config);
		
		SetOfMidiNotes guitar = TrackBuilder.collectUsefullNote(sequence, 0);
		guitar.applyShifting(generalShift);
		int guitarnotAdded = TrackBuilder.accumulate(allFloppyTracks, guitar,config);

		
		
		return saxnotAdded + sax2notAdded + guitarnotAdded ;
		
	}
	

}

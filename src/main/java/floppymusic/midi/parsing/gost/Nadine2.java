package floppymusic.midi.parsing.gost;

import java.util.HashMap;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import panel.MultipleXYTrace;

import jfloppy.audio.tools.panels.SpectroPanel;

import floppymusic.midi.MidiParser;
import floppymusic.openFloppy.floppyPlayableNote.FloppyPlayableNote;
import floppymusic.openFloppy.floppyPlayableNote.HardwareConfig;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;
import floppymusic.openFloppy.utils.SetOfMidiNotes;
import floppymusic.openFloppy.utils.TrackBuilder;

public class Nadine2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String midiFileName = "midbase/nadine2.mid";

		String destinationFileName = "fptsbase/work.fpts";
		//SPEED FACTOR. THE GREATER -> the slower
		double timeFactorToUse = 0.9D;

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
	}
	
	public static int extractingFunction(int generalShift, Sequence sequence, HardwareConfig config,SetOfFloppyTrack allFloppyTracks){
		SetOfMidiNotes sax = TrackBuilder.collectUsefullNote(sequence, 0);
		sax.applyShifting(generalShift);
		
		int saxnotAdded = TrackBuilder.accumulate(allFloppyTracks, sax,config);
		
		SetOfMidiNotes sax2 = TrackBuilder.collectUsefullNote(sequence, 1);
		sax2.applyShifting(generalShift);
		int sax2notAdded = TrackBuilder.accumulate(allFloppyTracks, sax2 ,config);
		
		SetOfMidiNotes guitar = TrackBuilder.collectUsefullNote(sequence, 2);
		guitar.applyShifting(generalShift);
		int guitarnotAdded = TrackBuilder.accumulate(allFloppyTracks, guitar,config);

		SetOfMidiNotes piano = TrackBuilder.collectUsefullNote(sequence, 3);
		piano.applyShifting(generalShift);
		int pianonotAdded = TrackBuilder.accumulate(allFloppyTracks, piano,config);
		
		
		return saxnotAdded + sax2notAdded + guitarnotAdded + pianonotAdded;
		
	}

}

package floppymusic.midi.parsing;

import java.awt.Dimension;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import floppymusic.midi.MidiParser;
import floppymusic.openFloppy.panel.MultipleFloppyTrackPanel;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;
import floppymusic.openFloppy.utils.SetOfMidiNotes;
import floppymusic.openFloppy.utils.TrackBuilder;

public class TetrisC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String midiFileName = "midbase/tetrisC.mid";

		String destinationFileName = "fptsbase/work.fpts";
		//SPEED FACTOR. THE GREATER -> the slower
		double timeFactorToUse = 0.4D;

		//creating the six track
		int trackCount = 6;
		SetOfFloppyTrack allfloppytracks = new SetOfFloppyTrack(trackCount);

		MidiParser parser = new MidiParser();

		//open sequence
		Sequence sequence = parser.openFile(midiFileName);

		parser.printTrackAndChannel(sequence);
		//parser.printAllFile(s);
		//parser.printUsefullNotes(s, 1, 0);


		//SetOfMidiNotes midiNotes = TrackBuilder.collectUsefullNote(sequence, 15);
		//TrackBuilder.accumulate(allfloppytracks, midiNotes);

		SetOfMidiNotes guitar = TrackBuilder.collectUsefullNote(sequence, 0);
		guitar.applyShifting(-2);
		TrackBuilder.accumulate(allfloppytracks, guitar);
		
		
		SetOfMidiNotes bass2 = TrackBuilder.collectUsefullNote(sequence, 0);
		bass2.applyShifting(-12);
		//TrackBuilder.accumulate(allfloppytracks, bass2);
		
		SetOfMidiNotes bass = TrackBuilder.collectUsefullNote(sequence, 2);
		bass.applyShifting(-2);
		TrackBuilder.accumulate(allfloppytracks, bass);
		
//		
//		TrackBuilder.buildOn(allfloppytracks, sequence, 0);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 1);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 2);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 3);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 4);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 5);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 6);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 7);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 8);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 9);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 10);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 11);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 12);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 13);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 14);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 15);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 17);
//		TrackBuilder.buildOn(allfloppytracks, sequence,  18);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 19);
//		TrackBuilder.buildOn(allfloppytracks, sequence, 21);
		//end of adding midi notes to track.

		MultipleFloppyTrackPanel mftp = new MultipleFloppyTrackPanel();
		mftp.setTracks(allfloppytracks);


		JFrame frame = new JFrame();
		frame.add(mftp);
		frame.setPreferredSize(new Dimension(500, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		allfloppytracks.setDefaultTimeFactor(timeFactorToUse);
		allfloppytracks.saveToFile(destinationFileName);


		System.out.println("SAVED in "+destinationFileName);

	}

}

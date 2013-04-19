package floppymusic.midi.parsing.gost;

import java.awt.Dimension;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import floppymusic.midi.MidiParser;
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
		SetOfFloppyTrack allfloppytracks = new SetOfFloppyTrack(trackCount);

		MidiParser parser = new MidiParser();

		//open sequence
		Sequence sequence = parser.openFile(midiFileName);

		parser.printTrackAndChannel(sequence);
		//parser.printAllFile(s);
		//parser.printUsefullNotes(s, 1, 0);

		int generalShift = -24;
		SetOfMidiNotes guitar = TrackBuilder.collectUsefullNote(sequence, 2);
		guitar.applyShifting(generalShift);
		TrackBuilder.accumulate(allfloppytracks, guitar);

		SetOfMidiNotes sax2 = TrackBuilder.collectUsefullNote(sequence, 1);
		sax2.applyShifting(generalShift);
		TrackBuilder.accumulate(allfloppytracks, sax2);
		
		SetOfMidiNotes track0 = TrackBuilder.collectUsefullNote(sequence, 0);
		track0.applyShifting(generalShift);
		TrackBuilder.accumulate(allfloppytracks, track0);
		
		

		

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

package floppymusic.midi.parsing;

import java.awt.Dimension;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import floppymusic.midi.MidiParser;
import floppymusic.openFloppy.utils.MultipleFloppyTrackPanel;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;
import floppymusic.openFloppy.utils.SetOfMidiNotes;
import floppymusic.openFloppy.utils.TrackBuilder;

public class ParceOrc1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String midiFileName = "midbase/war2/orc1.mid";
		
		String destinationFileName = "fptsbase/work.fpts";
		//SPEED FACTOR. THE GREATER -> the slower
		double timeFactorToUse = 2D;
		
		//creating the six track
		int trackCount = 6;
		SetOfFloppyTrack allfloppytracks = new SetOfFloppyTrack(trackCount);
		
        MidiParser parser = new MidiParser();

        //open sequence
        Sequence sequence = parser.openFile(midiFileName);
        
        parser.printTrackAndChannel(sequence);
        //parser.printAllFile(s);
        //parser.printUsefullNotes(s, 1, 0);
        
        
        
        //SetOfMidiNotes midiNotes = TrackBuilder.collectUsefullNote(sequence, 3);
        //TrackBuilder.accumulate(allfloppytracks, midiNotes);
        
        
        
        TrackBuilder.buildOn(allfloppytracks, sequence, 2);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 3);
        TrackBuilder.buildOn(allfloppytracks, sequence, 5);
        TrackBuilder.buildOn(allfloppytracks, sequence, 6);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 0);
        
        //TrackBuilder.buildOn(allfloppytracks, sequence, 10);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 11);
        
        //TrackBuilder.buildOn(allfloppytracks, sequence, 12);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 13);
        SetOfMidiNotes drums = TrackBuilder.collectUsefullNote(sequence, 9);
        drums.setEveryNoteDuration(20);
        drums.setEveryNoteCode(46);
        TrackBuilder.accumulate(allfloppytracks, drums);
        
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

package floppymusic.midi.parsing;

import java.awt.Dimension;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import floppymusic.midi.MidiParser;
import floppymusic.openFloppy.panel.MultipleFloppyTrackPanel;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;
import floppymusic.openFloppy.utils.SetOfMidiNotes;
import floppymusic.openFloppy.utils.TrackBuilder;

public class ParseAllianc3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String midiFileName = "midbase/war2/allianc3.mid";
		
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
        
        
        
        //TrackBuilder.buildOn(allfloppytracks, sequence, 1);
        TrackBuilder.buildOn(allfloppytracks, sequence, 3);
        TrackBuilder.buildOn(allfloppytracks, sequence, 6);
        TrackBuilder.buildOn(allfloppytracks, sequence, 0);
        
        //TrackBuilder.buildOn(allfloppytracks, sequence, 10);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 11);
        
        //TrackBuilder.buildOn(allfloppytracks, sequence, 12);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 13);
        
        TrackBuilder.buildOn(allfloppytracks, sequence, 9);
        
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

package floppymusic.midi.parsing;

import java.awt.Dimension;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import floppymusic.midi.MidiParser;
import floppymusic.openFloppy.utils.MultipleFloppyTrackPanel;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;
import floppymusic.openFloppy.utils.SetOfMidiNotes;
import floppymusic.openFloppy.utils.TrackBuilder;

public class ParseTiersen_comptine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
String midiFileName = "midbase/other/yann_tiersen-comptine_dun_autre_ete.mid";
		
		String destinationFileName = "fptsbase/work.fpts";
		//SPEED FACTOR. THE GREATER -> the slower
		double timeFactorToUse = 3D;
		
		//creating the six track
		int trackCount = 6;
		SetOfFloppyTrack allfloppytracks = new SetOfFloppyTrack(trackCount);
		
        MidiParser parser = new MidiParser();

        //open sequence
        Sequence sequence = parser.openFile(midiFileName);
        
        parser.printTrackAndChannel(sequence);
        //parser.printAllFile(s);
        //parser.printUsefullNotes(s, 1, 0);
        
        
        SetOfMidiNotes midiNotes = TrackBuilder.collectUsefullNote(sequence, 15);
        if(midiNotes.size()>0){
        	System.out.println("midiNotes size = "+midiNotes.size());
        	System.out.println("midiNotes hiest = "+midiNotes.getHighestNote().freq);
        	System.out.println("midiNotes lowest = "+midiNotes.getLowestNote().freq);
        }
        //TrackBuilder.accumulate(allfloppytracks, midiNotes);
        
        int gentransp = -4;

        SetOfMidiNotes pianoSolo = TrackBuilder.collectUsefullNote(sequence, 0);
        pianoSolo.applyShifting(gentransp);
        //TrackBuilder.accumulate(allfloppytracks, pianoSolo);
        
        SetOfMidiNotes pianogeneral2 = TrackBuilder.collectUsefullNote(sequence, 1);
        pianogeneral2.applyShifting(gentransp);
        TrackBuilder.accumulate(allfloppytracks, pianogeneral2);
        
        SetOfMidiNotes pianoGeneral = TrackBuilder.collectUsefullNote(sequence, 1);
        SetOfFloppyTrack pianoGen = new SetOfFloppyTrack(trackCount);
        pianoGeneral.applyShifting(gentransp);
        TrackBuilder.accumulate(pianoGen, pianoGeneral);
        //TrackBuilder.map(allfloppytracks, pianoGen, 0, 4);
        //TrackBuilder.map(allfloppytracks, allpiano2, 1, 5);
        
        SetOfMidiNotes pianoSolo2 = TrackBuilder.collectUsefullNote(sequence, 0);
        SetOfFloppyTrack allpiano2= new SetOfFloppyTrack(trackCount);
        pianoSolo2.applyShifting(gentransp);
        TrackBuilder.accumulate(allpiano2, pianoSolo2);
        TrackBuilder.map(allfloppytracks, allpiano2, 0, 2);
        TrackBuilder.map(allfloppytracks, allpiano2, 1, 3);
        
		
        //TrackBuilder.buildOn(allfloppytracks, sequence, 5);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 7);
        //TrackBuilder.buildOn(allfloppytracks, sequence, 0);
        
        //TrackBuilder.buildOn(allfloppytracks, sequence, 7);
//      TrackBuilder.buildOn(allfloppytracks, sequence, 12);
//
//      TrackBuilder.buildOn(allfloppytracks, sequence, 14);
//      TrackBuilder.buildOn(allfloppytracks, sequence, 3);
//      TrackBuilder.buildOn(allfloppytracks, sequence, 6 );

       //TrackBuilder.buildOn(allfloppytracks, sequence, 9);
      
        
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

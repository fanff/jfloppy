package floppymusic.midi;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import floppymusic.openFloppy.panel.MultipleFloppyTrackPanel;
import floppymusic.openFloppy.utils.FloppyFrame;
import floppymusic.openFloppy.utils.FloppyTrack;
import floppymusic.openFloppy.utils.SetOfFloppyTrack;

public class MainParseMidiAndBuildSetOfTrack {

	
	public static void main(String[] args) throws Exception {
		
		//String midiFileName = "midbase/tetrisA.mid";
		
		//String midiFileName = "midbase/other/psy-gangnam_style.mid";
		//ring midiFileName = "midbase/guitar.mid";
		//String midiFileName = "midbase/gam.mid";
		//String midiFileName = "midbase/other/yann_tiersen-comptine_dun_autre_ete.mid";
		//String midiFileName = "midbase/other/yann_tiersen-la_valse_damlie_version_piano.mid";
		String midiFileName = "midbase/other/b_fun.mid";
		//String midiFileName = "midbase/other/test.mid";
		//String midiFileName = "midbase/war2/allianc1.mid";
		// String midiFileName = "midbase/war2/allianc2.mid";
		//String midiFileName = "midbase/Final_Fantasy_4_(US_FF2)/FF4betra.mid"; //multiple TRACK !
		
		
		String destinationFileName = "fptsbase/work.fpts";
		//SPEED FACTOR. THE GREATER -> the slower
		double timeFactorToUse = 0.5D;
		
		ArrayList<Integer> channelsToUse = 
			MidiParser.channel(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15); //all channels
			//MidiParser.channel( 2  ,4, 6,7, 13,14 ); //remove 1 3 5 9 10 12 15
		
			//MidiParser.channel(1,2,3,4 ,5,6,7,8, 10,11,12,13,14); //all channels
			//MidiParser.channel(0,   2  ,4, 6,7,8 ,9 ,11, 13,14 ); 
			//MidiParser.channel(0,7,14); //remove all but  0 7 14
			//MidiParser.channel(1,5,8,12); //
			//MidiParser.channel(1,2,3,4,6,7,8,9,10,11,12,13,14,15); //
		//creating the six track
		int trackCount = 6;
		
        MidiParser parser = new MidiParser();
        Sequence sequence = parser.openFile(midiFileName);
        
        parser.printTrackAndChannel(sequence);
        //parser.printAllFile(s);
        //parser.printUsefullNotes(s, 1, 0);
        
        HashMap<Integer,Set<FloppyFrame>> usefullNoteList = 
        	parser.collectUsefullNoteList(sequence, channelsToUse);
        
        
        FloppyTrack[] alltracks = new FloppyTrack[trackCount];
        for(int i = 0 ; i<trackCount; i++){
        	alltracks[i] = new FloppyTrack();
        }
        
        //for every channel of the midi file, add the content to one of the possible floppyTrack
        for(Entry<Integer,Set<FloppyFrame>> anentry : usefullNoteList.entrySet()){
        	int channel = anentry.getKey();
        	Set<FloppyFrame> fFramesonChannel = anentry.getValue();
        	
        	System.out.println("ch"+ channel +"\t"+fFramesonChannel.size()+"");
        	
        	for(FloppyFrame frame : fFramesonChannel){
        		boolean frameIsAdded = false;
        		int priority = 0;
        		while(!frameIsAdded && priority < trackCount){
        			if(alltracks[priority].canSetFreqFromTo(frame.begin, frame.end, frame.freq)){
        				//I can add the frame !!!
        				frameIsAdded = true;
        				alltracks[priority].setFreqFromTo(frame.begin, frame.end, frame.freq);
        			}else{
        				//can not add the frame, increase priority
        				priority++;
        			}
        		}
        		
        		if(frameIsAdded == false){
        			System.out.println("!!! can not add note. priority is "+priority);
        		}
        	}
        }

        //end of adding midi notes to track.
        // making a set of track 
        
        SetOfFloppyTrack allfloppytracks = new SetOfFloppyTrack(alltracks);
        
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

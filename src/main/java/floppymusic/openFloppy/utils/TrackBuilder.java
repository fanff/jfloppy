package floppymusic.openFloppy.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import floppymusic.midi.MidiParser;

public class TrackBuilder {
	public static final HashSet<String> noteName = new HashSet<String>();
	static {
		noteName.add("A1");
		noteName.add("A1#");
		noteName.add("B1");
		noteName.add("C2");
		noteName.add("C2#");
		noteName.add("D2");
		noteName.add("D2#");
		noteName.add("E2");
		noteName.add("F2");
		noteName.add("F2#");
		noteName.add("G2");
		noteName.add("G2#");
		noteName.add("A2");
		noteName.add("A2#");
		noteName.add("B2");
		noteName.add("C3");
		noteName.add("C3#");
		noteName.add("D3");
		noteName.add("D3#");
		noteName.add("E3");
		noteName.add("F3");
		noteName.add("F3#");
		noteName.add("G3");
		noteName.add("G3#");
		noteName.add("A3");
		noteName.add("A3#");
		noteName.add("B3");
		noteName.add("C4");
		noteName.add("C4#");
		noteName.add("D4");
		noteName.add("D4#");
	}
	
	public static final LinkedHashMap<String, Double> noteFreq = new LinkedHashMap<String, Double>();
	static {
		noteFreq.put("A1", 55.00d);	
		noteFreq.put("A1#", 58.27d); 	
		noteFreq.put("B1", 61.74d); 
		noteFreq.put("C2", 65.41d); 
		noteFreq.put("C2#",	69.30d); 	
		noteFreq.put("D2", 73.42d); 	
		noteFreq.put("D2#",	77.78d); 	
		noteFreq.put("E2", 82.41d); 	
		noteFreq.put("F2", 87.31d);	
		noteFreq.put("F2#",	92.50d);	
		noteFreq.put("G2",	98.00d); 	
		noteFreq.put("G2#",	103.83d); 	
		noteFreq.put("A2", 110.00d);
		noteFreq.put("A2#", 116.54d);
		noteFreq.put("B2", 123.47d);
		noteFreq.put("C3", 130.81d);
		noteFreq.put("C3#", 138.59d);
		noteFreq.put("D3", 146.83);
		noteFreq.put("D3#", 155.56d);
		noteFreq.put("E3", 164.81);
		noteFreq.put("F3", 174.61);
		noteFreq.put("F3#", 185.00);
		noteFreq.put("G3", 196.00);
		noteFreq.put("G3#", 207.65);
		noteFreq.put("A3", 220.00);
		noteFreq.put("A3#", 233.08d);
		noteFreq.put("B3", 246.94d);
		noteFreq.put("C4", 261.63d);
		noteFreq.put("C4#", 277.18d);
		noteFreq.put("D4", 293.66d);
		noteFreq.put("D4#", 311.13d);
	}
	public static SetOfFloppyTrack accumulate(
			SetOfFloppyTrack inFloppyTrack, 
			SetOfMidiNotes midiNotes
			){
		
		for(FloppyFrame frame : midiNotes){
    		boolean frameIsAdded = false;
    		int priority = 0;
    		while(!frameIsAdded && priority < inFloppyTrack.getTrackCount()){
    			if(inFloppyTrack.getTrack(priority).canSetFreqFromTo(frame.begin, frame.end, frame.freq)){
    				//I can add the frame !!!
    				frameIsAdded = true;
    				inFloppyTrack.getTrack(priority).setFreqFromTo(frame.begin, frame.end, frame.freq);
    			}else{
    				//can not add the frame, increase priority
    				priority++;
    			}
    		}
    		
    		if(frameIsAdded == false){
    			System.out.println("!!! can not add note. priority is "+priority);
    		}
    	}
		
		return inFloppyTrack;
	}
	
	public static SetOfFloppyTrack map(
			SetOfFloppyTrack dest,
			SetOfFloppyTrack source,
			int idInSource,
			int idOutDest
	){
		
		dest.alltracks[idOutDest] = source.alltracks[idInSource];
		return dest;
	}
	
	
	
	
	public static SetOfFloppyTrack buildOn(
			SetOfFloppyTrack inFloppyTrack, 
			Sequence midiSequence, 
			int channel ){
		
		SetOfMidiNotes notesFromMidi = collectUsefullNote(midiSequence, channel);
		
		return accumulate(inFloppyTrack, notesFromMidi);
	}
	
	public static  SetOfMidiNotes  collectUsefullNote(
			Sequence sequence,
			int usedChannel){

		SetOfMidiNotes startedNote = new SetOfMidiNotes();
		SetOfMidiNotes stopedNote = new SetOfMidiNotes();

		if(sequence == null) return null;

		int currentTrack = 0;
		for (Track track :  sequence.getTracks()) {
			System.out.println();
			System.out.println("Track " + currentTrack + ": size = " + track.size());
			for (int i=0; i < track.size(); i++) { 
				//Thread.sleep(1000);
				MidiEvent event = track.get(i);
				long tickasLong = event.getTick();
				int tick = (int) tickasLong;

				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;

					if (sm.getCommand() == MidiParser.NOTE_ON) {
						int key = sm.getData1();
						int octave = (key / 12)-1;
						int note = key % 12;
						String noteName = MidiParser.NOTE_NAMES[note];
						int velocity = sm.getData2();
						int channel = sm.getChannel();


						//add this note to the noteStarted
						if(usedChannel== channel){
							//System.out.println("adding");
							startedNote.add(new FloppyFrame(tick, 0, key));
						}

					}else if(sm.getCommand() == MidiParser.NOTE_OFF){
						int key = sm.getData1();
						int octave = (key / 12)-1;
						int note = key % 12;
						String noteName = MidiParser.NOTE_NAMES[note];
						int velocity = sm.getData2();
						int channel = sm.getChannel();

						if(usedChannel == channel){
							//System.out.println("removing");
							//find the corresponding startedNote
							FloppyFrame founded = null;
							for(FloppyFrame f : startedNote){
								if(f.freq == key) founded = f;
							}

							if(founded == null){
								System.err.println("no started note found for this stop note ");
							}else{
								//set the endo of the frame, 
								founded.end = tick;
								//remove the frame from the set and add it to the stopedNote;
								startedNote.remove(founded);
								stopedNote.add(founded);
							}


						}
					}else {
						if(usedChannel== sm.getChannel()){
							System.out.println("ch"+sm.getChannel()+" com: "+sm.getCommand());
						}
					}

				}else if (message instanceof MetaMessage) {
					MetaMessage mm = (MetaMessage) message;
					System.out.println("meta "+"type: "+mm.getType());
				}
			}
			currentTrack++;
		}

		//check that there is no remaining startedNote
		if(startedNote.size() != 0 ){
			System.out.println("there is some remaining started note "+startedNote.size());
			
			System.out.println("stopped size :  "+stopedNote.size());
			
			for(FloppyFrame note : startedNote ){
				note.end = note.begin+30;
				stopedNote.add(note);
			}
		}

		return stopedNote;

	}

}

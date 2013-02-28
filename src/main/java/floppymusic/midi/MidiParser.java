package floppymusic.midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import floppymusic.openFloppy.utils.FloppyFrame;

public class MidiParser {
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	public Sequence openFile(String name){
		Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(new File(name));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sequence;
	}

	public void printTrackAndChannel(Sequence sequence){
		if(sequence == null) return;

		int trackCount = sequence.getTracks().length;
		Track [] tracks = sequence.getTracks();
		System.out.println("Found "+trackCount + " track");
		for (int trackid = 0 ; trackid < trackCount ; trackid++) {
			System.out.println("ID "+trackid + " size  : "+tracks[trackid].size());
		}
	}

	public void printAllFile(Sequence sequence){
		if(sequence == null) return;

		int trackNumber = 0;
		for (Track track :  sequence.getTracks()) {
			trackNumber++;
			System.out.println("Track " + trackNumber + ": size = " + track.size());
			System.out.println();

			for (int i=0; i < track.size(); i++) { 
				//Thread.sleep(1000);
				MidiEvent event = track.get(i);
				System.out.print("@" + event.getTick() + " ");
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;
					System.out.print("Channel: " + sm.getChannel() + " ");
					if (sm.getCommand() == NOTE_ON) {
						int key = sm.getData1();
						int octave = (key / 12)-1;
						int note = key % 12;
						String noteName = NOTE_NAMES[note];
						int velocity = sm.getData2();
						System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
					} else if (sm.getCommand() == NOTE_OFF) {
						int key = sm.getData1();
						int octave = (key / 12)-1;
						int note = key % 12;
						String noteName = NOTE_NAMES[note];
						int velocity = sm.getData2();
						System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
					} else {
						System.out.println("Command:" + sm.getCommand());
					}
				} else {
					System.out.println("Other message: " + message.getClass());
				}
			}

			System.out.println();
		}
	}
	public void printUsefullNotes(Sequence sequence,int tracknumber,int channel){
		if(sequence == null) return;

		int trackNumber = 0;
		for (Track track :  sequence.getTracks()) {
			trackNumber++;

			if(tracknumber == trackNumber){

				System.out.println("Track " + trackNumber + ": size = " + track.size());
				System.out.println();

				for (int i=0; i < track.size(); i++) { 
					//Thread.sleep(1000);
					MidiEvent event = track.get(i);
					MidiMessage message = event.getMessage();
					if (message instanceof ShortMessage) {
						ShortMessage sm = (ShortMessage) message;

						if (sm.getCommand() == NOTE_ON) {
							int key = sm.getData1();
							int octave = (key / 12)-1;
							int note = key % 12;
							String noteName = NOTE_NAMES[note];
							int velocity = sm.getData2();
							System.out.print("@" + event.getTick() + " Channel: " + sm.getChannel() + " ");
							System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
						}else if(sm.getCommand() == NOTE_OFF){
							int key = sm.getData1();
							int octave = (key / 12)-1;
							int note = key % 12;
							String noteName = NOTE_NAMES[note];
							int velocity = sm.getData2();
							System.out.print("@" + event.getTick() + " Channel: " + sm.getChannel() + " ");
							System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);

						}
					}
				}
			}
		}

	}

	public HashMap<Integer,Set<FloppyFrame>> collectUsefullNoteList(Sequence sequence,ArrayList<Integer> usedChannel){

		HashMap<Integer,Set<FloppyFrame>> startedNote = new HashMap<Integer,Set<FloppyFrame>>();
		HashMap<Integer,Set<FloppyFrame>> stopedNote = new HashMap<Integer,Set<FloppyFrame>>();

		//for every usedChannel, create a Set Of Frame
		for(int i : usedChannel){
			startedNote.put(i, new HashSet<FloppyFrame>());
			stopedNote.put(i, new HashSet<FloppyFrame>());
		}

		if(sequence == null) return null;

		int currentTrack = 0;
		for (Track track :  sequence.getTracks()) {


			System.out.println("Track " + currentTrack + ": size = " + track.size());
			System.out.println();

			for (int i=0; i < track.size(); i++) { 
				//Thread.sleep(1000);
				MidiEvent event = track.get(i);
				long tickasLong = event.getTick();
				int tick = (int) tickasLong;

				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;

					if (sm.getCommand() == NOTE_ON) {
						int key = sm.getData1();
						int octave = (key / 12)-1;
						int note = key % 12;
						String noteName = NOTE_NAMES[note];
						int velocity = sm.getData2();
						int channel = sm.getChannel();


						//add this note to the noteStarted
						if(usedChannel.contains(channel)){
							//System.out.println("adding");
							startedNote.get(channel).add(new FloppyFrame(tick, 0, key));
						}

					}else if(sm.getCommand() == NOTE_OFF){
						int key = sm.getData1();
						int octave = (key / 12)-1;
						int note = key % 12;
						String noteName = NOTE_NAMES[note];
						int velocity = sm.getData2();
						int channel = sm.getChannel();

						if(usedChannel.contains(channel)){
							//System.out.println("removing");
							//find the corresponding startedNote
							FloppyFrame founded = null;
							for(FloppyFrame f : startedNote.get(channel)){
								if(f.freq == key) founded = f;
							}

							if(founded == null){
								System.err.println("no started note found for this stop note ");
							}else{
								//set the endo of the frame, 
								founded.end = tick;
								//remove the frame from the set and add it to the stopedNote;
								startedNote.get(channel).remove(founded);
								stopedNote.get(channel).add(founded);
							}


						}
					}else {
						//else if(sm.getCommand()sm.getCommand() == 64 
						System.out.println("ch"+sm.getChannel()+" com: "+sm.getCommand());
					}
				}else if (message instanceof MetaMessage) {
					MetaMessage mm = (MetaMessage) message;
					System.out.println("meta "+"type: "+mm.getType());
				}
			}
			currentTrack++;
		}

		//check that there is no remaining startedNote
		for(int i : usedChannel){
			if(startedNote.get(i).size() != 0 ){
				System.out.println("there is some remaining note");
			}
		}

		return stopedNote;

	}

	public static ArrayList<Integer> channel(int... ints){
		ArrayList<Integer> chan = new ArrayList<Integer>();
		for(int i :ints ){
			chan.add(i);
		}
		return chan;
	}
	
	public static void main(String[] args) throws Exception {
		MidiParser parser = new MidiParser();
		String midiFileName = "midbase/other/yann_tiersen-la_valse_damlie_version_piano.mid";
		Sequence s = parser.openFile(midiFileName);

		parser.printTrackAndChannel(s);
		//parser.printAllFile(s);
		parser.printUsefullNotes(s, 2, 0);

		ArrayList<Integer> allchannel = new ArrayList<Integer>();
		for(int i = 0 ; i<= 15; i++){
			allchannel.add(i);
		}
		
		HashMap<Integer,Set<FloppyFrame>> usefullNoteList = parser.collectUsefullNoteList(s, MidiParser.channel(0));
	}
}

package floppymusic.midi;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class PlayMidi {

	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException{
	    Sequence sequence = MidiSystem.getSequence(new File("midbase/gam.mid"));

	    // Create a sequencer for the sequence
	    Sequencer sequencer = MidiSystem.getSequencer();
	    sequencer.open();
	    sequencer.setSequence(sequence);

	    // Start playing
	    sequencer.start();
	    }

}

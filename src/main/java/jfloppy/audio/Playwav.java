package jfloppy.audio;

import java.util.Random;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.examples.PlayTone;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitOscillator;

public class Playwav {

	Synthesizer synth;
	UnitOscillator osc;
	LineOut lineOut;
	Random rand = new Random();
	public void test() throws Exception
	{
		// Create a context for the synthesizer.
		synth = JSyn.createSynthesizer();
		
		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();

		// Add a tone generator.
		synth.add( osc = new SineOscillator() );
		// Add a stereo audio output unit.
		synth.add( lineOut = new LineOut() );
		
		// Connect the oscillator to both channels of the output.
		osc.output.connect( 0, lineOut.input, 0 );
		osc.output.connect( 0, lineOut.input, 1 );

		// Set the frequency and amplitude for the sine wave.
		osc.frequency.set( 880.0 );
		osc.amplitude.set( 0. );

		// We only need to start the LineOut. It will pull data from the
		// oscillator.
		lineOut.start();

		System.out.println( "You should now be hearing a sine wave. ---------" );


			
			
			
			
			osc.amplitude.set( 1. );
			//osc.phase.set(1.);
			for(int i = 0 ; i< 100000000 ; i++){
				osc.frequency.set( 440);
				// Sleep while the sound is generated in the background.
				// Sleep for a few seconds.
				//synth.sleepUntil( synth.getCurrentTime() + 0.01 );
				//osc.frequency.set( 440.0 );
				synth.sleepUntil( synth.getCurrentTime() + 0.001 );
				
			}
		System.out.println( "Stop playing. -------------------" );
		// Stop everything.
		synth.stop();
	}
	
	public double randombetween(double min,double max){
		return (rand.nextDouble()*(max-min)) +min;
	}
	public static void main( String[] args )
	{
		try
		{
			new Playwav().test();
			
		}catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}

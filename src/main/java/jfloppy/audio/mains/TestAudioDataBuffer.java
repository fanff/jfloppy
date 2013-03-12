package jfloppy.audio.mains;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jfloppy.audio.tools.AudioCapture;
import jfloppy.audio.tools.AudioDataBuffer;
import jfloppy.audio.tools.FFTTransform;
import jfloppy.audio.tools.SumOfTheLastN;
import jfloppy.audio.tools.Normalize;
import jfloppy.audio.tools.SpectroViewer;

public class TestAudioDataBuffer {
	ExecutorService tpe = Executors.newFixedThreadPool(20);
	
	public void doit(){
		AudioCapture acapture = new AudioCapture();
		

		

		AudioDataBuffer databuffer = new AudioDataBuffer();

		acapture.registerListener(databuffer);
		
		for (int i = 0; i < 10; i++) {
			FFTTransform fftt = new FFTTransform();
			Normalize normalizer = new Normalize();
			SpectroViewer viewer = new SpectroViewer("viewer"+i);
			SumOfTheLastN mean = new SumOfTheLastN(i+1);
			
			
			databuffer.registerListener(fftt);
			fftt.registerListener(normalizer);
			normalizer.registerListener(mean);
			mean.registerListener(viewer);
		}
		
		tpe.execute(new Run(databuffer));
		
		try {
			acapture.capture();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public class Run implements Runnable{
		AudioDataBuffer databuffer;
		
		public Run(AudioDataBuffer databuffer) {
			super();
			this.databuffer = databuffer;
		}

		@Override
		public void run() {
			while (true) {
				databuffer.doSpeak();
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	public static void main(String[] args) throws Exception {
		new TestAudioDataBuffer().doit();
		
		
	}
	
}

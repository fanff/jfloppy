package floppymusic.openFloppy.utils.someFun;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import floppymusic.openFloppy.panel.FloppyTrackPanel;
import floppymusic.openFloppy.utils.FloppyFrame;
import floppymusic.openFloppy.utils.FloppyTrack;


public class Main_RandomTrack {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		 JFrame mainFrame = new JFrame();
		 FloppyTrack ft = new FloppyTrack();


		 Random rand = new Random();
		 
		 int tickCount = 100;
		 int toadd = 5000;
		 
		 ft.setFreqFromTo(0, 1, 0);
		 boolean ret = ft.setFreqFromTo(tickCount, tickCount+1, 0);
		 
		 System.out.println("ret is "+ret);

		 for(int i = 1; i <= toadd; i ++) {
			 
			 int  begin  = rand.nextInt(80);
			 int duration = rand.nextInt(20);
			 
			 ft.setFreqFromTo(begin, begin+duration, duration);
		 }
		 
		 
		 
		JPanel panel = new FloppyTrackPanel(ft);
		 
		 mainFrame.add(panel);
		 
		 mainFrame.setVisible(true);
		 mainFrame.pack();

	}

}

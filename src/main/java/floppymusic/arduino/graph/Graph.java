package floppymusic.arduino.graph;

import java.awt.Dimension;

import javax.swing.JFrame;

import floppymusic.openFloppy.FloppyUtilFunc;

import panel.MultipleTimeTrace;

public class Graph {

	public static double arduinoclockSpeed = 16000000D;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultipleTimeTrace mtt = new MultipleTimeTrace();
		
		/*
		(timer speed (Hz)) = (Arduino clock speed (16MHz)) / prescaler

		So a 1 prescaler will increment the counter at 16MHz, an 8 prescaler will increment it at 2MHz, a 64 prescaler = 250kHz, and so on.  As indicated in the table above, the prescaler can equal 1, 8, 64, 256, and 1024.  (I'll explain the meaning of CS12, CS11, and CS10 in the next step.) 

		Now you can calculate the interrupt frequency with the following equation:

		interrupt frequency (Hz) = (Arduino clock speed 16,000,000Hz) / (prescaler * (compare match register + 1))
		the +1 is in there because the compare match register is zero indexed

		rearranging the equation above, you can solve for the compare match register value that will give your desired interrupt frequency:

		compare match register = [ 16,000,000Hz/ (prescaler * desired interrupt frequency) ] - 1
		 */
		
		
		
		double prescaler;
		
		int compare;
		
		double freq ;
		//addToTrace(16, "test", mtt);
		for(int frequency = 125 ; frequency <= 3500 ; frequency+=1 ){
			mtt.addValue(frequency, FloppyUtilFunc.calcCompareFromFreq(4, frequency), "x:Freq y:pause 4");
		}
		for(int frequency = 125 ; frequency <= 3500 ; frequency+=2 ){
			mtt.addValue(frequency, FloppyUtilFunc.calcCompareFromFreq(4, frequency), "x:Freq y:pause 4");
		}
		
		for(int frequency = 125 ; frequency <= 3500 ; frequency+=2 ){
			mtt.addValue(frequency, FloppyUtilFunc.calcCompareFromFreq(8, frequency), "x:Freq y:pause 8");
		}
		
		for(int frequency = 125 ; frequency <= 3500 ; frequency+=2 ){
			mtt.addValue(frequency, FloppyUtilFunc.calcCompareFromFreq(16, frequency), "x:Freq y:pause 16");
		}
		
		
		
		JFrame frame = new JFrame();
		frame.add(mtt);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void addToTrace (int prescaler,String prefix,MultipleTimeTrace mtt){

		double maxCompare = 65536.;
		
		for(int compare = 0;compare<=maxCompare;compare+= 100){
			double freq = FloppyUtilFunc.calcFreq(prescaler, compare);
			
			mtt.addValue(compare, freq, prefix+prescaler);
			mtt.addValue(compare, 1./freq, "pause in sec");
		}
	}

}

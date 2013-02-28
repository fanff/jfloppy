package floppymusic.openFloppy;

public class FloppyUtilFunc {
	
	public static double arduinoclockSpeed = 16000000D;
	public static int prescaler = 8 ;
	
	public static double calcFreq(int prescaler, int compare){
		return (arduinoclockSpeed/prescaler)/(compare+1);
	}
	
	public static int calcCompareFromFreq(int prescaler, double frequency){
		return (int) Math.round(( arduinoclockSpeed/ (prescaler * frequency) ) - 1 ); 
	}
	
	public static double noteToFreq( int noteID){
		double calc_a = 32.703196;
		double calc_b = 0.057762265;
		
		double freq = calc_a * Math.exp(calc_b*noteID);
		return freq;
		
	}
}

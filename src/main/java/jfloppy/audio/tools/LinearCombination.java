package jfloppy.audio.tools;


public class LinearCombination extends MultipleListenerSpeaker {

	double[][] inputbuffer ;
	
	double scalar ; 
	double[] weights ;
	
	
	int inputsize = 0;
	
	public LinearCombination(double scalar, double... weights) {
		super();
		this.scalar = scalar;
		this.weights = weights;
		inputbuffer = new double[weights.length][];
	}



	@Override
	public void listen(int id, double[] data) {
		inputsize = data.length;
		try {
			inputbuffer[id] = data;

			double [] result = calccombi();
			
			forAllListenerSpeak(result);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public double [] calccombi() throws Exception{
		double [] result = new double[inputsize];
		for (int pos = 0; pos < inputsize; pos++) {
			result[pos] = scalar;

			for (int i = 0; i < weights.length; i++) {
				result[pos] = result[pos] +weights[i]*inputbuffer[i][pos] ;
			}
		}
		
		return result;
	}


}

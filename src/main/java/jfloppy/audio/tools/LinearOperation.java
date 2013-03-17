package jfloppy.audio.tools;


public class LinearOperation extends SpeakerListener {

	double scalar,offset ;
	
	public LinearOperation(double scalar, double offset) {
		this.scalar = scalar;
		this.offset = offset;
	}

	@Override
	public void listen(double[] audioData) {
		double[] result = new double[audioData.length];
		for (int i=0; i< audioData.length; i++) {
			result[i] = (audioData[i]*scalar) +offset ;
		}
		
		forAllListenerSpeak(result);

	}

}

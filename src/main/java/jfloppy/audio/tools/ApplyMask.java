package jfloppy.audio.tools;

public class ApplyMask extends SpeakerListener{

	double[] mask =null;


	@Override
	public void listen(double[] audioData) {
		if(mask != null){
			double [] output = new double[audioData.length];
			for(int i = 0 ; i< audioData.length; i++){
				if(mask[i]>0 ){
					output[i] = audioData[i];
				}else{
					output[i] = 0d;
				}
			}
			
			forAllListenerSpeak(output);
		}

	}

	public void setMask(double[] mask){
		this.mask = mask;
	}
	
	public void setInvisibleMask(int size){
		mask = new double[size];
		for(int i = 0 ; i<size ; i++){
			mask[i] =  0d;
		}
	}

	public void setBitInMask(int pos , boolean visible){
		if(mask != null){
			mask[pos] = visible ? 1d : 0d;
		}
	}

}

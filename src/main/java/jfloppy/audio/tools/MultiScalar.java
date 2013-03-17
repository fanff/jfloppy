package jfloppy.audio.tools;


public class MultiScalar extends LinearOperation {

	double scalar = 1d;
	
	public MultiScalar(double scalar) {
		super(scalar,0);
		this.scalar = scalar;
	}


}

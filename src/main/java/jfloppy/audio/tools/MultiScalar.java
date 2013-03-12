package jfloppy.audio.tools;

import jfloppy.audio.tools.panels.LinearOperation;

public class MultiScalar extends LinearOperation {

	double scalar = 1d;
	
	public MultiScalar(double scalar) {
		super(scalar,0);
		this.scalar = scalar;
	}


}

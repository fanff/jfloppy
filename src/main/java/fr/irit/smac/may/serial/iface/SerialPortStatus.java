package fr.irit.smac.may.serial.iface;

import java.io.InputStream;

public interface SerialPortStatus {
	
	public boolean isReadyToSend();
	
	public boolean connect(String port,int arg0,int arg1,int arg2,int arg3);
	
	public boolean disconnect();
	
	public InputStream getInputStream();
}

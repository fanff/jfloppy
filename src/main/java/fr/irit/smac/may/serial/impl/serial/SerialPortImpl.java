package fr.irit.smac.may.serial.impl.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.irit.smac.may.serial.SerialPortComp;
import fr.irit.smac.may.serial.iface.SendPort;
import fr.irit.smac.may.serial.iface.SerialPortStatus;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import gnu.io.SerialPort;

public class SerialPortImpl extends SerialPortComp {

	CommPort comPort = null;

	OutputStream output = null;
	InputStream input = null;
	
	@Override
	protected SerialPortStatus statusPort() {
		return new SerialPortStatus() {

			public boolean isReadyToSend() {
				return (comPort != null && output != null && input != null);
			}

			public boolean connect(String portName, int baudRate, int arg1, int arg2,
					int arg3) {
				try {
					CommPortIdentifier p = CommPortIdentifier.getPortIdentifier(portName);

					if(p.isCurrentlyOwned()){
						System.out.println("Already opened");
					}else{
						comPort= p.open("taking",300);
						if ( comPort instanceof SerialPort)
						{
							SerialPort serialPort = (SerialPort) comPort;
							serialPort.setSerialPortParams(baudRate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

							output = serialPort.getOutputStream();
							input = serialPort.getInputStream();

						}

					}
				} catch (NoSuchPortException e) {
					e.printStackTrace();
				} catch (PortInUseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (UnsupportedCommOperationException e) {
					e.printStackTrace();
				}
				
				return this.isReadyToSend();
			}

			public boolean disconnect() {

				if(isReadyToSend()){
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					comPort.close();
					System.out.println("Com Port closed");
					comPort = null;
					input = null;
					output = null;
				}
				
				return isReadyToSend();
			}

			@Override
			public InputStream getInputStream() {
				return input;
			}
			
		};
	}

	@Override
	protected SendPort sendingPort() {
		return new SendPort(){

			public void sendByte(Byte b) {
				if(statusPort().isReadyToSend()){
					byte[] a = { b.byteValue() };
					try {
						output.write(a);
					} catch (IOException e) {
						e.printStackTrace();
					};
				}
				
			}
			
			

			@Override
			public void sendOneByte(int value) {
				try {
					output.write(value);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		};
	}

}

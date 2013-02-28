package floppymusic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map.Entry;

import gnu.io.CommDriver;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class ScanPorts {
	public static String portName = "/dev/ttyACM0";
	public static void main(String[] args) {

		System.out.println("Listing ports");
		try {
			System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
			Enumeration<CommPortIdentifier> enume = CommPortIdentifier.getPortIdentifiers();


			System.out.println("NOW  CHECKING PORTS ");
			while(enume.hasMoreElements()){
				CommPortIdentifier p = enume.nextElement();
				System.out.println("portName: "+p.getName());
			}

			try {
				CommPortIdentifier p = null;
				try{
					p = CommPortIdentifier.getPortIdentifier(portName);
				}
				catch (NoSuchPortException e) {
					System.out.println("no port " +portName);
					//e.printStackTrace();
					return;
				}

				if(p.isCurrentlyOwned()){
					System.out.println("already owned " +portName);
				}else{
					CommPort comPort= p.open("taking",300);
					if ( comPort instanceof SerialPort )
					{
						SerialPort serialPort = (SerialPort) comPort;
						serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

						//serialPort.getOutputStream().write(32);
					}

					comPort.close();
				}
			}  catch (PortInUseException e) {
				e.printStackTrace();
			} catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}

		} catch (UnsatisfiedLinkError e) {
			System.out.println("UnsatisfiedLinkError catched");
			System.out.println("library path is : "+System.getProperty("java.library.path"));
			System.out.println("it is your lib folder ???");
		}

	}
}

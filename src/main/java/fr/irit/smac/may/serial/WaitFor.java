package fr.irit.smac.may.serial;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class WaitFor {

	public static char chara(InputStream instream) throws IOException{
		boolean cont = true;
		char i = 0;
		while(cont){
			if(instream.available() > 0){
				i = (char) instream.read();
				cont = false;
			}
		}
		return i;
	}
	
	public static String line(BufferedReader br) throws IOException{
		boolean cont = true;
		String line = "";

		while(!br.ready()){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		line = br.readLine();
		return line;
	}
}

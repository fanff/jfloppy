/**
 *
 * Copyright 2007 Stefanie Tellex
 *
 * This file is part of Fefie.com Sound Recorder.
 *
 * Fefie.com Sound Recorder is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Fefie.com Sound Recorder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.fefie.sound_recorder.audio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyByteArrayOutputStream extends OutputStream {
	private byte[] mBuf;
	private int mOffset;
	public MyByteArrayOutputStream(byte[] myArray) {
		this(myArray, 0);
	}
	public MyByteArrayOutputStream(byte[] myArray, int offset) {
		super();
		mBuf = myArray;
		mOffset = offset;
	}


	public void write(int b) throws IOException {
		mBuf[mOffset] = (byte) b;
		mOffset++;
	}


	public void write(byte[] b, int off, int len) throws IOException {
		int start = mOffset;
		mOffset = mOffset + len;
		System.arraycopy(b, off, mBuf, start, len);
		
		notify(start, len);
		
		
	}
	private void notify(int offset, int length) {
		for (NewDataListener l : mListeners) {
			l.newData(offset, length);
		}
	}
	
	private List<NewDataListener> mListeners = new ArrayList<NewDataListener>();
	public void addListener(NewDataListener l) {
		mListeners.add(l);
	}
	public static interface NewDataListener {
		public void newData(int offset, int length);
	}
	
}

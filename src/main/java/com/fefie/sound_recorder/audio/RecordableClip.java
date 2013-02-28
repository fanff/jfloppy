/**
 *
 * Copyright 2007 Stefanie Tellex
 *
 * This file is part of Fefie.com Sound ger.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import com.fefie.sound_recorder.audio.AudioUtils.LineAndStream;
import com.fefie.sound_recorder.audio.MyByteArrayOutputStream.NewDataListener;
import com.fefie.sound_recorder.utils.IOUtils;
import com.fefie.sound_recorder.utils.ThreadUtils;

public class RecordableClip  {
	private final File mFile;
	private final AudioFormat mFormat;
	private int mLengthInBytes;
	
	private AudioInputStream mCurrentStream;
	
	private int mStreamLength;
	private byte[] mStreamData = new byte[16777216];
	private List<ClipListener> mListeners = new ArrayList<ClipListener>();
	
	/**
	 * Used for playing audio back.
	 */
	private Clip mClip;
	
	
	/**
	 * Used for recording audio.
	 */
	private RecordThread mRecordThread;
	
	private static byte[] intToBytes(int i){
		ByteBuffer bb = ByteBuffer.allocate(4); 
		bb.putInt(i); 
		return bb.array();
		}
	private static int bytesToInt(byte[] intBytes, int offset, int length){
		ByteBuffer bb = ByteBuffer.wrap(intBytes, offset, length);
		
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return (int) bb.getShort(); // & 0x0000ffff;
		//return (int) bb.getChar();
	}
	
		
	public RecordableClip(File file, AudioFormat format) throws IOException, AudioException {
		mFile = file;
		if (! mFile.exists()) {
			IOUtils.create(mFile);
		}
		mStreamLength = IOUtils.load(mFile, mStreamData);
		
		mFormat = format;

		try {
			mClip = AudioSystem.getClip();

		} catch (LineUnavailableException lue) {
			throw new AudioException(lue);
		}
		mRecordThread = new RecordThread(AudioUtils.getRecordingStream(mFormat));	
		
		new Thread(new Runnable() {
			public void run() {
				int lastFramePos = -1;
				while (true) {
					if (mClip != null) {
						int newFramePos = mClip.getFramePosition();
						if (lastFramePos != newFramePos) {
							lastFramePos = newFramePos;
							for (ClipListener l : mListeners) {
								l.newPlayHead(newFramePos);
							}
						}
						ThreadUtils.sleep(100);
					}
				}
			}
		}).start();
		
		mClip.addLineListener(new LineListener() {
			
			public void update(LineEvent event) {
				//System.out.println("Line event: " + event);
				
			}
			
		});
		
	}

	public void addClipListener(ClipListener cl) {
		mListeners.add(cl);
	}
	
	public int getSample(int idx) {
		int byteStartIdx = idx * mFormat.getFrameSize();
		return bytesToInt(mStreamData, byteStartIdx, mFormat.getFrameSize());
		
	}
	
	private int getLengthInBytes(){ 
		return mStreamLength;
	}
	private ByteArrayInputStream makeInputStream() {
		return new ByteArrayInputStream(mStreamData, 0, mStreamLength);
	}
	private AudioInputStream makeStream() {
		return new AudioInputStream(makeInputStream(), mFormat, getLengthInBytes());
	}
	
	
	
	
	public void start() throws AudioException  {
		if (!mClip.isOpen()) {
			try {
				mClip.open(makeStream());
			} catch (Exception lue) {
				throw new AudioException(lue);
			}
		}
		mClip.start();
	}
	
	/**
	 * Return the index (in samples) of the play head.
	 */
	public int getPlayHead() {
		return mClip.getFramePosition();
	}
	
	/**
	 * Stops recording if we're recording.
	 * Stops playing if we're playing. 
	 * @throws AudioException
	 */
	public void stop() throws AudioException  {
		mClip.stop();
		mRecordThread.stop();
	}
	
	/**
	 * Move the playhead to the beginning of the clip.
	 * @throws AudioException
	 */
	public void reset() throws AudioException  {
		mClip.setFramePosition(0);
	}
	
	public int getLengthInSamples() {
		return getLengthInBytes() / mFormat.getFrameSize();
	}
	public int getLengthInSeconds() {
		return Math.round(getLengthInSamples() / mFormat.getSampleRate()); 
	}
	
	public int getMaxSampleHeight() {
		return (int) Math.round(Math.pow(2, mFormat.getSampleSizeInBits()));
	}
	
	private OutputStream getOutputStream() throws IOException {
		MyByteArrayOutputStream os = new MyByteArrayOutputStream(mStreamData);
		os.addListener(new NewDataListener() {
			public void newData(int offset, int length) {
				for (ClipListener l : mListeners) {
					l.newData(offset, length);
					mStreamLength = offset + length;
				}
			}
		});
		return os;
	}
	
	/**
	 * Start recording data.  Sends Newevents to NewDataListeners when new data is read. 
	 * Recording happens in a separate thread, and new data listeners are activated in the record thread. 
	 * @throws AudioException
	 */
	public void record() throws AudioException {
		try {
			mClip.close();
			mStreamLength = 0;
			mRecordThread.start(getOutputStream());
		} catch (Exception e) {
			throw new AudioException(e);
		}
	}
	
	/**
	 * Clears data from the clip.
	 */
	public void clear() {
		mStreamLength = 0;
	}
	
	private static class RecordThread {
		private final LineAndStream mSource;
		private OutputStream mDest;
		private boolean mStop = false;
		private Thread mThread;
		public RecordThread(LineAndStream source) {
			
			mSource = source;
		
		}
		public void stop() {
			mStop = true;
		}
		public boolean isRecording() {
			return mThread != null && mThread.isAlive();
		}
		
		public void start(OutputStream dest) {
			if (isRecording()) {
				throw new IllegalStateException("Already recording.  Thread is " + mThread);
			}
			mStop = false;
			mDest = dest;
			//System.out.println("Starting to record");
			mThread = new Thread(new Runnable() {
				public void run() {
					RecordThread.this.run();
				}
			});
			mThread.start();
		}
		private Exception mException;
		public Exception getLastException() {
			return mException;
		}
		public void run() {
			try {
				byte[] buffer = new byte[1024];
				mSource.getLine().start();
				while(!mStop) {
					int read = mSource.getStream().read(buffer, 0, buffer.length);
					mDest.write(buffer, 0, read);
				}
				

				mSource.getLine().stop();
				
				mDest.close();
			} catch (Exception e) {
				e.printStackTrace();
				mException = e;
			}
		}
	}

	public interface ClipListener {
		public void newData(int offset, int length);
		public void newPlayHead(int playhead);
	}
	
	/**
	 * Saves the file in the given format. 
	 * If the type extension is "raw" it saves it without a header - just writes the bytes
	 * to the file in the current format.
	 * @throws IOException
	 */
	public void save(File f, AudioFileFormat.Type t) throws IOException {
		if (t.getExtension().equals("raw")) {
			IOUtils.copy(makeInputStream(), new FileOutputStream(f));
		} else {
			AudioSystem.write(makeStream(), t, f);
		}
	}
}

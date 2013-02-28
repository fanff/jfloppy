/**
 * 
 */
package test.main.openFloppy.utils;

import static org.junit.Assert.*;

import java.util.LinkedList;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import floppymusic.openFloppy.utils.FloppyFrame;
import floppymusic.openFloppy.utils.FloppyTrack;

/**
 * @author fanf
 *
 */
public class TestFloppyTrack {

	FloppyTrack emptyFP = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyFP = new FloppyTrack();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link floppymusic.openFloppy.utils.FloppyTrack#getFrameAt(long)}.
	 */
	@Test
	public void testGetFrameAt() {
		assertNull(emptyFP.getFreqAt(5));
	}

	/**
	 * Test method for {@link floppymusic.openFloppy.utils.FloppyTrack#canPutFrame(floppymusic.openFloppy.utils.FloppyFrame)}.
	 * 
	 * This test is ONLY for setting at One Index 
	 */
	@Test
	public void testcanSetFreqAt() {
		assertTrue(emptyFP.canSetFreqAt(5,10));


		assertTrue(emptyFP.canSetFreqAt(5,10));

		assertTrue(emptyFP.setFreqAt(5, 10) ) ;
		assertFalse(emptyFP.setFreqAt(5, 12) );


	}

	/**
	 * Test method for {@link floppymusic.openFloppy.utils.FloppyTrack#putFrame(floppymusic.openFloppy.utils.FloppyFrame)}.
	 * 
	 * For multiple indexes
	 */
	@Test
	public void testcanSetFreqFromTo() {
		//The third one overides the first one (partially
		// and fully the second one.
		// .....88888....5555
		assertTrue(emptyFP.setFreqFromTo(5, 8, 8) ) ;

		assertTrue(emptyFP.setFreqFromTo(5, 10, 8) ) ;

		assertFalse(emptyFP.setFreqFromTo(5, 10, 12) ) ;
		assertFalse(emptyFP.setFreqFromTo(0, 10, 12) ) ;
		assertFalse(emptyFP.setFreqFromTo(5, 15, 12) ) ;
		assertFalse(emptyFP.setFreqFromTo(8, 10, 12) ) ;
		assertFalse(emptyFP.setFreqFromTo(8, 15, 12) ) ;

		//the 8 has been put
		//now put the 5

		assertTrue(emptyFP.setFreqFromTo(14, 18, 5) ) ;
		assertFalse(emptyFP.setFreqFromTo(10, 18, 12) ) ;
		assertFalse(emptyFP.setFreqFromTo(15, 22, 12) ) ;

		//can put .
		assertFalse( emptyFP.canSetFreqFromTo(0, 20, 5) ) ;
		assertTrue( emptyFP.canSetFreqFromTo(10, 14, 12) ) ;

		assertTrue(17 == emptyFP.getLength());

	}

	/***
	 * 
	 * 
	 * 
	 */
	@Test
	public void testgetAllFrames() {

		assertTrue(emptyFP.setFreqFromTo(5, 10, 1));

		assertTrue(emptyFP.setFreqFromTo(10, 12, 2));

		assertTrue(emptyFP.setFreqFromTo(13, 14, 1));

		assertTrue(emptyFP.setFreqFromTo(15, 17, 2));
		assertTrue(emptyFP.setFreqFromTo(16, 18, 2));

		assertTrue(emptyFP.setFreqFromTo(19, 20, 2));

		assertTrue(emptyFP.setFreqFromTo(21, 25, 1));
		assertTrue(emptyFP.setFreqFromTo(27, 30, 1));
		assertTrue(emptyFP.setFreqFromTo(23, 30, 1));

		LinkedList<FloppyFrame>  allFrame = emptyFP.getAllFrames();
		assertTrue(allFrame.size() == 6);


		FloppyFrame firstOne = allFrame.pollFirst();

		assertTrue(firstOne.begin == 5);
		assertTrue(firstOne.end == 10);
		assertTrue(firstOne.freq == 1);

		FloppyFrame secondOne = allFrame.pollFirst();

		assertTrue(secondOne.begin == 10);
		assertTrue(secondOne.end == 12);
		assertTrue(secondOne.freq == 2);

		FloppyFrame thirdOne = allFrame.pollFirst();

		assertTrue(thirdOne.begin == 13);
		assertTrue(thirdOne.end == 14);
		assertTrue(thirdOne.freq == 1);

		FloppyFrame forthOne = allFrame.pollFirst();

		assertTrue(forthOne.begin == 15);
		assertTrue(forthOne.end == 18);
		assertTrue(forthOne.freq == 2);


		FloppyFrame fifthOne = allFrame.pollFirst();

		assertTrue(fifthOne.begin == 19);
		assertTrue(fifthOne.end == 20);
		assertTrue(fifthOne.freq == 2);

		FloppyFrame sixthOne = allFrame.pollFirst();

		assertTrue(sixthOne.begin == 21);
		assertTrue(sixthOne.end == 30);
		assertTrue(sixthOne.freq == 1);
		
		assertTrue(allFrame.isEmpty());
	}

	/**
	 *  
	 */
	@Test
	public void testgetAllFrames2() {
		assertTrue(emptyFP.setFreqFromTo(0, 1, 0) );
		
		assertTrue(emptyFP.setFreqFromTo(100, 101, 0) );
		
		LinkedList<FloppyFrame>  allFrame = emptyFP.getAllFrames();
		assertTrue(allFrame.size() == 2);
		
		FloppyFrame firstOne = allFrame.pollFirst();

		assertTrue(firstOne.begin == 0);
		assertTrue(firstOne.end == 1);
		assertTrue(firstOne.freq == 0);

		FloppyFrame secondOne = allFrame.pollFirst();

		assertTrue(secondOne.begin == 100);
		assertTrue(secondOne.end == 101);
		assertTrue(secondOne.freq == 0);
	}
}

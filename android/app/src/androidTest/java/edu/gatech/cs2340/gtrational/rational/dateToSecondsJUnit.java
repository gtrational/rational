package edu.gatech.cs2340.gtrational.rational;

import org.junit.Test;

import java.text.ParseException;

import edu.gatech.cs2340.gtrational.rational.controller.MapFilterDialogFragment;

import static org.junit.Assert.*;



public class DateToSecondsJUnit {
    /**
     * Tests November 13, 2017
     * @throws Exception The exception
     */
    @Test
    public void simpleTest() throws Exception {
        assertEquals(1510549200000L, MapFilterDialogFragment.dateToSeconds("11-13-2017"));
    }

    /**
     * Tests 10-09-1997
     * @throws Exception The exception
     */
    @Test
    public void testOlderDate() throws Exception {
        assertEquals(876369600000L, MapFilterDialogFragment.dateToSeconds("10-09-1997"));
    }

    /**
     * Tests 10-31-1950
     * @throws Exception The exception
     */
    @Test
    public void testVeryNegativeDate() throws Exception {
        assertEquals(-628542000000L, MapFilterDialogFragment.dateToSeconds("01-31-1950"));
    }

    /**
     * Tests 2-29-2001
     * @throws Exception The exception
     */
    @Test
    public void invalidLeapYearDate() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("02-29-2001");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests 2-29-1961
     * @throws Exception The exception
     */
    @Test
    public void invalidOldLeapYearDate1() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("02-29-1961");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests 2-29-1900
     * @throws Exception The exception
     */
    @Test
    public void invalidOldLeapYearDate2() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("02-29-1900");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests 2-29-2004
     * @throws Exception The exception
     */
    @Test
    public void validLeapYearDate() throws Exception {
        assertEquals(1078030800000L, MapFilterDialogFragment.dateToSeconds("2-29-2004"));
    }

    /**
     * Tests 2-29-1960
     * @throws Exception The exception
     */
    @Test
    public void validOldLeapYearDate() throws Exception {
        assertEquals(-310503600000L, MapFilterDialogFragment.dateToSeconds("2-29-1960"));
    }

    /**
     * Tests 2-29-2000
     * @throws Exception The exception
     */
    @Test
    public void validLeapYearDate3() throws Exception {
        assertEquals(951800400000L, MapFilterDialogFragment.dateToSeconds("2-29-2000"));
    }

    /**
     * Tests an empty string
     * @throws Exception The exception
     */
    @Test
    public void badlyFormedDateEmpty() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests an short string
     * @throws Exception The exception
     */
    @Test
    public void badlyFormedDateShort() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("11-12");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests a non-existent date
     * @throws Exception The exception
     */
    @Test
    public void badDate() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("3-32-2017");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests a date of all letters
     * @throws Exception The exception
     */
    @Test
    public void badlyFormedDateLetters() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("aa-bb-cccc");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests mix of letters and numbers as date input
     * @throws Exception The exception
     */
    @Test
    public void badlyFormedDateMixture() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds("a1-5b-1345");
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof ParseException);
        }
        assert(correctExceptionCalled);
    }

    /**
     * Tests null input
     * @throws Exception The exception
     */
    @Test
    public void nullCheck() throws Exception {
        boolean correctExceptionCalled = false;
        try {
            MapFilterDialogFragment.dateToSeconds(null);
        } catch (Exception e) {
            correctExceptionCalled = (e instanceof NullPointerException);
        }
        assert(correctExceptionCalled);
    }


}

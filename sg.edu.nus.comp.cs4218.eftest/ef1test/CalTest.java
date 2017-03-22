package ef1test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.CalException;
import sg.edu.nus.comp.cs4218.impl.app.CalApplication;

public class CalTest {
	private static final String MAR_2017_MON
	= "     March 2017      \t" + System.lineSeparator()
	+ "Mo Tu We Th Fr Sa Su \t" + System.lineSeparator()
	+ "       1  2  3  4  5 \t" + System.lineSeparator()
	+ " 6  7  8  9 10 11 12 \t" + System.lineSeparator()
	+ "13 14 15 16 17 18 19 \t" + System.lineSeparator()
	+ "20 21 22 23 24 25 26 \t" + System.lineSeparator()
	+ "27 28 29 30 31       \t" + System.lineSeparator()
	+ "                     \t" + System.lineSeparator() + System.lineSeparator();

	private static final String FAIL_OUTPUT = "Exception was thrown";

	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private CalApplication calApplication;
	private OutputStream stdout = null;

	@Before
	public void setUp() {
		calApplication = new CalApplication();
		stdout = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() {
		calApplication = null;
	}

	// Tests to Run
	/*
	 * @bug Error in their test case 
	 * The expected string given is different from the arguments supplied to calApp.
	 * Expected = January 2010 
	 * Actual: December 2009 
	 * They ran calApp using 12, 2009 but gave 01, 2010 as the expected result.
	 */
	@Test
	public void testRun() throws CalException {
		String[] args = { "12", "2009" };
		String expected = "	January 2010" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "      1  2  3  4  5 " + System.lineSeparator() + "6  7  8  9  10 11 12" + System.lineSeparator()
				+ "13 14 15 16 17 18 19" + System.lineSeparator() + "20 21 22 23 24 25 26" + System.lineSeparator()
				+ "27 28 29 30 31";

		calApplication.run(args, null, stdout);
		assertEquals(expected, stdout.toString());
	}

	/*
	 * @bug Error in their test case 
	 * The expected string and the result string is different 
	 * Expected = March 2016 
	 * Result = March 2017
	 */
	@Test
	public void testRunNullArgs() throws /* CalException, */ IOException {
		String[] args = null;
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		String expected = generateCalendar(determineFile(month, false));
		String actual = calApplication.printCal(args);
		assertEquals(expected, actual);
	}

	/*
	 * @bug Error in their test case 
	 * The expected string and the result string is different 
	 * Expected = March 2016 
	 * Result = March 2017
	 */
	@Test
	public void testRunEmptyArgs() throws CalException, IOException {
		String[] args = {};
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		String expected = generateCalendar(determineFile(month, false));
		String actual = calApplication.printCal(args);
		assertEquals(expected, actual);
	}

	/*
	 * start of my test cases 
	 */

	@Test
	public void testWrongOutput() throws CalException {
		String[] args = {"01", "12"};
		String expected = "";
		expected += "     January 12" + System.lineSeparator();
		expected += "Su Mo Tu We Th Fr Sa" + System.lineSeparator();
		expected += "                1  2" + System.lineSeparator();
		expected += " 3  4  5  6  7  8  9" + System.lineSeparator();
		expected += "10 11 12 13 14 15 16" + System.lineSeparator();
		expected += "17 18 19 20 21 22 23" + System.lineSeparator();
		expected += "24 25 26 27 28 29 30" + System.lineSeparator();
		expected += "31" + System.lineSeparator();
		calApplication.run(args, null, stdout);
		assertEquals(expected, stdout.toString());
	}
	
	/*
	 * end of my test cases 
	 */

	@Test
	public void testRunWithNullStdout() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("Null Pointer Exception");

		String[] args = { "1", "2010" };
		calApplication.run(args, null, null);
	}

	@Test
	public void testRunInvalidYear() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The year specified is not valid");

		String[] args = { "abc" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithNegativeYear() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The year specified should be in between 1..9999");

		String[] args = { "-1" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithYearZero() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The year specified should be in between 1..9999");

		String[] args = { "0" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithFloatYear() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The year specified is not valid");

		String[] args = { "1.5" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithYearLargerThan9999() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The year specified should be in between 1..9999");

		String[] args = { "10000" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithNegativeMonth() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The month specified should be in between 1..12");

		String[] args = { "-1", "2010" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithMonthGreaterThan12() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The month specified should be in between 1..12");

		String[] args = { "13", "2010" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithMonthZero() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The month specified should be in between 1..12");

		String[] args = { "0", "2010" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithFloatMonth() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The month specified is not valid");

		String[] args = { "1.5", "2010" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithStringMonth() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("The month specified is not valid");

		String[] args = { "abc", "2010" };
		calApplication.run(args, null, stdout);
	}

	@Test
	public void testRunWithWrongArguments() throws CalException {
		expectedEx.expect(CalException.class);
		expectedEx.expectMessage("Wrong arguments supplied to Cal application");

		String[] args = { "12", "2010", "xyz" };
		calApplication.run(args, null, stdout);
	}

	// VALID TESTCASES

	// Tests for printCal
	/*
	 * @bug Error in their test case 
	 * The expected string and the result string is different 
	 * Expected = March 2016 
	 * Result = March 2017
	 */
	@Test
	public void testPrintCalForCurrentMonth() throws IOException {
		String[] args = {};
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		String expected = generateCalendar(determineFile(month, false));
		String actual = calApplication.printCal(args);
		assertEquals(expected, actual);
	}

	// Tests for printCalMondayFirst
	/*
	 * @bug Error in their test case 
	 * The expected string and the result string is different 
	 * Expected = March 2016 
	 * Result = March 2017
	 */
	@Test
	public void testPrintCalWithMondayFirst() throws IOException {
		String[] args = {};
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		String expected = generateCalendar(determineFile(month, true));
		String actual = calApplication.printCalWithMondayFirst(args);
		assertEquals(expected, actual);
	}

	// Tests for printCalForMonthYear
	/*
	 * @bug Error in their test case 
	 * The spaces in front of the month is different 
	 */
	@Test
	public void testPrintCalForMonthYear() throws IOException {
		for (int i = 1; i <= 12; i++) {
			String[] args = { i + "", "2016" };
			int month = Integer.parseInt(args[0]) - 1;
			String expected = generateCalendar(determineFile(month, false));
			String actual = calApplication.printCalForMonthYear(args);
			assertEquals(expected, actual);
		}
	}

	@Test
	public void testPrintCalForMonthYear1() {
		String[] args = { "1", "2010" };
		String result = calApplication.printCalForMonthYear(args);
		String expected = "    January 2010" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "                1  2" + System.lineSeparator() + " 3  4  5  6  7  8  9" + System.lineSeparator()
				+ "10 11 12 13 14 15 16" + System.lineSeparator() + "17 18 19 20 21 22 23" + System.lineSeparator()
				+ "24 25 26 27 28 29 30" + System.lineSeparator() + "31";

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYear2() {
		String[] args = { "7", "2010" };
		String result = calApplication.printCalForMonthYear(args);
		String expected = "     July 2010" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "             1  2  3" + System.lineSeparator() + " 4  5  6  7  8  9 10" + System.lineSeparator()
				+ "11 12 13 14 15 16 17" + System.lineSeparator() + "18 19 20 21 22 23 24" + System.lineSeparator()
				+ "25 26 27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYear3() {
		String[] args = { "12", "2010" };
		String result = calApplication.printCalForMonthYear(args);
		String expected = "   December 2010" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "          1  2  3  4" + System.lineSeparator() + " 5  6  7  8  9 10 11" + System.lineSeparator()
				+ "12 13 14 15 16 17 18" + System.lineSeparator() + "19 20 21 22 23 24 25" + System.lineSeparator()
				+ "26 27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYear4() {
		String[] args = { "1", "2020" };
		String result = calApplication.printCalForMonthYear(args);
		String expected = "    January 2020" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "          1  2  3  4" + System.lineSeparator() + " 5  6  7  8  9 10 11" + System.lineSeparator()
				+ "12 13 14 15 16 17 18" + System.lineSeparator() + "19 20 21 22 23 24 25" + System.lineSeparator()
				+ "26 27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYear5() {
		String[] args = { "7", "2020" };
		String result = calApplication.printCalForMonthYear(args);
		String expected = "     July 2020" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "          1  2  3  4" + System.lineSeparator() + " 5  6  7  8  9 10 11" + System.lineSeparator()
				+ "12 13 14 15 16 17 18" + System.lineSeparator() + "19 20 21 22 23 24 25" + System.lineSeparator()
				+ "26 27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYear6() {
		String[] args = { "12", "2020" };
		String result = calApplication.printCalForMonthYear(args);
		String expected = "   December 2020" + System.lineSeparator() + "Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "       1  2  3  4  5" + System.lineSeparator() + " 6  7  8  9 10 11 12" + System.lineSeparator()
				+ "13 14 15 16 17 18 19" + System.lineSeparator() + "20 21 22 23 24 25 26" + System.lineSeparator()
				+ "27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	// Tests for printCalForMonthYearMondayFirst
	@Test
	public void testPrintCalForMonthYearMondayFirst() throws IOException {
		for (int i = 1; i <= 12; i++) {
			String[] args = { i + "", "2016" };
			int month = Integer.parseInt(args[0]) - 1;
			String expected = generateCalendar(determineFile(month, true));
			String actual = calApplication.printCalForMonthYearMondayFirst(args);
			assertEquals(expected, actual);
		}
	}

	@Test
	public void testPrintCalForMonthYearMondayFirst1() {
		String[] args = { "1", "2010" };
		String result = calApplication.printCalForMonthYearMondayFirst(args);
		String expected = "    January 2010" + System.lineSeparator() + "Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "             1  2  3" + System.lineSeparator() + " 4  5  6  7  8  9 10" + System.lineSeparator()
				+ "11 12 13 14 15 16 17" + System.lineSeparator() + "18 19 20 21 22 23 24" + System.lineSeparator()
				+ "25 26 27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYearMondayFirst2() {
		String[] args = { "7", "2010" };
		String result = calApplication.printCalForMonthYearMondayFirst(args);
		String expected = "     July 2010" + System.lineSeparator() + "Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "          1  2  3  4" + System.lineSeparator() + " 5  6  7  8  9 10 11" + System.lineSeparator()
				+ "12 13 14 15 16 17 18" + System.lineSeparator() + "19 20 21 22 23 24 25" + System.lineSeparator()
				+ "26 27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYearMondayFirst3() {
		String[] args = { "12", "2010" };
		String result = calApplication.printCalForMonthYearMondayFirst(args);
		String expected = "   December 2010" + System.lineSeparator() + "Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "       1  2  3  4  5" + System.lineSeparator() + " 6  7  8  9 10 11 12" + System.lineSeparator()
				+ "13 14 15 16 17 18 19" + System.lineSeparator() + "20 21 22 23 24 25 26" + System.lineSeparator()
				+ "27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYearMondayFirst4() {
		String[] args = { "1", "2020" };
		String result = calApplication.printCalForMonthYearMondayFirst(args);
		String expected = "    January 2020" + System.lineSeparator() + "Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "       1  2  3  4  5" + System.lineSeparator() + " 6  7  8  9 10 11 12" + System.lineSeparator()
				+ "13 14 15 16 17 18 19" + System.lineSeparator() + "20 21 22 23 24 25 26" + System.lineSeparator()
				+ "27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYearMondayFirst5() {
		String[] args = { "7", "2020" };
		String result = calApplication.printCalForMonthYearMondayFirst(args);
		String expected = "     July 2020" + System.lineSeparator() + "Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "       1  2  3  4  5" + System.lineSeparator() + " 6  7  8  9 10 11 12" + System.lineSeparator()
				+ "13 14 15 16 17 18 19" + System.lineSeparator() + "20 21 22 23 24 25 26" + System.lineSeparator()
				+ "27 28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	@Test
	public void testPrintCalForMonthYearMondayFirst6() {
		String[] args = { "12", "2020" };
		String result = calApplication.printCalForMonthYearMondayFirst(args);
		String expected = "   December 2020" + System.lineSeparator() + "Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "    1  2  3  4  5  6" + System.lineSeparator() + " 7  8  9 10 11 12 13" + System.lineSeparator()
				+ "14 15 16 17 18 19 20" + System.lineSeparator() + "21 22 23 24 25 26 27" + System.lineSeparator()
				+ "28 29 30 31" + System.lineSeparator();

		assertEquals(expected, result);
	}

	// Tests for printCalForYear
	/*
	 * @bug Error in their test case 
	 * Expected string: Does not contain new line at the end 
	 * Actual String: Contains new line at the end
	 */
	@Test
	public void testPrintCalForYear() throws IOException {
		String[] args = { "2016" };
		String file = "16.txt";
		String expected = generateCalendar(file);
		String actual = calApplication.printCalForYear(args);
		assertEquals(expected, actual);
	}

	@Test
	public void testPrintCalForYear1() {
		String[] args = { "2010" };
		String result = calApplication.printCalForYear(args);
		String expected = "    January 2010         February 2010           March 2010" + System.lineSeparator()
				+ "Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "                1  2      1  2  3  4  5  6      1  2  3  4  5  6" + System.lineSeparator()
				+ " 3  4  5  6  7  8  9   7  8  9 10 11 12 13   7  8  9 10 11 12 13" + System.lineSeparator()
				+ "10 11 12 13 14 15 16  14 15 16 17 18 19 20  14 15 16 17 18 19 20" + System.lineSeparator()
				+ "17 18 19 20 21 22 23  21 22 23 24 25 26 27  21 22 23 24 25 26 27" + System.lineSeparator()
				+ "24 25 26 27 28 29 30  28                    28 29 30 31         " + System.lineSeparator()
				+ "31                                                              " + System.lineSeparator()
				+ "     April 2010             May 2010             June 2010" + System.lineSeparator()
				+ "Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "             1  2  3                     1         1  2  3  4  5" + System.lineSeparator()
				+ " 4  5  6  7  8  9 10   2  3  4  5  6  7  8   6  7  8  9 10 11 12" + System.lineSeparator()
				+ "11 12 13 14 15 16 17   9 10 11 12 13 14 15  13 14 15 16 17 18 19" + System.lineSeparator()
				+ "18 19 20 21 22 23 24  16 17 18 19 20 21 22  20 21 22 23 24 25 26" + System.lineSeparator()
				+ "25 26 27 28 29 30     23 24 25 26 27 28 29  27 28 29 30         " + System.lineSeparator()
				+ "                      30 31                                     " + System.lineSeparator()
				+ "     July 2010            August 2010          September 2010" + System.lineSeparator()
				+ "Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "             1  2  3   1  2  3  4  5  6  7            1  2  3  4" + System.lineSeparator()
				+ " 4  5  6  7  8  9 10   8  9 10 11 12 13 14   5  6  7  8  9 10 11" + System.lineSeparator()
				+ "11 12 13 14 15 16 17  15 16 17 18 19 20 21  12 13 14 15 16 17 18" + System.lineSeparator()
				+ "18 19 20 21 22 23 24  22 23 24 25 26 27 28  19 20 21 22 23 24 25" + System.lineSeparator()
				+ "25 26 27 28 29 30 31  29 30 31              26 27 28 29 30      " + System.lineSeparator()
				+ "                                                                " + System.lineSeparator()
				+ "    October 2010         November 2010         December 2010" + System.lineSeparator()
				+ "Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa  Su Mo Tu We Th Fr Sa" + System.lineSeparator()
				+ "                1  2      1  2  3  4  5  6            1  2  3  4" + System.lineSeparator()
				+ " 3  4  5  6  7  8  9   7  8  9 10 11 12 13   5  6  7  8  9 10 11" + System.lineSeparator()
				+ "10 11 12 13 14 15 16  14 15 16 17 18 19 20  12 13 14 15 16 17 18" + System.lineSeparator()
				+ "17 18 19 20 21 22 23  21 22 23 24 25 26 27  19 20 21 22 23 24 25" + System.lineSeparator()
				+ "24 25 26 27 28 29 30  28 29 30              26 27 28 29 30 31   " + System.lineSeparator()
				+ "31                                                              " + System.lineSeparator();

		assertEquals(expected, result);
	}

	// Tests for printCalForYearMondayFirst
	@Test
	public void testPrintCalForYearMondayFirst() throws IOException {
		String[] args = { "2016" };
		String file = "16_m.txt";
		String expected = generateCalendar(file);
		String actual = calApplication.printCalForYearMondayFirst(args);
		assertEquals(expected, actual);
	}

	@Test
	public void testPrintCalForYearMondayFirst1() {
		String[] args = { "2010" };
		String result = calApplication.printCalForYearMondayFirst(args);
		String expected = "    January 2010         February 2010           March 2010" + System.lineSeparator()
				+ "Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "             1  2  3   1  2  3  4  5  6  7   1  2  3  4  5  6  7" + System.lineSeparator()
				+ " 4  5  6  7  8  9 10   8  9 10 11 12 13 14   8  9 10 11 12 13 14" + System.lineSeparator()
				+ "11 12 13 14 15 16 17  15 16 17 18 19 20 21  15 16 17 18 19 20 21" + System.lineSeparator()
				+ "18 19 20 21 22 23 24  22 23 24 25 26 27 28  22 23 24 25 26 27 28" + System.lineSeparator()
				+ "25 26 27 28 29 30 31                        29 30 31            " + System.lineSeparator()
				+ "                                                                " + System.lineSeparator()
				+ "     April 2010             May 2010             June 2010" + System.lineSeparator()
				+ "Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "          1  2  3  4                  1  2      1  2  3  4  5  6" + System.lineSeparator()
				+ " 5  6  7  8  9 10 11   3  4  5  6  7  8  9   7  8  9 10 11 12 13" + System.lineSeparator()
				+ "12 13 14 15 16 17 18  10 11 12 13 14 15 16  14 15 16 17 18 19 20" + System.lineSeparator()
				+ "19 20 21 22 23 24 25  17 18 19 20 21 22 23  21 22 23 24 25 26 27" + System.lineSeparator()
				+ "26 27 28 29 30        24 25 26 27 28 29 30  28 29 30            " + System.lineSeparator()
				+ "                      31                                        " + System.lineSeparator()
				+ "     July 2010            August 2010          September 2010" + System.lineSeparator()
				+ "Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "          1  2  3  4                     1         1  2  3  4  5" + System.lineSeparator()
				+ " 5  6  7  8  9 10 11   2  3  4  5  6  7  8   6  7  8  9 10 11 12" + System.lineSeparator()
				+ "12 13 14 15 16 17 18   9 10 11 12 13 14 15  13 14 15 16 17 18 19" + System.lineSeparator()
				+ "19 20 21 22 23 24 25  16 17 18 19 20 21 22  20 21 22 23 24 25 26" + System.lineSeparator()
				+ "26 27 28 29 30 31     23 24 25 26 27 28 29  27 28 29 30         " + System.lineSeparator()
				+ "                      30 31                                     " + System.lineSeparator()
				+ "    October 2010         November 2010         December 2010" + System.lineSeparator()
				+ "Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su  Mo Tu We Th Fr Sa Su" + System.lineSeparator()
				+ "             1  2  3   1  2  3  4  5  6  7         1  2  3  4  5" + System.lineSeparator()
				+ " 4  5  6  7  8  9 10   8  9 10 11 12 13 14   6  7  8  9 10 11 12" + System.lineSeparator()
				+ "11 12 13 14 15 16 17  15 16 17 18 19 20 21  13 14 15 16 17 18 19" + System.lineSeparator()
				+ "18 19 20 21 22 23 24  22 23 24 25 26 27 28  20 21 22 23 24 25 26" + System.lineSeparator()
				+ "25 26 27 28 29 30 31  29 30                 27 28 29 30 31      " + System.lineSeparator()
				+ "                                                                " + System.lineSeparator();

		assertEquals(expected, result);
	}

	/*
	 * @bug Error in their test case 
	 * The path given to the test folder is wrong. 
	 * Given: CurrentDirectory/resources/cal/test/filename
	 * Should be: currentDirectory/resources.cal.test/filename
	 * Commented out the wrong path to be able to test files in the correct path 
	 */
	private String generateCalendar(String filename) throws IOException {
		if (!filename.isEmpty()) {
			StringBuilder stringBuilder = new StringBuilder();
//			File file = Paths.get(Environment.currentDirectory)
//					.resolve("resources" + File.separator + "cal" + File.separator + "test" + File.separator + filename)
//					.toFile();
			File file = Paths.get(Environment.currentDirectory)
					.resolve("resources.cal.test" + File.separator + filename)
					.toFile();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(System.lineSeparator());
			}
			reader.close();

			stringBuilder.deleteCharAt(stringBuilder.length() - 1);

			return stringBuilder.toString();
		}

		return null;
	}

	private String determineFile(int month, boolean isMondayFirst) {
		String[] files = { "jan16", "feb16", "mar16", "apr16", "may16", "jun16", "jul16", "aug16", "sep16", "oct16",
				"nov16", "dec16" };

		String file = files[month];

		if (isMondayFirst) {
			file += "_m";
		}

		file += ".txt";
		return file;
	}

}

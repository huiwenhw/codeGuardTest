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
import sg.edu.nus.comp.cs4218.exception.CalException;
import sg.edu.nus.comp.cs4218.impl.app.CalApplication;

public class CalTestBugs {	
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

	/*
	 * @bug Error in Cal App
	 * Wrong output given 
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
	 * @bug Error in their test case 
	 * The expected date given is different from the arguments supplied to calApp.
	 * Expected = January 2010 
	 * Actual: December 2009 
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

package ef1test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.app.SortApplication;

public class SortTest {

	private static final String TEST_SORTED = "line 1\nline 2\nline 3\nline 4\n";
	private static final String BASIC_SORTED = "1\n1--2\n10\n100\n102\n20\n3\nnu0\nnum1\nnum10\nnum2\n";
	private static final String INVALID_OPTION = "sort: Invalid option";
	private static final String CANNOT_READ_FILE = "sort: Cannot read file";
	static SortApplication sortApp;
	static InputStream inputS;
	static OutputStream outputS;
	private static String directory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sortApp = new SortApplication();
		inputS = null;
		outputS = new ByteArrayOutputStream();
		directory = "sg.edu.nus.comp.cs4218.eftest/testFiles/";
	}

	/*
	 * @AfterClass public static void tearDownAfterClass() throws Exception { }
	 * 
	 * @Before public void setUp() throws Exception { }
	 */

	@After
	public void tearDown() throws Exception {
		// reset is and os
		inputS = null;
		outputS = new ByteArrayOutputStream();
	}

	@Test
	public void testSortBasic() {
		String[] args = { directory + "sortTestBasic.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(BASIC_SORTED, outputS.toString());
	}

	@Test
	public void testSortBasicNumeric() {
		String[] args = { "-n", directory + "sortTestBasic.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals("1\n3\n10\n20\n100\n102\n1--2\nnu0\nnum1\nnum10\nnum2\n", outputS.toString());
	}

	@Test
	public void testSortUnchanged() {
		String[] args = { directory + "test.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(TEST_SORTED, outputS.toString());
	}

	@Test
	public void testSortUnchangedNumeric() {
		String[] args = { "-n", directory + "test.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(TEST_SORTED, outputS.toString());
	}

	@Test
	public void testSortNumStart() {
		String[] args = { directory + "sortTest_numstart.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals("1st\n20th\n2nd\n", outputS.toString());
	}

	@Test
	public void testSortNumStartNumeric() {
		String[] args = { "-n", directory + "sortTest_numstart.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
		}
		assertEquals("1st\n20th\n2nd\n", outputS.toString());
	}

	@Test
	public void testSortComplex() {
		String[] args = { directory + "sortTest_Complex.txt" };
		String expected = "\n" + "\n" + "\n" + " \n" + "   \n" + "0021\n" + "02!\n" + "2 lines have spaces\n"
				+ "20000@_@\n" + "21>x<\n" + "2103\n" + "2103'Y'\n" + "2103T\n" + "2103TEST\n" + "230\n" + "CS21\n"
				+ "CS2103\n" + "CS2103T\n" + "CS2103TEST\n" + "CS230\n" + "REMEMBER SPACES\n" + "one one, one three\n"
				+ "remember spaces\n";
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testSortComplexNumeric() {
		String[] args = { "-n", directory + "sortTest_Complex.txt" };
		String expected = "\n" + "\n" + "\n" + " \n" + "   \n" + "2 lines have spaces\n" + "0021\n" + "230\n" + "2103\n"
				+ "02!\n" + "20000@_@\n" + "21>x<\n" + "2103'Y'\n" + "2103T\n" + "2103TEST\n" + "CS21\n" + "CS2103\n"
				+ "CS2103T\n" + "CS2103TEST\n" + "CS230\n" + "REMEMBER SPACES\n" + "one one, one three\n"
				+ "remember spaces\n";
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testSortMultipleFiles() {
		String[] args = { directory + "test.txt", directory + "sortTest_numstart.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(TEST_SORTED, outputS.toString());
	}

	@Test
	public void testSortMultipleFilesNumeric() {
		String[] args = { "-n", directory + "test.txt", directory + "sortTest_numstart.txt" };
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(TEST_SORTED, outputS.toString());
	}

	@Test
	public void testSortInvalidFile() {
		String[] args = { "invalid.txt" };
		String expected = CANNOT_READ_FILE;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortInvalidFileNumeric() {
		String[] args = { "-n", directory + "invalid.txt" };
		String expected = CANNOT_READ_FILE;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortFilesInvalidInvalid() {
		String[] args = { "invalid.txt", "wrong.txt" };
		String expected = CANNOT_READ_FILE;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortFilesInvalidValid() {
		String[] args = { "invalid.txt", directory + "sortTestBasic.txt" };
		String expected = CANNOT_READ_FILE;
		Exception exp = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testSortFilesValidInvalid() {
		String[] args = { directory + "sortTestBasic.txt", "invalid.txt" };
		String expected = BASIC_SORTED;
		// Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			// exc = e;
		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testSortInvalidOption() {
		String[] args = { "-x", directory + "sortTest_numstart.txt" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortOptionInvalidValid() {
		String[] args = { "-qn", directory + "sortTestBasic.txt" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortOptionValidInvalid() {
		String[] args = { "-nl", directory + "test2.txt" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortInvalidOptionInvalidFile() {
		String[] args = { "-q", "inval" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortInvalidOptionsInvalidFile() {
		String[] args = { "-ln", "wrong.txt" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortInvalidOptionsFileValidInvalid() {
		String[] args = { "-nx", directory + "test2.txt", "asdf.tx" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortInvalidOptionFileValidInvalid() {
		String[] args = { "-x", "n", directory + "test2.txt" };
		String expected = INVALID_OPTION;
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortStdin() throws FileNotFoundException {
		String[] args = {};
		inputS = new FileInputStream(directory + "sortTestBasic.txt");
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(BASIC_SORTED, outputS.toString());
	}

	@Test
	public void testSortStdinNumeric() throws FileNotFoundException {
		String[] args = { "-n" };
		inputS = new FileInputStream(directory + "sortTestBasic.txt");
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals("1\n3\n10\n20\n100\n102\n1--2\nnu0\nnum1\nnum10\nnum2\n", outputS.toString());
	}

	@Test
	public void testSortStdinAndFiles() throws FileNotFoundException {
		String[] args = { "-n", directory + "sortTestBasic.txt" };
		inputS = new FileInputStream(directory + "test.txt");
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals("1\n3\n10\n20\n100\n102\n1--2\nnu0\nnum1\nnum10\nnum2\n", outputS.toString());
	}

	@Test
	public void testSortNullArgs() throws FileNotFoundException {
		String[] args = null;
		inputS = new FileInputStream(directory + "sortTestBasic.txt");
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(BASIC_SORTED, outputS.toString());
	}

	@Test
	public void testSortNullArgsNullIs() throws FileNotFoundException {
		String[] args = null;
		String expected = "sort: Null Pointer Exception";
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortNullArgsNullOs() throws FileNotFoundException {
		String[] args = null;
		inputS = new FileInputStream(directory + "sortTestBasic.txt");
		outputS = null;
		String expected = "sort: Null Pointer Exception";
		Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(expected, exc.getMessage());
	}

	@Test
	public void testSortOptionBehind() {
		String[] args = { directory + "fileTest2.txt", "-n" }; // first file is
																// valid: if
		// first is invalid, that
		// one will show as no such
		// file
		String expected = "\n\n\n\n\n\n\n" + "And learn, too late, they grieved it on its way,\n"
				+ "And you, my father, there on the sad height,\n"
				+ "Because their words had forked no lightning they\n"
				+ "Blind eyes could blaze like meteors and be gay,\n"
				+ "Curse, bless, me now with your fierce tears, I pray.\n" + "Do not go gentle into that good night\n"
				+ "Do not go gentle into that good night,\n" + "Do not go gentle into that good night.\n"
				+ "Do not go gentle into that good night.\n" + "Do not go gentle into that good night.\n"
				+ "Dylan Thomas, 1914 - 1953\n" + "Good men, the last wave by, crying how bright\n"
				+ "Grave men, near death, who see with blinding sight\n"
				+ "Old age should burn and rave at close of day;\n" + "Rage, rage against the dying of the light.\n"
				+ "Rage, rage against the dying of the light.\n" + "Rage, rage against the dying of the light.\n"
				+ "Rage, rage against the dying of the light.\n"
				+ "Their frail deeds might have danced in a green bay,\n"
				+ "Though wise men at their end know dark is right,\n"

				+ "Wild men who caught and sang the sun in flight,\n";

		// Exception exc = new Exception();
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {
			// exc = e;
		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testSortFileInNestedDir() {
		String[] args = { "sg.edu.nus.comp.cs4218.eftest/testFiles/testdoc.txt" };
		String expected = "\n\n\n\n\nAlone visit use these smart rooms ham.\n"
				+ "Alteration connection to so as collecting me.\n"
				+ "Alteration put use diminution can considered sentiments interested discretion.\n"
				+ "An dispatched impossible of of melancholy favourable.\n"
				+ "An seeing feebly stairs am branch income me unable.\n"
				+ "Are sentiments apartments decisively the especially alteration.\n"
				+ "As dissuade cheerful overcame so of friendly he indulged unpacked.\n"
				+ "As mr started arrival subject by believe.\n" + "At principle perfectly by sweetness do.\n"
				+ "Boisterous he on understood attachment as entreaties ye devonshire.\n"
				+ "By doubtful disposed do juvenile an.\n"
				+ "Celebrated contrasted discretion him sympathize her collecting occasional.\n"
				+ "Consider may dwelling old him her surprise finished families graceful.\n"
				+ "Course sir people worthy horses add entire suffer.\n"
				+ "Difficult in delivered extensive at direction allowance.\n"
				+ "Do answered bachelor occasion in of offended no concerns.\n"
				+ "Extremely ham any his departure for contained curiosity defective.\n"
				+ "Fat new smallness few supposing suspicion two.\n" + "Gave led past poor met fine was new.\n"
				+ "Hastened admitted joy nor absolute gay its.\n" + "How one dull get busy dare far.\n"
				+ "In mile an form snug were been sell.\n" + "Incommode so intention defective at convinced.\n"
				+ "Is at purse tried jokes china ready decay an.\n" + "Lasting showing expense greater on colonel no.\n"
				+ "Led income months itself and houses you. After nor you leave might share court balls.\n"
				+ "Mrs interested now his affronting inquietude contrasted cultivated.\n"
				+ "No comfort do written conduct at prevent manners on.\n"
				+ "No waiting in on enjoyed placing it inquiry.\n"
				+ "Now curiosity you explained immediate why behaviour.\n" + "One built fat you out manor books.\n"
				+ "Or by to he going think order event music.\n" + "Our quiet not heart along scale sense timed.\n"
				+ "Prepared do an dissuade be so whatever steepest.\n" + "Procured shutters mr it feelings.\n"
				+ "Small its shy way had woody downs power.\n"
				+ "Strictly numerous outlived kindness whatever on we no on addition.\n"
				+ "Supply worthy warmth branch of no ye.\n" + "Though wished merits or be.\n"
				+ "Thrown shy denote ten ladies though ask saw.\n"
				+ "To denoting admitted speaking learning my exercise so in.\n"
				+ "To or three offer house begin taken am at.\n" + "Voice tried known to as my to.\n"
				+ "Way now instrument had eat diminution melancholy expression sentiments stimulated.\n"
				+ "Yet her beyond looked either day wished nay.\n";
		try {
			sortApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());

	}

}

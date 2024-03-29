package ef1test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.SortException;
import sg.edu.nus.comp.cs4218.impl.app.SortApplication;

public class SortApplicationTest {

	SortApplication ssa;
	String args[];
	String fileName;
	InputStream stdin;
	InputStream numericStdin;
	InputStream emptyStdin;
	String directory;
	String numericFile;
	String testMethodsFile;
	String emptyFile;
	String reorderStr1 = "test.txt";
	String reorderStr2 = "test1.txt";

	String defaultString = "Boisterous he on understood attachment as entreaties ye devonshire.\n"
			+ "In mile an form snug were been sell.\n" + "Hastened admitted joy nor absolute gay its.\n"
			+ "Extremely ham any his departure for contained curiosity defective.\n"
			+ "Way now instrument had eat diminution melancholy expression sentiments stimulated.\n"
			+ "One built fat you out manor books.\n"
			+ "Mrs interested now his affronting inquietude contrasted cultivated.\n"
			+ "Lasting showing expense greater on colonel no.\n\n" +

			"Prepared do an dissuade be so whatever steepest.\n" + "Yet her beyond looked either day wished nay.\n"
			+ "By doubtful disposed do juvenile an.\n" + "Now curiosity you explained immediate why behaviour.\n"
			+ "An dispatched impossible of of melancholy favourable.\n"
			+ "Our quiet not heart along scale sense timed.\n"
			+ "Consider may dwelling old him her surprise finished families graceful.\n"
			+ "Gave led past poor met fine was new.\n\n" +

			"Is at purse tried jokes china ready decay an.\n" + "Small its shy way had woody downs power.\n"
			+ "To denoting admitted speaking learning my exercise so in.\n" + "Procured shutters mr it feelings.\n"
			+ "To or three offer house begin taken am at.\n"
			+ "As dissuade cheerful overcame so of friendly he indulged unpacked.\n"
			+ "Alteration connection to so as collecting me.\n"
			+ "Difficult in delivered extensive at direction allowance.\n"
			+ "Alteration put use diminution can considered sentiments interested discretion.\n"
			+ "An seeing feebly stairs am branch income me unable.\n\n" +

			"No comfort do written conduct at prevent manners on.\n"
			+ "Celebrated contrasted discretion him sympathize her collecting occasional.\n"
			+ "Do answered bachelor occasion in of offended no concerns.\n" + "Supply worthy warmth branch of no ye.\n"
			+ "Voice tried known to as my to.\n" + "Though wished merits or be.\n"
			+ "Alone visit use these smart rooms ham.\n" + "No waiting in on enjoyed placing it inquiry.\n\n" +

			"Fat new smallness few supposing suspicion two.\n" + "Course sir people worthy horses add entire suffer.\n"
			+ "How one dull get busy dare far.\n" + "At principle perfectly by sweetness do.\n"
			+ "As mr started arrival subject by believe.\n"
			+ "Strictly numerous outlived kindness whatever on we no on addition.\n\n" +

			"Are sentiments apartments decisively the especially alteration.\n"
			+ "Thrown shy denote ten ladies though ask saw.\n" + "Or by to he going think order event music.\n"
			+ "Incommode so intention defective at convinced.\n"
			+ "Led income months itself and houses you. After nor you leave might share court balls.";

	String sortedString = "\n\n\n\n\nAlone visit use these smart rooms ham.\n"
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

	@Before
	public void setUp() throws FileNotFoundException {
		ssa = new SortApplication();
		args = new String[2];
		directory = "sg.edu.nus.comp.cs4218.eftest/testFiles/";
		fileName = directory + "testdoc.txt";
		stdin = new FileInputStream(directory + "testdoc.txt");
		numericStdin = new FileInputStream(directory + "TestSortNumeric.txt");
		numericFile = directory + "TestSortNumeric.txt";
		testMethodsFile = directory + "TestSortMethods.txt";
		emptyFile = directory + "emptydoc.txt";
		emptyStdin = new FileInputStream(directory + "emptydoc.txt");
	}

	@Test(expected = SortException.class)
	public void testSort() throws SortException {
		args[0] = "-n";
		args[1] = fileName;
		ssa.run(args, null, null);
	}

	@Test(expected = SortException.class)
	public void testNull() throws SortException {
		ssa.run(null, null, System.out);
	}

	@Test
	public void arrToString() {
		assertEquals("convert\nto\nstring", ssa.convertToStr(new String[] { "convert", "to", "string" }));
	}

	/*
	 * @Test public void listToString() { ArrayList<String> temp = new
	 * ArrayList<String>(); temp.add("convert"); temp.add("to");
	 * temp.add("string"); assertEquals("convert\nto\nstring\n",
	 * SortExtendedFunctions.listToString(temp)); }
	 * 
	 * @Test public void stringToArr(){ assertTrue(Arrays.equals(new
	 * String[]{"convert", "to", "arr"},
	 * SortExtendedFunctions.stringToArr("convert to arr"))); }
	 */

	@Test
	public void testSortNumbersAsText() {
		String input = ssa.convertToStr(new String[] { "2", "1", "10" });
		String expected = ssa.convertToStr(new String[] { "1", "10", "2" });
		assertEquals(expected, ssa.sortAll(input));
	}

	@Test
	public void testSortNumbersAsNumbers() {
		String input = ssa.convertToStr(new String[] { "2", "1", "10" });
		String expected = ssa.convertToStr(new String[] { "1", "2", "10" });
		assertEquals(expected, ssa.sortAllWithFirstWordNumber(ssa.sortAll(input)));
	}

	@Test
	public void testSortStringsCapital() {
		String input = ssa.convertToStr(new String[] { "ZA", "ABC", "ZZ" });
		String expected = ssa.convertToStr(new String[] { "ABC", "ZA", "ZZ" });
		assertEquals(expected, ssa.sortStringsCapital(input));
	}

	@Test
	public void testSortStringsSimple() {
		String input = ssa.convertToStr(new String[] { "za", "abc", "zz" });
		String expected = ssa.convertToStr(new String[] { "abc", "za", "zz" });
		assertEquals(expected, ssa.sortStringsSimple(input));
	}

	@Test
	public void testSortSpecialChars() {
		String input = ssa.convertToStr(new String[] { "!", "-", "+" });
		String expected = ssa.convertToStr(new String[] { "!", "+", "-" });
		assertEquals(expected, ssa.sortSpecialChars(input));
	}

	@Test
	public void testSortCapitalNumbers() {
		String input = ssa.convertToStr(new String[] { "1ZA", "ABC", "ZZ", "10" });
		String expected = ssa.convertToStr(new String[] { "10", "1ZA", "ABC", "ZZ" });
		assertEquals(expected, ssa.sortCapitalNumbers(input));
	}

	@Test
	public void testSortCapitalSpecialChars() {
		String input = ssa.convertToStr(new String[] { "!ZA", "ABC", "ZZ", "+" });
		String expected = ssa.convertToStr(new String[] { "!ZA", "+", "ABC", "ZZ" });
		assertEquals(expected, ssa.sortCapitalSpecialChars(input));
	}

	@Test
	public void testSortNumbersSpecialChars() {
		String input = ssa.convertToStr(new String[] { "!", "-10", "2", "+1" });
		String expected = ssa.convertToStr(new String[] { "!", "+1", "-10", "2" });
		assertEquals(expected, ssa.sortNumbersSpecialChars(input));
	}

	@Test
	public void testSortSimpleNumbers() {
		String input = ssa.convertToStr(new String[] { "1za", "abc", "zz", "10" });
		String expected = ssa.convertToStr(new String[] { "10", "1za", "abc", "zz" });
		assertEquals(expected, ssa.sortSimpleNumbers(input));
	}

	@Test
	public void testSortSimpleSpecialChars() {
		String input = ssa.convertToStr(new String[] { "!za", "abc", "zz", "+" });
		String expected = ssa.convertToStr(new String[] { "!za", "+", "abc", "zz" });
		assertEquals(expected, ssa.sortSimpleSpecialChars(input));
	}

	@Test
	public void testSortSimpleCapital() {
		String input = ssa.convertToStr(new String[] { "Za", "abc", "zZ", "aBc" });
		String expected = ssa.convertToStr(new String[] { "Za", "aBc", "abc", "zZ" });
		assertEquals(expected, ssa.sortSimpleCapital(input));
	}

	@Test
	public void testSortSimpleCapitalSpecialChars() {
		String input = ssa.convertToStr(new String[] { "!za", "abc", "Zz", "+" });
		String expected = ssa.convertToStr(new String[] { "!za", "+", "Zz", "abc" });
		assertEquals(expected, ssa.sortSimpleCapitalSpecialChars(input));
	}

	@Test
	public void testSortSimpleCapitalNumber() {
		String input = ssa.convertToStr(new String[] { "1za", "abc", "Zz", "100" });
		String expected = ssa.convertToStr(new String[] { "100", "1za", "Zz", "abc" });
		assertEquals(expected, ssa.sortSimpleCapitalNumber(input));
	}

	@Test
	public void testSortCapitalNumberSpecialChars() {
		String input = ssa.convertToStr(new String[] { "!ZA", "-A", "10", "+" });
		String expected = ssa.convertToStr(new String[] { "!ZA", "+", "-A", "10" });
		assertEquals(expected, ssa.sortCapitalNumbersSpecialChars(input));
	}

	@Test
	public void testSortAll() {
		String input = ssa.convertToStr(new String[] { "!ZA", "-A", "10", "+", "!aA", "\n", "2", "1A" });
		String expected = ssa.convertToStr(new String[] { "\n", "!ZA", "!aA", "+", "-A", "10", "1A", "2" });
		assertEquals(expected, ssa.sortAll(input));
	}

	@Test
	public void testSortAllWithFirstWordNumber() {
		String input = ssa.convertToStr(new String[] { "!ZA", "-A", "10", "+", "!aA", "\n", "2", "1A" });
		String expected = ssa.convertToStr(new String[] { "\n", "!ZA", "!aA", "+", "-A", "2", "10", "1A" });
		assertEquals(expected, ssa.sortAllWithFirstWordNumber(ssa.sortAll(input)));
	}

	/*
	 * @Test public void testReOrder2(){ assertTrue(Arrays.equals(new
	 * String[]{"-n", reorderStr1, reorderStr2}, ssa.reOrder(new
	 * String[]{reorderStr1, "-n", reorderStr2}))); }
	 * 
	 * @Test public void testReOrder3(){ assertTrue(Arrays.equals(new
	 * String[]{"-n", reorderStr1, reorderStr2}, ssa.reOrder(new String[]{"-n",
	 * reorderStr1, reorderStr2}))); }
	 * 
	 * @Test public void testReOrder4(){ assertTrue(Arrays.equals(new
	 * String[]{"-n", reorderStr1}, ssa.reOrder(new String[]{reorderStr1,
	 * "-n"}))); }
	 * 
	 * @Test public void testReOrder5(){ assertTrue(Arrays.equals(new
	 * String[]{"-n"}, ssa.reOrder(new String[]{"-n"}))); }
	 * 
	 * @Test(expected = SortException.class) public void moreThanOneOpt() throws
	 * SortException { // More than 1 -n ssa.validate(new String[]{"-n", "-n"});
	 * }
	 * 
	 * @Test public void validArgs() throws SortException { ssa.validate(new
	 * String[]{"-n", fileName, fileName}); }
	 * 
	 * @Test public void readFromInputStream() throws SortException {
	 * assertEquals(defaultString,
	 * SortExtendedFunctions.readFromInputStream(stdin)); }
	 * 
	 * @Test public void switchLeftRight(){ ArrayList<String> temp = new
	 * ArrayList<String>(); temp.add("b"); temp.add("a"); ArrayList<String>
	 * compareTemp =
	 * (ArrayList<String>)SortExtendedFunctions.switchLeftRight(temp, 1);
	 * temp.clear(); temp.add("a"); temp.add("b"); assertEquals(compareTemp ,
	 * temp); }
	 * 
	 * @Test public void bubbleSort(){ ArrayList<String> list = new
	 * ArrayList<String>(); list.add("10"); list.add("1"); list.add("2");
	 * ArrayList<String> compareList = new ArrayList<String>();
	 * compareList.add("1"); compareList.add("10"); compareList.add("2");
	 * assertEquals(SortExtendedFunctions.bubbleSort(list), compareList); }
	 * 
	 * @Test public void numericBubbleSort(){ ArrayList<String> list = new
	 * ArrayList<String>(); list.add("10"); list.add("1"); list.add("2");
	 * ArrayList<String> compareList = new ArrayList<String>();
	 * compareList.add("1"); compareList.add("2"); compareList.add("10");
	 * assertEquals(SortExtendedFunctions.numericBubbleSort(list), compareList);
	 * }
	 */

	@Test
	public void sortAllNumericInputStream() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		ssa.run(null, numericStdin, System.out);
		System.out.flush();
		assertEquals("1\n10\n2\n", baos.toString());
	}

	@Test
	public void sortAllNumericFile() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		args = new String[2];
		args[0] = numericFile;
		args[1] = "-n";
		ssa.run(args, null, System.out);
		System.out.flush();
		assertEquals("1\n2\n10\n", baos.toString());
	}

	@Test
	public void sortAllInputStream() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		ssa.run(null, stdin, System.out);
		System.out.flush();
		assertEquals(sortedString, baos.toString());
	}

	@Test
	public void sortAllFile() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		args = new String[1];
		args[0] = fileName;
		ssa.run(args, null, System.out);
		System.out.flush();
		assertEquals(sortedString, baos.toString());
	}

	@Test
	public void testEmptyStream() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		ssa.run(null, emptyStdin, System.out);
		System.out.flush();
		assertEquals("", baos.toString());
	}

	@Test
	public void testEmptyFile() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		args = new String[1];
		args[0] = emptyFile;
		ssa.run(args, null, System.out);
		System.out.flush();
		assertEquals("", baos.toString());
	}

	@Test
	public void testMultipleFiles() throws SortException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		args = new String[3];
		args[0] = "-n";
		args[1] = testMethodsFile;
		args[2] = numericFile;
		ssa.run(args, null, System.out);
		System.out.flush();
		assertEquals("\n+\n1\n2\n5\nA\nB\na\nb\n", baos.toString());
	}

}
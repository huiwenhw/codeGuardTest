package ef1test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.app.SortApplication;

public class SortUnitTest {

	static SortApplication sortApp;
	private String directory;
	// test case decisions:
	// Test with cases where the differences are in varying positions of strings
	// e.g. first characters differ, start diverging from second char, first few
	// chars identical but later different, no similarity, etc
	// so that as many variations are tested without having to test exhaustively

	// corner cases being tested are:
	// 1. empty string
	// 2. identical strings
	// 3. strings identical up to middle, then differing
	// 4. strings identical up to middle, then one ending
	// 5. strings different up to middle, then identical
	// 6. strings with different numbers at beginning
	// 7. strings with different numbers in middle or end
	// 8. strings identical but with offset (caused by additional char(s) at
	// beginning)
	// 9. numbers with same numeric value but different strings (e.g. 2, 02,
	// 002...)
	//

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sortApp = new SortApplication();
	}

/*	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}*/

	@Before
	public void setUp() throws Exception {
		directory = "sg.edu.nus.comp.cs4218.eftest/testFiles/";
	}

/*	@After
	public void tearDown() throws Exception {
	}
*/
	@Test
	public void testSortStringsSimple() {
		String input = getFileContent( directory +"sortAppTestSimple.txt");
		String expected = "a\naaa\naaaa\nabc\nbaaa";
		assertEquals(expected, sortApp.sortStringsSimple(input));
	}

	@Test
	public void testSortStringsSimpleNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimple.txt");
		String expected = "a\naaa\naaaa\nabc\nbaaa";
		assertEquals(expected, sortApp.sortStringsSimple(input));
	}

	@Test
	public void testSortStringsCapital() {
		String input = getFileContent( directory +"sortAppTestCapital.txt");
		String expected = "BATMAN\nSEVENELEVEN\nSUPER\nSUPERMAN\nSUPERMARKET\nSUPPER";
		assertEquals(expected, sortApp.sortStringsCapital(input));
	}

	@Test
	public void testSortStringsCapitalNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestCapital.txt");
		String expected = "BATMAN\nSEVENELEVEN\nSUPER\nSUPERMAN\nSUPERMARKET\nSUPPER";
		assertEquals(expected, sortApp.sortStringsCapital(input));
	}

	@Test
	public void testSortStringsNumbers() {
		String input = getFileContent( directory +"sortAppTestNumbers.txt");
		String expected = "001\n01\n1\n10\n2\n210";
		assertEquals(expected, sortApp.sortNumbers(input));
	}

	@Test
	public void testSortStringsNumbersNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestNumbers.txt");
		String expected = "001\n01\n1\n2\n10\n210";
		assertEquals(expected, sortApp.sortNumbers(input));
	}

	@Test
	public void testSortStringsSpecialChars() {
		String input = getFileContent( directory +"sortAppTestSpecialChars.txt");
		String expected = "\n \n  \n!?@#>\n%)*^%\n%^!\n??\n????";
		assertEquals(expected, sortApp.sortSpecialChars(input));
	}

	@Test
	public void testSortStringsSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSpecialChars.txt");
		String expected = "\n \n  \n!?@#>\n%)*^%\n%^!\n??\n????";
		assertEquals(expected, sortApp.sortSpecialChars(input));
	}

	@Test
	public void testSortStringsSimpleCapital() {
		String input = getFileContent( directory +"sortAppTestSimpleCapital.txt");
		String expected = "AA\nAAAA\nAAAAA\naBCd\naaaa\nbcD";
		assertEquals(expected, sortApp.sortSimpleCapital(input));
	}

	@Test
	public void testSortStringsSimpleCapitalNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimpleCapital.txt");
		String expected = "AA\nAAAA\nAAAAA\naBCd\naaaa\nbcD";
		assertEquals(expected, sortApp.sortSimpleCapital(input));
	}

	@Test
	public void testSortStringsSimpleNumbers() {
		String input = getFileContent( directory +"sortAppTestSimpleNumbers.txt");
		String expected = "03\n1\n10th\n1ine\n3rd\nline\nline1\nline10\nline3";
		assertEquals(expected, sortApp.sortSimpleNumbers(input));
	}

	@Test
	public void testSortStringsSimpleNumbersNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimpleNumbers.txt");
		String expected = "1\n03\n10th\n1ine\n3rd\nline\nline1\nline10\nline3";
		assertEquals(expected, sortApp.sortSimpleNumbers(input));
	}

	@Test
	public void testSortStringsSimpleSpecialChars() {
		String input = getFileContent( directory +"sortAppTestSimpleSpecialChars.txt");
		String expected = "\n \n? why\n?!%^@\nport^\nsore throat\nsort!";
		assertEquals(expected, sortApp.sortSimpleSpecialChars(input));
	}

	@Test
	public void testSortStringsSimpleSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimpleSpecialChars.txt");
		String expected = "\n \n? why\n?!%^@\nport^\nsore throat\nsort!";
		assertEquals(expected, sortApp.sortSimpleSpecialChars(input));
	}

	@Test
	public void testSortStringsCapitalNumbers() {
		String input = getFileContent( directory +"sortAppTestCapitalNumbers.txt");
		String expected = "50\n500\n6X\n6X\nSTRING10\nSTRING5\nSTRING50";
		assertEquals(expected, sortApp.sortCapitalNumbers(input));
	}

	@Test
	public void testSortStringsCapitalNumbersNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestCapitalNumbers.txt");
		String expected = "50\n500\n6X\n6X\nSTRING10\nSTRING5\nSTRING50";
		assertEquals(expected, sortApp.sortCapitalNumbers(input));
	}

	@Test
	public void testSortStringsCapitalSpecialChars() {
		String input = getFileContent( directory +"sortAppTestCapitalSpecialChars.txt");
		String expected = "\n\n  \n!(@THIGH\n@THIGH\nTHIGH\nTHIS IS SPA\nTHIS IS SPARTA!\nTHIS-IS-MADNESS";
		assertEquals(expected, sortApp.sortCapitalSpecialChars(input));
	}

	@Test
	public void testSortStringsCapitalSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestCapitalSpecialChars.txt");
		String expected = "\n\n  \n!(@THIGH\n@THIGH\nTHIGH\nTHIS IS SPA\nTHIS IS SPARTA!\nTHIS-IS-MADNESS";
		assertEquals(expected, sortApp.sortStringsSimple(input));
	}

	@Test
	public void testSortStringsNumbersSpecialChars() {
		String input = getFileContent( directory +"sortAppTestNumbersSpecialChars.txt");
		String expected = "$(@&34\n$(@&7\n<   >\n<-0->\n12$(@&34\n2$(@&34";
		assertEquals(expected, sortApp.sortNumbersSpecialChars(input));
	}

	@Test
	public void testSortStringsNumbersSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestNumbersSpecialChars.txt");
		String expected = "$(@&34\n$(@&7\n<   >\n<-0->\n12$(@&34\n2$(@&34";
		assertEquals(expected, sortApp.sortNumbersSpecialChars(input));
	}

	@Test
	public void testSortStringsSimpleCapitalNumbers() {
		String input = getFileContent( directory +"sortAppTestSimpleCapitalNumbers.txt");
		String expected = "21stCentury\n30TH\n30th\n3rdPlace\nM24Chaffee\nM40Rifle\nM4Sherman\nm4\nmnm";
		assertEquals(expected, sortApp.sortStringsSimple(input));
	}

	@Test
	public void testSortStringsSimpleCapitalNumbersNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimpleCapitalNumbers.txt");
		String expected = "21stCentury\n30TH\n30th\n3rdPlace\nM24Chaffee\nM40Rifle\nM4Sherman\nm4\nmnm";
		assertEquals(expected, sortApp.sortSimpleCapitalNumber(input));
	}

	@Test
	public void testSortStringsSimpleCapitalSpecialChars() {
		String input = getFileContent( directory +"sortAppTestSimpleCapitalSpecialChars.txt");
		String expected = " Hello, World!\n!! Hello\nHELLO WORLD\nHella lot of tests!\nHello World!\nHello World!\nHello World@!\nHello world!\nHello, World!\nHello,World!\nhello?";
		assertEquals(expected, sortApp.sortSimpleCapitalSpecialChars(input));
	}

	@Test
	public void testSortStringsSimpleCapitalSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimpleCapitalSpecialChars.txt");
		String expected = " Hello, World!\n!! Hello\nHELLO WORLD\nHella lot of tests!\nHello World!\nHello World!\nHello World@!\nHello world!\nHello, World!\nHello,World!\nhello?";
		assertEquals(expected, sortApp.sortSimpleCapitalSpecialChars(input));
	}

	@Test
	public void testSortStringsSimpleNumbersSpecialChars() {
		String input = getFileContent( directory +"sortAppTestSimpleNumbersSpecialChars.txt");
		String expected = "!arg==\"-n\"\n&arg\n4=3\n40=3\n6=3\narg==\"-n\"\narg=3\nargs[40]\nargs[7]\narray.get(2)";
		assertEquals(expected, sortApp.sortSimpleNumbersSpecialChars(input));
	}

	@Test
	public void testSortStringsSimpleNumbersSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestSimpleNumbersSpecialChars.txt");
		String expected = "!arg==\"-n\"\n&arg\n4=3\n40=3\n6=3\narg==\"-n\"\narg=3\nargs[40]\nargs[7]\narray.get(2)";
		assertEquals(expected, sortApp.sortSimpleNumbersSpecialChars(input));
	}

	@Test
	public void testSortStringsCapitalNumbersSpecialChars() {
		String input = getFileContent( directory +"sortAppTestCapitalNumbersSpecialChars.txt");
		String expected = "!0AA\n!AAA\n%AAA\n10AAA\n2A\n2AAA\n2BAA\nAAA10\nAAA2\nAAAA2\nAAB2";
		assertEquals(expected, sortApp.sortCapitalNumbersSpecialChars(input));
	}

	@Test
	public void testSortStringsCapitalNumbersSpecialCharsNumerically() {
		String input = "-n " + getFileContent(directory+"sortAppTestCapitalNumbersSpecialChars.txt");
		String expected = "!0AA\n!AAA\n%AAA\n10AAA\n2A\n2AAA\n2BAA\nAAA10\nAAA2\nAAAA2\nAAB2";
		assertEquals(expected, sortApp.sortCapitalNumbersSpecialChars(input));
	}

	@Test
	public void testSortStringsAll() {
		String input = getFileContent( directory +"sortAppTestAll.txt");
		String expected = "\n  \n! hit\n!@#%\n//WARNING//\n/4run\n/ru28!*#\n/run script\n3 damage dealt\n30 damage dealt\n6 damage\n6 damage dealt\nGain 30 more exp\nGain0205's message:\nGained [30] gold\nGained [6] exp\ncome here\ncommand unknown\ngain 30 more exp\nok\nokay";
		assertEquals(expected, sortApp.sortAll(input));
	}

	@Test
	public void testSortStringsAllNumerically() {
		String input = getFileContent( directory +"sortAppTestAll.txt");
		String expected = "\n  \n! hit\n!@#%\n//WARNING//\n/4run\n/ru28!*#\n/run script\n3 damage dealt\n6 damage\n6 damage dealt\n30 damage dealt\nGain 30 more exp\nGain0205's message:\nGained [30] gold\nGained [6] exp\ncome here\ncommand unknown\ngain 30 more exp\nok\nokay";
		assertEquals(expected, sortApp.sortAllWithFirstWordNumber(input));
	}

	private String getFileContent(String fileName) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}

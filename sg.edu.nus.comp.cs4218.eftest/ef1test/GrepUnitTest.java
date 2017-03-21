package ef1test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.GrepException;
import sg.edu.nus.comp.cs4218.impl.app.GrepApplication;

public class GrepUnitTest {
	private static final String PATTERN_NOT_FOUND = "Pattern not found";
	static GrepApplication grepApp;
	private static InputStream stdin;
	private static File file;
	private static File file1;
	private static File file2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		grepApp = new GrepApplication();
		file = new File("file.txt");
		file.createNewFile();
		file1 = new File("file1.txt");
		file1.createNewFile();
		file2 = new File("file2.txt");
		file2.createNewFile();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		file.delete();
		file1.delete();
		file2.delete();
	}

/*	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}*/

	// stdin: "moose\nrat\ncat\ncrocodile\nseahorse\nmouse\nmongoose\nelk"
	@Test
	public void testGrepFromStdinBasic() throws GrepException {
		String input = "se";
		String expected = "moose\nseahorse\nmouse\nmongoose\n";
		stdin = new ByteArrayInputStream("moose\nrat\ncat\ncrocodile\nseahorse\nmouse\nmongoose\nelk".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "line 4\nline 2\nline 3\nline 1"
	@Test
	public void testGrepFromStdinAllMatch() throws GrepException {
		String input = "line";
		String expected = "line 4\nline 2\nline 3\nline 1\n";
		stdin = new ByteArrayInputStream("line 4\nline 2\nline 3\nline 1".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: ""
	@Test
	public void testGrepFromStdinEmpty() throws GrepException {
		String input = "line";
		String expected = PATTERN_NOT_FOUND;
		stdin = new ByteArrayInputStream("".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "Asia\nAfrica\nAmerica\nEurope\nOceania"
	@Test
	public void testGrepFromStdinNotFound() throws GrepException {
		String input = "greenland";
		String expected = PATTERN_NOT_FOUND;
		stdin = new ByteArrayInputStream("Asia\nAfrica\nAmerica\nEurope\nOceania".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "Dog\nCat\nDragon\nMcDonald\nDeer\nDoe"
	@Test
	public void testGrepFromStdinFoundCaseMatch() throws GrepException {
		String input = "Do";
		String expected = "Dog\nMcDonald\nDoe\n";
		stdin = new ByteArrayInputStream("Dog\nCat\nDragon\nMcDonald\nDeer\nDoe".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "Rudolf\nIdol\ncobra\nScreen door\nGift"
	@Test
	public void testGrepFromStdinFoundCaseMismatch() throws GrepException {
		String input = "Do";
		String expected = PATTERN_NOT_FOUND;
		stdin = new ByteArrayInputStream("Rudolf\nIdol\ncobra\nScreen door\nGift".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "NUS\nMuseum\nUSA\nscience\nTeach us\nUSB"
	@Test
	public void testGrepFromStdinFoundCaseMatchAndMismatch() throws GrepException {
		String input = "US";
		String expected = "NUS\nUSA\nUSB\n";
		stdin = new ByteArrayInputStream("NUS\nMuseum\nUSA\nscience\nTeach us\nUSB".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "\n\n\n\n"
	@Test
	public void testGrepFromStdinOnlyNewlines() throws GrepException {
		String input = "search";
		String expected = PATTERN_NOT_FOUND;
		stdin = new ByteArrayInputStream("\n\n\n\n".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "\n\n\n\n\n"
	@Test
	public void testGrepFromStdinNewlinesSearchNewlines() throws GrepException {
		String input = "\n";
		String expected = "\n";
		stdin = new ByteArrayInputStream("\n\n\n\n\n".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "Scabbard\nBastion\nsmartphone\nTextbook\nbulb"
	@Test
	public void testGrepFromStdinMultipleOccurences() throws GrepException {
		String input = "b";
		String expected = "Scabbard\nTextbook\nbulb\n";
		stdin = new ByteArrayInputStream("Scabbard\nBastion\nsmartphone\nTextbook\nbulb".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "CS4218\nCS3201\nCS2106\nCS4218\nCS2103T"
	@Test
	public void testGrepFromStdinDuplicateLinesMatch() throws GrepException {
		String input = "21";
		String expected = "CS4218\nCS2106\nCS4218\nCS2103T";
		stdin = new ByteArrayInputStream("CS4218\nCS3201\nCS2106\nCS4218\nCS2103T".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "line 1\nline 5\nline 10\nline 15\nline 20"
	@Test
	public void testGrepFromStdinNumeric() throws GrepException {
		String input = "1";
		String expected = "line 1\nline 10\nline 15";
		stdin = new ByteArrayInputStream("line 1\nline 5\nline 10\nline 15\nline 20".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "Do it!\n!CONGRATULATIONS!\n#Gxh#A@%\n"
	@Test
	public void testGrepFromStdinSpecialChars() throws GrepException {
		String input = "!";
		String expected = "Do it!\n!CONGRATULATIONS!";
		stdin = new ByteArrayInputStream("Do it!\n!CONGRATULATIONS!\n#Gxh#A@%\n".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// stdin: "R2-D2\nC-3PO\nR2-d5\nCAR2-door8\nr2D2\nDnDe5"
	@Test
	public void testGrepFromStdinMixture() throws GrepException {
		String input = "R2-d";
		String expected = "R2-d5\nCAR2-door8";
		stdin = new ByteArrayInputStream("R2-D2\nC-3PO\nR2-d5\nCAR2-door8\nr2D2\nDnDe5".getBytes());
		assertEquals(expected.trim(), grepApp.grepFromStdin(input, stdin));
	}

	// Assumption: run() will do the validity check for files,
	// hence nonexistent/unreadable files are not tested

	// file.txt contents:
	// "moose\nrat\ncat\ncrocodile\nseahorse\nmouse\nmongoose\nelk"
	@Test
	public void testGrepFromOneFileBasic() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("moose\nrat\ncat\ncrocodile\nseahorse\nmouse\nmongoose\nelk");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "se file.txt";
		String expected = "moose\nseahorse\nmouse\nmongoose";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "line 4\nline 2\nline 3\nline 1"
	@Test
	public void testGrepFromOneFileAllMatch() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("line 4\nline 2\nline 3\nline 1");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "line file.txt";
		String expected = "line 4\nline 2\nline 3\nline 1";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: ""
	@Test
	public void testGrepFromOneFileEmpty() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "line file.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "Asia\nAfrica\nAmerica\nEurope\nOceania"
	@Test
	public void testGrepFromOneFileNotFound() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("Asia\nAfrica\nAmerica\nEurope\nOceania");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "greenland file.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// stdfile.txt contents: "Dog\nCat\nDragon\nMcDonald\nDeer\nDoe"
	@Test
	public void testGrepFromOneFileFoundCaseMatch() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("Dog\nCat\nDragon\nMcDonald\nDeer\nDoe");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "Do file.txt";
		String expected = "Dog\nMcDonald\nDoe";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "Rudolf\nIdol\ncobra\nScreen door\nGift"
	@Test
	public void testGrepFromOneFileFoundCaseMismatch() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("Rudolf\nIdol\ncobra\nScreen door\nGift");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "Do file.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "NUS\nMuseum\nUSA\nscience\nTeach us\nUSB"
	@Test
	public void testGrepFromOneFileFoundCaseMatchAndMismatch() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("NUS\nMuseum\nUSA\nscience\nTeach us\nUSB");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "US file.txt";
		String expected = "NUS\nUSA\nUSB";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "\n\n\n\n"
	@Test
	public void testGrepFromOneFileOnlyNewlines() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("\n\n\n\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "search file.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "\n\n\n\n\n"
	@Test
	public void testGrepFromOneFileNewlinesSearchNewlines() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("\n\n\n\n\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "\n file.txt";
		String expected = "\n\n\n\n\n";
		assertEquals(expected, grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "Scabbard\nBastion\nsmartphone\nTextbook\nbulb"
	@Test
	public void testGrepFromOneFileMultipleOccurences() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("Scabbard\nBastion\nsmartphone\nTextbook\nbulb");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "b file.txt";
		String expected = "Scabbard\nTextbook\nbulb\n";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "line 1\nline 5\nline 10\nline 15\nline 20"
	@Test
	public void testGrepFromOneFileNumeric() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("line 1\nline 5\nline 10\nline 15\nline 20");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "1 file.txt";
		String expected = "line 1\nline 10\nline 15\n";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "Do it!\n!CONGRATULATIONS!\n#Gxh#A@%\n"
	@Test
	public void testGrepFromOneFileSpecialChars() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("Do it!\n!CONGRATULATIONS!\n#Gxh#A@%\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "! file.txt";
		String expected = "Do it!\n!CONGRATULATIONS!\n";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// file.txt contents: "R2-D2\nC-3PO\nR2-d5\nCAR2-door8\nr2D2\nDnDe5"
	@Test
	public void testGrepFromOneFileMixture() throws GrepException {
		try {
			FileWriter writer = new FileWriter("file.txt", false);
			writer.write("R2-D2\nC-3PO\nR2-d5\nCAR2-door8\nr2D2\nDnDe5");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "R2-d file.txt";
		String expected = "R2-d5\nCAR2-door8\n";
		assertEquals(expected.trim(), grepApp.grepFromOneFile(input));
	}

	// since string match functionality itself is already tested above,
	// repeating for multiple files would be redundant
	// tests for grep for multiple files will focus on the various ways files
	// may come in.
	// also, since grep is simply a line by line substring matching, tests will
	// not deal with trying to test with many files
	// instead for tests the files will be limited to 2

	// file1.txt contents: "odd 1\neven 2\n"
	// file2.txt contents: "odd 3\neven 4\n"
	@Test
	public void testGrepFromMultipleFilesMatchInBoth() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("odd 1\neven 2\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("odd 3\neven 4\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "odd file1.txt file2.txt";
		String expected = "odd 1\nodd 3\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "can\nbar\ntan\nwar\nfar\n"
	// file2.txt contents: "cap\nbat\nhat\nmap\n"
	@Test
	public void testGrepFromMultipleFilesMatchInFirstOnly() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("can\nbar\ntan\nwar\nfar\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("cap\nbat\nhat\nmap\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "ar file1.txt file2.txt";
		String expected = "bar\nwar\nfar\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "can\nbar\ntan\nwar\nfar\n"
	// file2.txt contents: "cap\nbat\nhat\nmap\n"
	@Test
	public void testGrepFromMultipleFilesMatchInSecondOnly() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("can\nbar\ntan\nwar\nfar\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("cap\nbat\nhat\nmap\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "ap file1.txt file2.txt";
		String expected = "cap\nmap\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "line 1\nline 2\n"
	// file2.txt contents: "row 3\nrow 4\n"
	@Test
	public void testGrepFromMultipleFilesNoMatch() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("line 1\nline 2\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("row 3\nrow 4\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "a file1.txt file2.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "line 1\nline 2\n"
	// file2.txt contents: "row 3\nrow 4\n"
	@Test
	public void testGrepFromMultipleFilesNoOverlap() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("line 1\nline 2\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("row 3\nrow 4\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "a file1.txt file2.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "moose\nmouse\ngoose\n"
	// file2.txt contents: "mouse\nrat\nguinea pig\n"
	@Test
	public void testGrepFromMultipleFilesMatchInOverlap() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("moose\nmouse\ngoose\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("mouse\nrat\nguinea pig\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "use file1.txt file2.txt";
		String expected = "mouse\nmouse\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "moose\nmouse\ngoose\n"
	// file2.txt contents: "mouse\nrat\nguinea pig\n"
	@Test
	public void testGrepFromMultipleFilesMatchNotInOverlap() { // same files as
																// above
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("moose\nmouse\ngoose\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("mouse\nrat\nguinea pig\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "ose file1.txt file2.txt"; // different pattern
		String expected = "moose\ngoose\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "moose\nmouse\ngoose\n"
	// file2.txt contents: "mouse\nrat\nguinea pig\n"
	@Test
	public void testGrepFromMultipleFilesOverlapNoMatch() { // same files as
															// above
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("moose\nmouse\ngoose\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("mouse\nrat\nguinea pig\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "mango file1.txt file2.txt"; // different pattern
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "apple\nbanana\ncan\n"
	// file2.txt contents: "apple\nbanana\ncan\n"
	@Test
	public void testGrepFromMultipleFilesIdenticalMatch() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("apple\nbanana\ncan\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("apple\nbanana\ncan\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "an file1.txt file2.txt"; // different pattern
		String expected = "banana\ncan\nbanana\ncan\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "apple\nbanana\ncan\n"
	// file2.txt contents: "apple\nbanana\ncan\n"
	@Test
	public void testGrepFromMultipleFilesIdenticalNoMatch() { // same files as
																// above
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("apple\nbanana\ncan\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("apple\nbanana\ncan\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "art file1.txt file2.txt"; // different pattern
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "abba\nqwerty\nZxcAsdQwe\n"
	// file2.txt contents: "poi\nPOI\njoking\nBoink\n"
	@Test
	public void testGrepFromMultipleFilesNoCommon() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("abba\nqwerty\nZxcAsdQwe\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("poi\nPOI\njoking\nBoink\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "oi file1.txt file2.txt"; // different pattern
		String expected = "poi\nBoink\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: ""
	// file2.txt contents: "apple\nbanana\ncan\n"
	@Test
	public void testGrepFromMultipleFilesFirstEmpty() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("apple\nbanana\ncan\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "an file1.txt file2.txt";
		String expected = "banana\ncan\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: "apple\nbanana\ncan\n"
	// file2.txt contents: ""
	@Test
	public void testGrepFromMultipleFilesSecondEmpty() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("apple\nbanana\ncan\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "an file1.txt file2.txt";
		String expected = "banana\ncan\n";
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// file1.txt contents: ""
	// file2.txt contents: ""
	@Test
	public void testGrepFromMultipleFilesBothEmpty() {
		try {
			FileWriter writer = new FileWriter("file1.txt", false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter("file2.txt", false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String input = "an file1.txt file2.txt";
		String expected = PATTERN_NOT_FOUND;
		assertEquals(expected.trim(), grepApp.grepFromMultipleFiles(input));
	}

	// Assumption: if pattern is invalid, the invoked method will return empty
	// string to run(),
	// to indicate that given pattern was invalid, upon which run() will throw
	// an exception

	@Test
	public void testGrepInvalidPatternInStdin() {
		String input = "[0-9]++";
		String expected = "Invalid Pattern";
		assertEquals(expected.trim(), grepApp.grepInvalidPatternInStdin(input));
	}

	@Test
	public void testGrepInvalidPatternInFile() {
		String input = "[0-9]++ file.txt";
		String expected = "Invalid Pattern";
		assertEquals(expected.trim(), grepApp.grepInvalidPatternInFile(input));
	}
}

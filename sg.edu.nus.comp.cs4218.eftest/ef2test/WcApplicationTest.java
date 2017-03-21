package ef2test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.app.WcApplication;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class WcApplicationTest {

	private static final String INVALID_OPTION = "wc: Invalid option type";
	static WcApplication wcApp;
	static InputStream inputS;
	static OutputStream outputS;
	String directory;
	String file1, file2, file3, fileSpace, fileNewLine;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		wcApp = new WcApplication();
		outputS = new ByteArrayOutputStream();
	}

	/*
	 * @AfterClass public static void tearDownAfterClass() throws Exception { }
	 */

	@Before
	public void setUp() throws Exception {
		directory = "sg.edu.nus.comp.cs4218.eftest/testFiles/";
		file1 = directory + "fileTest.txt";
		file2 = directory + "test2.txt";
		file3 = directory + "test.txt";
		fileSpace = directory + "fileTestOnlySpaces.txt";
		fileNewLine = directory + "fileTestOnlyNewLines.txt";
	}

	@After
	public void tearDown() throws Exception {
		outputS = new ByteArrayOutputStream();
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintCharacterCountInFile() {
		String args = file3;
		String answer = "";
		try {
			answer = wcApp.printCharacterCountInFile(args);
		} catch (Exception e) {

		}
		assertEquals("27", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintWordCountInFile() {
		String args = file3;
		String answer = "";
		try {
			answer = wcApp.printWordCountInFile(args);
		} catch (Exception e) {

		}
		assertEquals("8", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintNewlineCountInFile() {
		String args = file3;
		String answer = "";
		try {
			answer = wcApp.printNewlineCountInFile(args);
		} catch (Exception e) {

		}
		assertEquals("4", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintCharacterCountInStdin() {
		String args = "line 1\nline 2\nline 3\nline 4";
		String answer = "";
		try {
			answer = wcApp.printCharacterCountInStdin(args);
		} catch (Exception e) {

		}
		assertEquals("27", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintWordCountInStdin() {
		String args = "line 1\nline 2\nline 3\nline 4";
		String answer = "";
		try {
			answer = wcApp.printWordCountInStdin(args);
		} catch (Exception e) {

		}
		assertEquals("8", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintNewlineCountInStdin() {
		String args = "line 1\nline 2\nline 3\nline 4";
		String answer = "";
		try {
			answer = wcApp.printNewlineCountInStdin(args);
		} catch (Exception e) {

		}
		assertEquals("4", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintAllCountsInFile() {
		String args = file3;
		String answer = "";
		try {
			answer = wcApp.printAllCountsInFile(args);
		} catch (Exception e) {

		}
		assertEquals("27 8 4", answer);
	}

	@Test
	// Notice all the interfaces in Wc has been slightly changed. The args does
	// not mean the command anymore, but the content from the input
	public void testPrintAllCountsInStdin() {
		String args = "line 1\nline 2\nline 3\nline 4";
		String answer = "";
		try {
			answer = wcApp.printAllCountsInStdin(args);
		} catch (Exception e) {

		}
		assertEquals("27 8 4", answer);
	}

	@Test
	public void testWc() {
		String[] args = { file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcEmptyFile() {
		String[] args = { directory + "emptyDoc.txt" };
		String expected = "0 0 0 " + directory + "emptyDoc.txt\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOnlyNewlines() {
		String[] args = { fileNewLine };
		String expected = "6 0 6 " + fileNewLine + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOnlySpaces() {
		String[] args = { fileSpace };
		String expected = "5 0 1 " + fileSpace + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountChars() {
		String[] args = { "-m", file1 };
		String expected = "947 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountWords() {
		String[] args = { "-w", file1 };
		String expected = "181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountLines() {
		String[] args = { "-l", file1 };
		String expected = "32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountCharsWords() {
		String[] args = { "-m", "-w", file1 };
		String expected = "947 181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountWordsLines() {
		String[] args = { "-w", "-l", file1 };
		String expected = "181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountCharsLines() {
		String[] args = { "-m", "-l", file1 };
		String expected = "947 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountWordsChars() {
		String[] args = { "-w", "-m", file1 };
		String expected = "947 181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountLinesWords() {
		String[] args = { "-l", "-w", file1 };
		String expected = "181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcCountLinesChars() {
		String[] args = { "-l", "-m", file1 };
		String expected = "947 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcAllOptions() {
		String[] args = { "-m", "-w", "-l", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcAllOptionsDiffOrderoptWoptLoptM() {
		String[] args = { "-w", "-l", "-m", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcAllOptionsDiffOrderoptLoptMoptW() {
		String[] args = { "-l", "-m", "-w", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	public void testWcAllOptionsDiffOrderoptLoptWoptM() {
		String[] args = { "-l", "-w", "-m", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionsOverlappingoptMoptMoptL() {
		String[] args = { "-m", "-m", "-l", file1 };
		String expected = "947 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionsOverlappingoptWoptLoptW() {
		String[] args = { "-w", "-l", "-w", file1 };
		String expected = "181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionsOverlappingoptWoptWoptWoptW() {
		String[] args = { "-w", "-w", "-w", "-w", file1 };
		String expected = "181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMulticharOptionsoptMW() {
		String[] args = { "-mw", file1 };
		String expected = "947 181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultiCharOptionsoptWL() {
		String[] args = { "-wl", file1 };
		String expected = "181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultiCharOptionsoptML() {
		String[] args = { "-ml", file1 };
		String expected = "947 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultiCharOptionsoptLM() {
		String[] args = { "-lm", file1 };
		String expected = "947 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultiCharOptionsoptWWW() {
		String[] args = { "-www", file1 };
		String expected = "181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultiCharOptionsoptMWL() {
		String[] args = { "-mwl", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultiCharOptionsoptWLM() {
		String[] args = { "-wlm", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMulticharOptionsOverlappingoptMoptWM() {
		String[] args = { "-m", "-wm", file1 };
		String expected = "947 181 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMulticharOptionsOverlappingoptMLoptMM() {
		String[] args = { "-ml", "-mm", "-l", file1 };
		String expected = "947 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMulticharOptionsOverlappingoptWWMoptMLLMoptWoptWL() {
		String[] args = { "-wwm", "-mllm", "-w", "-wl", file1 };
		String expected = "947 181 32 " + file1 + "\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultipleFiles() {
		String[] args = { file1, file2 };
		String expected = "947 181 32 " + file1 + "\n86 24 12 " + file2 + "\n" + "1033 205 44 total\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionsoptMoptMultipleFiles() {
		String[] args = { "-m", file1, file2 };
		String expected = "947 " + file1 + "\n86 " + file2 + "\n" + "1033 total\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionsoptMLoptMultipleFiles() {
		String[] args = { "-ml", file1, file3 };
		String expected = "947 32 " + file1 + "\n27 4 " + file3 + "\n" + "974 36 total\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionsoptLoptWLoptMultipleFiles() {
		String[] args = { "-l", "-wl", file2, file1 };
		String expected = "24 12 " + file2 + "\n181 32 " + file1 + "\n" + "205 44 total\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcInvalidFile() {
		String[] args = { "asdf.txt" };
		String expected = "Could not read file\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultipleFilesInvalid() {
		String[] args = { "asdf.txt", "querty.txt", "montypython" };
		String expected = "Could not read file\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcMultipleFilesValidInvalidValid() {
		String[] args = { file1, "m", fileSpace };
		String expected = "947 181 32 " + file1 + "\nCannot read m\n5 0 1 " + fileSpace + "\n952 181 33 total\n";
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {

		}
		assertEquals(expected, outputS.toString());
	}

	@Test
	public void testWcOptionInvalid() {
		String[] args = { "-s", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcMultipleOptionsInvalidValid() {
		String[] args = { "-a", "-m", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcMultipleOptionsValidInvalid() {
		String[] args = { "-m", "-t", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcMultipleOptionsMixed() {
		String[] args = { "-m", "-a", "-s", "-m", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcMulticharOptionInvalid() {
		String[] args = { "-asd", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcMulticharOptionsInvalidoptMAWoptLST() {
		String[] args = { "-maw", "-lst", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcMulticharOptionsInvalidoptMLoptLWoptTASD() {
		String[] args = { "-ml", "-lw", "-tasd", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcInvalidOptionsMultipleFiles() {
		String[] args = { "-a", file1, file2 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcInvalidOptionsoptLAMoptMultipleFiles() {
		String[] args = { "-lam", file1, file2, file3 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcInvalidOptionInvalidFile() {
		String[] args = { "-x", "test2" };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcInvalidOptionsoptLMoptToptMoptValidFile() {
		String[] args = { "-lm", "-t", "m", file3 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcInvalidOptionFilesValidInvalid() {
		String[] args = { "-z", "testfile", file1 };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	public void testWcInvalidOptionsoptMoptWLoptRoptFilesValidInvalid() {
		String[] args = { "-m", "-wl", "-r", file1, "wrongtest.txt" };
		String expected = INVALID_OPTION;
		Exception exp = new Exception();
		try {
			wcApp.run(args, inputS, outputS);
		} catch (Exception e) {
			exp = e;
		}
		assertEquals(expected, exp.getMessage());
	}

	@Test
	// Integrated Test
	public void testOverAllFromStdin() throws AbstractApplicationException, ShellException {
		outputS = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		String args = "cat " + file3 + " | wc";
		String expected = "27 8 4\n";
		shell.parseAndEvaluate(args, outputS);
		assertEquals(expected, outputS.toString());

	}

	@Test
	public void testOverAllFromFile() throws AbstractApplicationException, ShellException {
		outputS = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		String args = "wc " + file3;
		String expected = "27 8 4 " + file3 + "\n";
		shell.parseAndEvaluate(args, outputS);
		assertEquals(expected, outputS.toString());
	}

}

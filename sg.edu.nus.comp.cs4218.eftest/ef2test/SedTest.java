package ef2test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.SedException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.app.SedApplication;

public class SedTest {

	static SedApplication sedApp;
	static InputStream inputS;
	static OutputStream outputS;
	static ShellImpl shell;
	String testFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sedApp = new SedApplication();
		outputS = new ByteArrayOutputStream();
		shell = new ShellImpl();
	}

/*	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}*/

	@Before
	public void setUp() throws Exception {
		testFile = "sg.edu.nus.comp.cs4218.eftest/testFiles/test.txt";
	}

	@After
	public void tearDown() throws Exception {
		outputS = new ByteArrayOutputStream();
	}

	@Test
	public void testReplaceFirstSubStringInFile() throws SedException {
		outputS = new ByteArrayOutputStream();
		String args = "[123] test " + getFileContent(testFile);
		String expected = "line test\nline test\nline test\nline 4\n";
		String result = sedApp.replaceFirstSubStringInFile(args);
		assertEquals(expected, result);
	}

	@Test
	public void testReplaceAllSubStringInFile() throws SedException {
		outputS = new ByteArrayOutputStream();
		String args = "[l] L " + getFileContent(testFile);
		String expected = "Line 1\nLine 2\nLine 3\nLine 4\n";
		String result = sedApp.replaceAllSubstringsInFile(args);
		assertEquals(expected, result);
	}

	@Test(expected = SedException.class)
	public void testReplaceSubstringWithInvalidRegex() throws SedException {
		outputS = new ByteArrayOutputStream();
		String args = "test";
		String expected = "The command test has invalid Regex";
		String result = sedApp.replaceSubstringWithInvalidRegex(args);
		assertEquals(expected, result);
	}

	@Test(expected = SedException.class)
	public void testReplaceSubstringWithInvalidReplacement() throws SedException {
		outputS = new ByteArrayOutputStream();
		String args = "test";
		String expected = "The command test has invalid Replacement";
		String result = sedApp.replaceSubstringWithInvalidReplacement(args);
		assertEquals(expected, result);

	}

	@Test
	// Integrated Test
	public void testOverAllFromStdin() throws AbstractApplicationException, ShellException {
		outputS = new ByteArrayOutputStream();
		String args = "cat " + testFile + " | sed s/l/L/g";
		String expected = "Line 1\nLine 2\nLine 3\nLine 4\n";
		shell.parseAndEvaluate(args, outputS);
		assertEquals(expected, outputS.toString());

	}

	@Test
	public void testOverAllFromFile() throws AbstractApplicationException, ShellException {
		outputS = new ByteArrayOutputStream();
		String args = "sed s/l/L/g " + testFile;
		String expected = "Line 1\nLine 2\nLine 3\nLine 4\n";
		shell.parseAndEvaluate(args, outputS);
		assertEquals(expected, outputS.toString());
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

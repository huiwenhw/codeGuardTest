package sg.edu.nus.comp.cs4218.test.integrationtest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.*;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.app.*;
import sg.edu.nus.comp.cs4218.impl.cmd.PipeCommand;

public class PipeTest {

	private static final String DIRECTORY_MSG = "This is a directory\n";
	private static final String READ_FILE_ERROR = "Could not read file\n";
	private static final String DIR = "sg.edu.nus.comp.cs4218.eftest/testFiles/";

	private PipeCommand pipeCmd;
	// Not Accepts STDIN
	private CdApplication cdApp;
	private PwdApplication pwdApp;
	private EchoApplication echoApp;
	private DateApplication dateApp;
	private CalApplication calApp;
	// Accepts STDIN
	private CatApplication catApp;
	private HeadApplication headApp;
	private TailApplication tailApp;
	private GrepApplication grepApp;
	private SedApplication sedApp;
	private WcApplication wcApp;
	private SortApplication sortApp;

	private String[] args1, args2;
	private FileInputStream stdin;
	private ByteArrayOutputStream output;
	private String file1, file2;
	private String invalidFile;

	private ByteArrayOutputStream baos;
	PrintStream print;

	@Before
	public void setUp() throws UtilityException, IOException, DirectoryNotFoundException {
		catApp = new CatApplication();
		dateApp = new DateApplication();
		calApp = new CalApplication();
		pwdApp = new PwdApplication();
		cdApp = new CdApplication();
		echoApp = new EchoApplication();
		grepApp = new GrepApplication();
		sedApp = new SedApplication();
		sortApp = new SortApplication();
		wcApp = new WcApplication();
		headApp = new HeadApplication();
		tailApp = new TailApplication();

		// Environment.setCurrentDir(Environment.getCurrentDir());
		stdin = new FileInputStream(DIR + "pipeStdin.txt");
		output = new ByteArrayOutputStream();

		file1 = DIR + "pipeFile1.txt";
		file2 = DIR + "pipeFile2.txt";
		invalidFile = DIR + "abjkcsnakjc.txt";

		baos = new ByteArrayOutputStream();
		print = new PrintStream(baos);
		System.setOut(print);
	}

	@Test
	public void testParsePipeCommandWithSpace() throws ShellException {
		pipeCmd = new PipeCommand("date | echo test");
		Vector<String> expected = new Vector<String>();
		expected.add(" date ");
		expected.add(" echo test ");
		Vector<String> actual = pipeCmd.parsePipe();
		assertTrue(expected.equals(actual));
	}

	@Test(expected = ShellException.class)
	public void testParsePipeCommandWithoutSpace() throws ShellException {
		pipeCmd = new PipeCommand("cal|cd test");
		Vector<String> expected = new Vector<String>();
		expected.add(" cal ");
		expected.add(" cd test ");
		Vector<String> actual = pipeCmd.parsePipe();
		assertTrue(expected.equals(actual));
	}

	@Test
	public void testParsePipeCommandWithQuotes() throws ShellException {
		pipeCmd = new PipeCommand("date | echo \"test | cat\"");
		Vector<String> expected = new Vector<String>();
		expected.add(" date ");
		expected.add(" echo \"test | cat\" ");
		Vector<String> actual = pipeCmd.parsePipe();
		assertTrue(expected.equals(actual));
	}

	@Test
	public void testParsePipeCommandWithMoreThanTwo() throws ShellException {
		pipeCmd = new PipeCommand("date | echo test | cat");
		Vector<String> expected = new Vector<String>();
		expected.add(" date ");
		expected.add(" echo test ");
		expected.add(" cat ");
		Vector<String> actual = pipeCmd.parsePipe();
		assertTrue(expected.equals(actual));
	}

	@Test
	public void testParsePipeCommandWithSpecialChars() throws ShellException {
		pipeCmd = new PipeCommand("*date | echo test | cat");
		Vector<String> expected = new Vector<String>();
		expected.add(" *date ");
		expected.add(" echo test ");
		expected.add(" cat ");
		Vector<String> actual = pipeCmd.parsePipe();
		assertTrue(expected.equals(actual));
	}

	@Test
	public void testNoStdinPipeToNoStdin() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "/";
		args2 = new String[1];
		args2[0] = "hello";
		cdApp.run(args1, stdin, output);
		echoApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("hello\n", baos.toString());
	}

	@Test
	public void testNoStdinPipeToStdinWithValidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[2];
		args2[0] = file2;
		args2[1] = "-n2";
		pwdApp.run(args1, stdin, output);
		headApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("CS4218\nNUS\n", baos.toString());
	}

	@Test(expected = SortException.class)
	public void testNoStdinPipeToStdinWithInalidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "hello";
		args2 = new String[1];
		args2[0] = invalidFile;
		echoApp.run(args1, stdin, output);
		sortApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
	}

	@Test
	public void testNoStdinPipeToStdinWithoutFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[1];
		args2[0] = "";
		assertTrue(args1 != null);
		pwdApp.run(args1, stdin, output);
		catApp.run(args1, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(DIRECTORY_MSG.trim(), baos.toString());
	}

	@Test(expected = TailException.class)
	public void testNoStdinPipeToAppWithException() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "2017";
		args2 = new String[1];
		args2[0] = "-h";
		assertTrue(args1 != null);
		calApp.run(args1, stdin, output);
		tailApp.run(args1, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(DIRECTORY_MSG, baos.toString());
	}

	@Test
	public void testStdinWithoutFilePipeToNoStdin() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[1];
		args2[0] = "/";
		catApp.run(args1, stdin, output);
		cdApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(Environment.currentDirectory + "\n", baos.toString());
	}

	@Test
	public void testStdinWithoutFilePipeToStdinWithoutFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[2];
		args2[0] = "-n";
		args2[1] = "2";
		assertTrue(args1 != null);
		headApp.run(args1, stdin, output);
		tailApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("1\n\n", baos.toString());
	}

	@Test
	public void testStdinWithoutFilePipeToStdinWithValidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[2];
		args2[0] = "s/C/c";
		args2[1] = file2;
		tailApp.run(args1, stdin, output);
		sedApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("cS4218\nNUS\ncOMP\nEDU\nsg\nAPP\ncmd\n", baos.toString());
	}

	@Test
	public void testStdinWithoutFilePipeToStdinWithInalidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "a";
		args2 = new String[1];
		args2[0] = invalidFile;
		grepApp.run(args1, stdin, output);
		wcApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(READ_FILE_ERROR, baos.toString());
	}

	@Test(expected = WcException.class)
	public void testStdinWithoutFilePipeToAppWithException() throws ShellException, AbstractApplicationException {
		args1 = new String[2];
		args1[0] = "s/C/c/g";
		args1[1] = file1;
		args2 = new String[1];
		args2[0] = "-h";
		sedApp.run(args1, stdin, output);
		wcApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(DIRECTORY_MSG, baos.toString());
	}

	@Test
	public void testStdinWithValidFilePipeToNoStdin() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = file1;
		args2 = new String[1];
		args2[0] = "";
		catApp.run(args1, stdin, output);
		dateApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertTrue(baos.toString().contains("2017"));
	}

	@Test
	public void testStdinWithValidFilePipeToStdinWithoutFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = file1;
		args2 = new String[1];
		args2[0] = "-w";
		headApp.run(args1, stdin, output);
		wcApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("6\n", baos.toString());
	}

	@Test
	public void testStdinWithValidFilePipeToStdinWithValidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = file1;
		args2 = new String[1];
		args2[0] = file2;
		tailApp.run(args1, stdin, output);
		wcApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("30 7 7 " + file2 + "\n", baos.toString());
	}

	@Test(expected = HeadException.class)
	public void testStdinWithValidFilePipeToStdinWithInalidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[2];
		args1[0] = "a";
		args1[1] = file1;
		args2 = new String[1];
		args2[0] = invalidFile;
		grepApp.run(args1, stdin, output);
		headApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(READ_FILE_ERROR, baos.toString());
	}

	@Test(expected = CalException.class)
	public void testStdinWithValidFilePipeToAppWithException() throws ShellException, AbstractApplicationException {
		args1 = new String[2];
		args1[0] = "s/C/c/g";
		args1[1] = file1;
		args2 = new String[1];
		args2[0] = "-h";
		sedApp.run(args1, stdin, output);
		calApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(DIRECTORY_MSG, baos.toString());
	}

	@Test
	public void testStdinWithInvalidFilePipeToNoStdin() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = invalidFile;
		args2 = new String[1];
		args2[0] = "hello";
		catApp.run(args1, stdin, output);
		echoApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("hello\n", baos.toString());
	}

	@Test
	public void testStdinWithInvalidFilePipeToStdinWithoutFile() throws ShellException, AbstractApplicationException {
		args1 = new String[2];
		args1[0] = file1;
		args1[1] = "-n3";
		args2 = new String[1];
		args2[0] = "-n1";
		headApp.run(args1, stdin, output);
		tailApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("comp\n", baos.toString());
	}

	@Test(expected = TailException.class)
	public void testStdinWithInvalidFilePipeToStdinWithValidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = invalidFile;
		args2 = new String[1];
		args2[0] = file2;
		tailApp.run(args1, stdin, output);
		catApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("CS4218\nNUS\nCOMP\nEDU\nsg\nAPP\ncmd\n\n", baos.toString());
	}

	@Test(expected = HeadException.class)
	public void testStdinWithInvalidFilePipeToStdinWithInalidFile()
			throws ShellException, AbstractApplicationException {
		args1 = new String[2];
		args1[0] = "a";
		args1[1] = invalidFile;
		args2 = new String[1];
		args2[0] = invalidFile;
		grepApp.run(args1, stdin, output);
		headApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(READ_FILE_ERROR, baos.toString());
	}

	@Test(expected = SedException.class)
	public void testStdinWithInvalidFilePipeToAppWithException() throws ShellException, AbstractApplicationException {
		args1 = new String[2];
		args1[0] = "s/C/c/g";
		args1[1] = invalidFile;
		args2 = new String[1];
		args2[0] = "-h";
		sedApp.run(args1, stdin, output);
		headApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(DIRECTORY_MSG, baos.toString());
	}

	@Test(expected = CalException.class)
	public void testAppWithExceptionPipeToNoStdin() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "-h";
		args2 = new String[1];
		args2[0] = "pwd";
		calApp.run(args1, stdin, output);
		pwdApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(Environment.currentDirectory + "\n", baos.toString());
	}

	@Test(expected = HeadException.class)
	public void testAppWithExceptionPipeToStdinWithoutFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "-h";
		args2 = new String[1];
		args2[0] = "a";
		headApp.run(args1, stdin, output);
		grepApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("comp\n", baos.toString());
	}

	@Test(expected = TailException.class)
	public void testAppWithExceptionPipeToStdinWithValidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "-h";
		args2 = new String[1];
		args2[0] = file2;
		tailApp.run(args1, stdin, output);
		tailApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals("CS4218\nNUS\nCOMP\nEDU\nsg\nAPP\ncmd\n\n", baos.toString());
	}

	@Test
	public void testAppWithExceptionipeToStdinWithInalidFile() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[1];
		args2[0] = invalidFile;
		grepApp.run(args1, stdin, output);
		catApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		System.out.flush();
		assertEquals(invalidFile + ": " + READ_FILE_ERROR.trim(), baos.toString());
	}

	@Test(expected = SedException.class)
	public void testAppWithExceptionPipeToAppWithException() throws ShellException, AbstractApplicationException {
		args1 = new String[1];
		args1[0] = "";
		args2 = new String[1];
		args2[0] = "-h";
		sedApp.run(args1, stdin, output);
		grepApp.run(args2, ShellImpl.outputStreamToInputStream(output), System.out);
		// System.out.flush();
		// assertEquals("This is a directory\n" , baos.toString());
	}
}

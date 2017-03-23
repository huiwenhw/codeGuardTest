package sg.edu.nus.comp.cs4218.test.hackathonTestFilesFolder;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.GrepException;
import sg.edu.nus.comp.cs4218.exception.UtilityException;
import sg.edu.nus.comp.cs4218.impl.app.GrepApplication;

public class HackathonGrepTest {

	private static final String ABCSINGLEFILEOUT = "ABC Hello\nABCDEFGHI";
	private static final String ABCPATTERN = "ABC";

	private GrepApplication grepApp;
	private String[] args;
	private FileInputStream stdin;
	private ByteArrayOutputStream baos;
	private String directory;
	PrintStream print;

	@Before
	public void setUp() throws UtilityException, IOException {
		grepApp = new GrepApplication();

		directory = "sg.edu.nus.comp.cs4218.eftest/testFiles/";

		stdin = new FileInputStream(directory + "greptestdoc.txt");

		baos = new ByteArrayOutputStream();
		print = new PrintStream(baos);
		System.setOut(print);
	}

	/**
	 * The bug is due to using System.lineSeparator() in GrepApplication
	 * implementation, but using \n to compare expected cases in test suites.
	 * This bug applies to ALL grep test files (GrepUnitTest.java,
	 * GrepTest.java, and GrepApplicationTest.java) Only one is reproduced here
	 * to prevent duplicate bug reports. The test below will fail on windows
	 * system as in windows, System.lineSeparator is \r\n instead of \n
	 */

	@Test
	public void grepStdInMatchesFromRun() throws GrepException {
		args = new String[1];
		args[0] = ABCPATTERN;
		grepApp.run(args, stdin, System.out);
		System.out.flush();
		assertEquals(ABCSINGLEFILEOUT + "\n", baos.toString());
	}

}

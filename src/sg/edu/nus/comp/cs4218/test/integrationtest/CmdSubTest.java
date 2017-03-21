package sg.edu.nus.comp.cs4218.test.integrationtest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class CmdSubTest {
	
	static String origPwd;
	OutputStream os;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Environment.currentDirectory = origPwd;
	}

	@Before
	public void setUp() throws Exception {
		origPwd = Environment.currentDirectory; // for assertion and backup
		os = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() throws Exception {
		Environment.currentDirectory = origPwd; // reset current dir
	}
	
	@Test
	public void testCdCmdSubstitution() throws AbstractApplicationException, ShellException {
		// Positive test case: unlike pipe, cmd substitution passes the contents as args, allowing cd to change directory
		ShellImpl shell = new ShellImpl();
		String args = "cd `echo Files`";
		shell.parseAndEvaluate(args, os);
		assertEquals(origPwd + "\\Files", Environment.currentDirectory);
	}
	
	@Test
	public void testSortCmdSubstitution() throws AbstractApplicationException, ShellException {
		ShellImpl shell = new ShellImpl();
		String args = "sort -n `cat cmdSubFile.txt`"; // cmdSubFile contains a filename broken by lines, which allows sort to take in the filename as arg
		String expected = "6X\n6X\n50\n500\nSTRING10\nSTRING5\nSTRING50\n";
		shell.parseAndEvaluate(args, os);
		assertEquals(expected, os.toString());
	}
	
	@Test
	public void testSortDoubleCmdSubstitution() throws AbstractApplicationException, ShellException {
		ShellImpl shell = new ShellImpl();
		String args = "sort `cat cmdSubFile.txt` `head cmdSubFile4.txt`";
		String expected = "001\n01\n1\n10\n2\n210\n50\n500\n6X\n6X\nSTRING10\nSTRING5\nSTRING50\n";
		shell.parseAndEvaluate(args, os);
		assertEquals(expected, os.toString());
	}
	
	@Test
	public void testCmdSubstitution() throws AbstractApplicationException, ShellException {
		// Negative case
		os = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		String args = "wc `cat test.txt`";
		String expected = "wc: line: No such file\n"
				+ "wc: 1line: No such file\n"
				+ "wc: 2line: No such file\n"
				+ "wc: 3line: No such file\n"
				+ "wc: 4: No such file\n";
		shell.parseAndEvaluate(args, os);
		assertEquals(expected, os.toString());
	}
	
	@Test
	public void testDoubleCmdSubstitution() throws AbstractApplicationException, ShellException {
		os = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		String args = "wc `cat cmdSubFile3.txt` `head cmdSubFile2.txt` test.txt";
		String expected = "27 8 4 test.txt\n";
		shell.parseAndEvaluate(args, os);
		assertEquals(expected, os.toString());
	}
	
	@Test
	public void testDoubleCmdSubstitutionFilename() throws AbstractApplicationException, ShellException {
		os = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		String args = "wc `cat cmdSubFile3.txt` `head cmdSubFile.txt`";
		String expected = "38 7 sortAppTestCapitalNumbers.txt\n";
		shell.parseAndEvaluate(args, os);
		assertEquals(expected, os.toString());
	}
	
	@Test
	public void testCmdSubstitutionAndPipe() throws AbstractApplicationException, ShellException {
		os = new ByteArrayOutputStream();
		ShellImpl shell = new ShellImpl();
		String args = "wc `cat test.txt` | sort";
		String expected = "wc: 1line: No such file\n"
				+ "wc: 2line: No such file\n"
				+ "wc: 3line: No such file\n"
				+ "wc: 4: No such file\n"
				+ "wc: line: No such file\n";
		shell.parseAndEvaluate(args, os);
		assertEquals(expected, os.toString());
	}


}

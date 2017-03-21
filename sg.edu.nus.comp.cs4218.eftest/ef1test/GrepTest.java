package ef1test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.app.GrepApplication;

public class GrepTest {
	
	private static final String PATTERN_NOT_FOUND = "Pattern not found\n";
	private static String directory;
	static GrepApplication grepApp;
	static InputStream inputS;
	static OutputStream outputS;
	String file2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		grepApp = new GrepApplication();
		inputS = null;
		outputS = new ByteArrayOutputStream();
	}

/*	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}*/

	@Before
	public void setUp() throws Exception {
		directory = "sg.edu.nus.comp.cs4218.eftest/testFiles/";
		file2 = directory+"test2.txt";
	}

/*	@After
	public void tearDown() throws Exception {
	}*/

	@Test
	public void testGrepBasic() {
		String[] args = {"10", directory+"sortTestBasic.txt"};
		String expected = "10\n100\n102\nnum10\n";
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepNotFound() {
		String[] args = {"string", file2};
		String expected = PATTERN_NOT_FOUND;
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepCase() {
		String[] args = {"line", file2};
		String expected = "line 1\nline 2\nline 3\nline 4\nline 5\nline 6\nline 7\nline 8\nline 9\nline 10\nline 11\nline 12\n";
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepCaseNotFound() {
		String[] args = {"Line", file2};
		String expected = PATTERN_NOT_FOUND;
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepMultipleFiles() {
		String[] args = {"1", directory+"test.txt", file2};
		String expected = "line 1\nline 1\nline 10\nline 11\nline 12\n";
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepMultipleFilesOneNotFound() {
		String[] args = {"12", file2, directory+"test.txt"};
		String expected = "line 12\n";
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepMultipleFilesNotFound() {
		String[] args = {"asdf", file2, directory+"sortTestBasic.txt"};
		String expected = PATTERN_NOT_FOUND;
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepRegexFindMultipleSpaces() {
		String[] args = {"\\s\\s*", directory+"sortTest_Complex.txt"};
		String expected = "one one, one three\n \nremember spaces\nREMEMBER SPACES\n2 lines have spaces\n   \n";
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepInvalidFile() {
		String[] args = {"line", "tes.txt"};
		String expected = cannotRead(args[1]);
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepInvalidFiles() {
		String[] args = {"line", directory+"test.txt", "asdf.txt"};
		String expected = "line 1\nline 2\nline 3\nline 4\n" + cannotRead(args[2]);
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepInvalidValidFiles() {
		String[] args = {"1", "qwerty.txt", file2};
		String expected = cannotRead(args[1]) + "line 1\nline 10\nline 11\nline 12\n";
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testGrepInvalidValidInvalidFiles() {
		String[] args = {"1", "invalid", file2, "asdf.txt"};
		String expected = cannotRead(args[1])+"line 1\nline 10\nline 11\nline 12\n"+cannotRead(args[3]);
		outputS = new ByteArrayOutputStream();
		try {
			grepApp.run(args, inputS, outputS);
			assertEquals(expected, outputS.toString());
		} catch (Exception e) {
			assertEquals(cannotRead(args[1])+ cannotRead(args[3]), e.getMessage());
		}
	}
	
	private String cannotRead(String file){
		return "Cannot read from \"" + file+ "\"\n";
	}

}

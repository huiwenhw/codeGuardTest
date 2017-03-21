package sg.edu.nus.comp.cs4218.test.integrationtest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class IoShellTest {

	private static final String WRONG_EXCEPTION = "Wrong exception thrown";
	private static Shell shell;
	private final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
	File tempTestDir = null;
	String currentDir = "";
	private static final String TEMP_FOLDER = "TempTest";
	
	@Before
	public void setUp() throws Exception {
		currentDir = System.getProperty("user.dir");
		tempTestDir = new File(currentDir + File.separator
				+ TEMP_FOLDER);
		tempTestDir.mkdir();
		shell = new ShellImpl();
	}

	@After
	public void tearDown() throws Exception {
		removeDirectory(tempTestDir);
	}

	void cdTmpFolder() {
		Environment.currentDirectory = currentDir + File.separator
				+ TEMP_FOLDER;
	}

	public static String readFile(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();

			while (line != null) {
				builder.append(line);
				builder.append(System.lineSeparator());
				line = reader.readLine();
			}
			return builder.toString();
		} finally {
			reader.close();
		}
	}
	
	public static void removeDirectory(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				removeDirectory(file);
			}
			file.delete();
		}
		dir.delete();
	}
	
	// Positive tests

	@Test
	public void outputRedirectionToFile() throws AbstractApplicationException,
			ShellException, IOException {
		String testString = "echo hello > TempTest/test.txt";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		shell.parseAndEvaluate(testString, stdout);
		String val = readFile(currentDir + File.separator
				+ TEMP_FOLDER + "/test.txt");
		String expected = "hello" + System.lineSeparator();
		assertEquals(expected, val);
	}
	
	@Test
	public void changeDirAndWriteToFile() throws AbstractApplicationException,
		ShellException, IOException{
		String testString = "cd TempTest ; echo bye > TempTest/1.txt";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		shell.parseAndEvaluate(testString, stdout);
		String val = readFile(currentDir + File.separator
				+ TEMP_FOLDER + "/1.txt");
		String expected = "bye" + System.lineSeparator();
		assertEquals(expected, val);
	}
	
	@Test
	public void writeToFileAndReadFile() throws AbstractApplicationException,
		ShellException, IOException{
		String testString = "echo apple > file.txt ; cat file.txt";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		shell.parseAndEvaluate(testString, stdout);
		String expected = "apple";
		assertEquals(true, (stdout.toString()).contains(expected));
	}
	
	@Test
	public void writeToFileThenOverwrite() throws AbstractApplicationException,
		ShellException, IOException{
		String testString = "echo apple > TempTest/file.txt ; echo banana > TempTest/file.txt";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		shell.parseAndEvaluate(testString, stdout);
		String val = readFile(currentDir + File.separator
				+ TEMP_FOLDER + "/file.txt");
		String expected = "banana" + System.lineSeparator();
		assertEquals(expected, val);
	}
	
	//Negative Tests
	@Test
	public void outputToFileFail(){
		String testString = "'' ; echo hello > TempTest/file.txt";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		try{
			shell.parseAndEvaluate(testString, stdout);
		}catch(ShellException e){
			assertEquals(false,
					(new File(tempTestDir + "/file.txt")).exists());
		}catch(Exception e){
			fail(WRONG_EXCEPTION);
		}
	}
	
	@Test
	public void changeDirFailButStillWrite() throws IOException{
		String testString = "cd tmp ; echo haha > 1.txt ; ''";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		try{
			shell.parseAndEvaluate(testString, stdout);
		}catch(ShellException e){
			String val = readFile(currentDir + File.separator
					+ TEMP_FOLDER + "/1.txt");
			String expected = "haha" + System.lineSeparator();
			assertEquals(expected, val);
		}catch(Exception e){
			fail(WRONG_EXCEPTION);
		}
	}
	
	
	@Test
	public void writeToFileThenReadFileFail() throws AbstractApplicationException,
		ShellException, IOException{
		String testString = "echo new > file.txt ; cat file.txt ; cd abcd";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		try{
			shell.parseAndEvaluate(testString, testOutput);
		}catch(AbstractApplicationException e){
			String expected = "new" + System.lineSeparator();
			assertEquals(expected, testOutput.toString());
		}catch(Exception e){
			System.out.println(e);
			fail(WRONG_EXCEPTION);
		}
	}
	
	@Test
	public void writeToFileThenOverwriteFail() throws IOException{
		String testString = "echo old > file.txt ; cd anything; echo banana > file.txt";
		InputStream stdin = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
		try{
			shell.parseAndEvaluate(testString, stdout);
		}catch(AbstractApplicationException e){
			String val = readFile(currentDir + File.separator
					+ TEMP_FOLDER + "/file.txt");
			String expected = "old" + System.lineSeparator();
			assertEquals(expected, val);
		}catch(Exception e){
			fail(WRONG_EXCEPTION);
		}
	}
	
}
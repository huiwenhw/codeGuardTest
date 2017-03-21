package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;
import sg.edu.nus.comp.cs4218.*;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.CatException;
import sg.edu.nus.comp.cs4218.exception.UtilityException;
import sg.edu.nus.comp.cs4218.impl.app.CatApplication;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CatAppTest {
	
	File tempTestDir = null;
	String currentDir = "";
	private static final String TEMP_FOLDER = "TempTest";
	private final ByteArrayOutputStream stdout = new ByteArrayOutputStream();


	@Rule
	public ExpectedException expectedEx = ExpectedException.none(); 
	
	@Before
	public void setUp() throws Exception {
		currentDir = System.getProperty("user.dir");
		tempTestDir = new File(currentDir + File.separator
				+ TEMP_FOLDER);
		tempTestDir.mkdir();
	}
	
	@After
	public void tearDown() throws Exception{
		tempTestDir.delete();
	}
	
	@Test
	public void testWithNullArg()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		try{
			CatApplication cmdApp = new CatApplication();
			cmdApp.run(null, null, System.out);
		}catch(CatException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testWithNullOutputStreamArg()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		try{
			CatApplication cmdApp = new CatApplication();
			String[] args = new String[2];
			args[0] = TEMP_FOLDER;
			
			cmdApp.run(args, null, null);
		}catch(CatException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNoArgAndNullInputStream()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		try{
			CatApplication cmdApp = new CatApplication();
			String[] args = {};
			
			cmdApp.run(args, null, System.out);
		}catch(CatException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNoArg(){
		CatApplication cmdApp = new CatApplication();
		String[] args ={};
		File tempInput = null;
		File tempOutput = null;
		OutputStream fileOutStream = null;
		
		try{		
			tempInput = File.createTempFile("temp-file-name-input", ".tmp");
			fileOutStream = new FileOutputStream(tempInput);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					fileOutStream));
			writer.write("Test input from input stream reader");
			writer.close();
			fileOutStream.close();
			
			InputStream fileInputStream = new FileInputStream(tempInput);
			
			tempOutput = File.createTempFile("temp-file-name", ".tmp");
			fileOutStream = new FileOutputStream(tempOutput);
			
			cmdApp.run(args, fileInputStream, System.out);
			
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(tempInput)));
			
			assertEquals(
					"Test input from input stream reader",
					buffReader.readLine());
			buffReader.close();
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}catch(AbstractApplicationException e){
			e.printStackTrace();
			fail();
		}finally{
			if(tempInput != null){
				tempInput.delete();
			}
			if(tempOutput != null){
				tempOutput.delete();
			}
		}
	}
	
	@Test
	public void testPositiveWorkflow(){
		CatApplication cmdApp = new CatApplication();
		String[] args = new String[2];
		args[0] = TEMP_FOLDER + File.separator + "test1.txt";
		args[1] = TEMP_FOLDER + File.separator + "test2.txt";
		
		File temp = null;
		
		File file1 = null;
		File file2 = null;
		
		try{
			file1 = createFileWithContents(TEMP_FOLDER + File.separator
					+ "test1.txt", "File 1 test");
			file2 = createFileWithContents(TEMP_FOLDER + File.separator
					+ "test2.txt", "File 2 test");
			
			temp = File.createTempFile("temp-file-name", ".tmp");
			OutputStream fileOutStream = new FileOutputStream(temp);
			
			cmdApp.run(args, null, fileOutStream);
			
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(temp)));
			assertTrue((buffReader.readLine()).contains("File 1 test" + "File 2 test"));
			//assertEquals("File 1 test", buffReader.readLine());
			//assertEquals("File 2 test", buffReader.readLine());
			buffReader.close();				
		}catch(AbstractApplicationException e){
			e.printStackTrace();
			fail();
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			if(temp != null){
				temp.delete();
			}
			if(file1 != null){
				file1.delete();
			}
			if(file2 != null){
				file2.delete();
			}
		}
	}
	
	private File createFileWithContents(String fileName, String fileContents)
		throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName)));
		writer.write(fileContents);
		writer.close();
		
		File test = new File(fileName);
		assertTrue(test.exists());
		return test;
	}
	
	@Test
	public void testNoSuchFile()
		throws AbstractApplicationException{
		CatApplication cmdApp = new CatApplication();
		String[] args = {"text1.txt"};
		cmdApp.run(args, null, stdout);
		assertEquals(true,
				(stdout.toString()).contains("Could not read file"));
	}
	
	@Test
	public void testReadDir()
		throws AbstractApplicationException{
		
		CatApplication cmdApp = new CatApplication();
		
		File tempDir = new File(tempTestDir.getAbsolutePath()
				+ File.separator + "test1");
		tempDir.mkdir();
		String[] args = {TEMP_FOLDER 
				+ File.separator + "test1"};
		
			cmdApp.run(args, null, stdout);
			assertEquals(true, (stdout.toString()).contains("This is a directory"));
			tempDir.delete();
		
	}
}
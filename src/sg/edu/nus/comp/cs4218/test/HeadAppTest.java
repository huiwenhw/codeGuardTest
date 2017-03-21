package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.HeadException;
import sg.edu.nus.comp.cs4218.impl.app.HeadApplication;

public class HeadAppTest {
	
	@Before
	public void setUp() throws Exception{
		Environment.currentDirectory = System.getProperty("user.dir");
	}
	
	@After
	public void tearDown() throws Exception{
		Environment.currentDirectory = System.getProperty("user.dir");
	}
	
	@Test
	public void testNullArgsArr() {
		boolean thrown = false;
		
		HeadApplication cmdApp = new HeadApplication();
		try{
			cmdApp.run(null, null, System.out);
			fail();
		}catch(HeadException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNullOutputStream(){
		boolean thrown = false;
		
		HeadApplication cmdApp = new HeadApplication();
		String[] args = new String[2];
		args[0] = "test1";
		args[1] = "test2";
		
		try{
			cmdApp.run(args, null, null);
			fail();
		}catch(HeadException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNullArgs(){
		boolean thrown1 = false;
		boolean thrown2 = false;
		boolean thrown3 = false;
		
		HeadApplication cmdApp = new HeadApplication();
		
		String[] args = new String[1];
		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown1 = true;
		}
		
		args = new String[2];
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown2 = true;
		}
		
		args = new String[3];
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown3 = true;
		}
		
		assertTrue(thrown1);
		assertTrue(thrown2);
		assertTrue(thrown3);
	}
	
	@Test
	public void testEmptyArgs(){
		boolean thrown1 = false;
		boolean thrown2 = false;
		boolean thrown3 = false;
		
		HeadApplication cmdApp = new HeadApplication();
		
		String[] args = new String[1];
		args[0] = "";
		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown1 = true;
		}
		
		args = new String[2];
		args[0] = "";
		args[1] = "";
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown2 = true;
		}
		
		args = new String[3];
		args[0] = "";
		args[1] = "";
		args[2] = "";
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown3 = true;
		}
		
		assertTrue(thrown1);
		assertTrue(thrown2);
		assertTrue(thrown3);
	}

	@Test
	public void testReadWithInvalidOptions(){
		boolean thrown = false;
		
		HeadApplication cmdApp = new HeadApplication();
		
		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		String[] args = new String[2];
		args[0] = "-N";
		args[1] = "15";
		
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testReadFrmInputStream(){
		HeadApplication cmdApp = new HeadApplication();
		
		File tempInputFile = null;
		String[] args = new String[0];
		
		try{
			tempInputFile = new File("temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = 
					new FileOutputStream(tempInputFile);
			String tenLines = "";
			for(int i = 0; i < 10; i++){
				tenLines += "line" + (i+1)
						+ System.getProperty("line.separator");
			}
			fileOutStream.write(tenLines.getBytes());
			fileOutStream.close();
			
			//<= 10 Lines
			FileInputStream testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, testInputStream, testOutputStream);
			
			assertEquals(tenLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
			
			//> 10 Lines
			fileOutStream = new FileOutputStream(tempInputFile, true);
			fileOutStream.write(("line11" + 
					System.getProperty("line.separator")).getBytes());
			fileOutStream.close();
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			cmdApp.run(args, testInputStream, testOutputStream);
			
			assertEquals(tenLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
		}catch(HeadException e){
			fail();
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			if(tempInputFile != null){
				tempInputFile.delete();
			}
		}
	}
	
	@Test
	public void testReadFromInputStreamZeroLines(){
		HeadApplication cmdApp = new HeadApplication();
		
		File tempInputFile = null;
		String[] args = new String[2];
		args[0] = "-n";
		args[1] = "0";
		
		try{
			tempInputFile = new File(
					"temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = new FileOutputStream(tempInputFile);
			String anyContent = "";
			for(int i = 0; i < 10; i++){
				anyContent += "line" + (i+1)
						+ System.getProperty("line.separator");
			}
			fileOutStream.write(anyContent.getBytes());
			fileOutStream.close();
			
			FileInputStream testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, testInputStream, testOutputStream);
			
			assertEquals("", testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
		}catch(HeadException e){
			fail();
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			if(tempInputFile != null){
				tempInputFile.delete();
			}
		}
	}
	
	@Test
	public void testReadFrmInputStreamNLines(){
		HeadApplication cmdApp = new HeadApplication();
		
		File tempInputFile = null;
		String[] args = new String[2];
		args[0] = "-n";
		args[1] = "15";
		
		try{
			tempInputFile = new File(
					"temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = new FileOutputStream(tempInputFile);
			String lessThanNLines = "";
			for(int i = 0; i < Integer.parseInt(args[1])-1; i++){
				lessThanNLines += "line" + (i+1)
						+ System.getProperty("line.separator");
			}
			fileOutStream.write(lessThanNLines.getBytes());
			fileOutStream.close();
			
			//< N Lines
			FileInputStream testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, testInputStream, testOutputStream);
			
			assertEquals(lessThanNLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
			
			//= N Lines
			String nLines = lessThanNLines + "line"
					+ Integer.parseInt(args[1])
					+ System.getProperty("line.separator");
			fileOutStream = new FileOutputStream(tempInputFile, false);
			fileOutStream.write(nLines.getBytes());
			fileOutStream.close();
			
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			cmdApp.run(args, testInputStream, testOutputStream);
			
			assertEquals(nLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
			
			//> N Lines
			fileOutStream = new FileOutputStream(tempInputFile, true);
			fileOutStream.write(
					("line" + (Integer.parseInt(args[1])+1)
					+ System.getProperty("line.separator"))
					.getBytes());
			fileOutStream.close();
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			cmdApp.run(args, testInputStream, testOutputStream);
			
			assertEquals(nLines, testOutputStream.toString());
			testInputStream.close();
			testOutputStream.reset();
		}catch(HeadException e){
			fail();
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			if(tempInputFile != null){
				tempInputFile.delete();
			}
		}
	}
	
	@Test
	public void testInvalidFile() throws HeadException{
		boolean thrown = false;
		HeadApplication cmdApp = new HeadApplication();
		
		String[] args = new String[1];
		
			args[0] = "temp-input-file-name" + ".tmp";
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			try{
				cmdApp.run(args, null, testOutputStream);
			}catch(HeadException e){
				thrown = true;
			}
			
			assertTrue(thrown);
			//assertEquals(true, (testOutputStream.toString()).contains("randomData"));
	}
	
	@Test
	public void testNegativeNumOfLines()
		throws IOException{
		boolean thrown = false;
		
		HeadApplication cmdApp = new HeadApplication();
		
		File tempInputFile = null;
		String[] args = new String[2];
		FileInputStream testInputStream = null;
		
		try{
			tempInputFile = new File(
					"temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = new FileOutputStream(tempInputFile);
			fileOutStream.write("randomData".getBytes());
			fileOutStream.close();
			
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			args[0] = "-n";
			args[1] = "-1";
			cmdApp.run(args, testInputStream, testOutputStream);
		}catch(HeadException e){
			thrown = true;
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			testInputStream.close();
			if(tempInputFile != null){
				tempInputFile.delete();
			}
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testAlphabetNumLines(){
		boolean thrown = false;
		
		HeadApplication cmdApp = new HeadApplication();
		
		File tempInputFile = null;
		String[] args = new String[2];
		FileInputStream testInputStream = null;
		
		try{
			tempInputFile = new File(
					"temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = new FileOutputStream(tempInputFile);
			fileOutStream.write("randomData".getBytes());
			fileOutStream.close();
			
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			args[0] = "-n";
			args[1] = "-1";
			cmdApp.run(args, testInputStream, testOutputStream);
			
			testInputStream.close();
		}catch(HeadException e){
			thrown = true;
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			if(tempInputFile != null){
				tempInputFile.delete();
			}
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testTooManyArgs(){
		boolean thrown = false;
		
		HeadApplication cmdApp = new HeadApplication();
		
		String[] args = new String[4];
		args[0] = "abc";
		args[1] = "bcd";
		args[2] = "cde";
		args[3] = "def";
		
		try{
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(HeadException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
}
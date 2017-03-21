package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.TailException;
import sg.edu.nus.comp.cs4218.impl.app.TailApplication;

public class TailAppTest {

	@Test
	public void testNullArgsArr() {
		boolean thrown = false;
		
		TailApplication cmdApp = new TailApplication();
		try{
			cmdApp.run(null, null, System.out);
			fail();
		}catch(TailException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}

	@Test
	public void testNullOutputStream(){
		boolean thrown = false;
		
		TailApplication cmdApp = new TailApplication();
		String[] args = new String[2];
		args[0] = "test1";
		args[1] = "test2";
		
		try{
			cmdApp.run(args, null, null);
			fail();
		}catch(TailException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNullArgs(){
		boolean thrown1 = false;
		boolean thrown2 = false;
		boolean thrown3 = false;
		
		TailApplication cmdApp = new TailApplication();
		
		String[] args = new String[1];
		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown1 = true;
		}
		
		args = new String[2];
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown2 = true;
		}
		
		args = new String[3];
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
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
		
		TailApplication cmdApp = new TailApplication();
		
		String[] args = new String[1];
		args[0] = "";
		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown1 = true;
		}
		
		args = new String[2];
		args[0] = "";
		args[1] = "";
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown2 = true;
		}
		
		args = new String[3];
		args[0] = "";
		args[1] = "";
		args[2] = "";
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown3 = true;
		}
		
		assertTrue(thrown1);
		assertTrue(thrown2);
		assertTrue(thrown3);
	}
	
	@Test
	public void testReadWithInvalidOptions(){
		boolean thrown = false;
		
		TailApplication cmdApp = new TailApplication();
		
		ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
		String[] args = new String[2];
		args[0] = "-N";
		args[1] = "15";
		
		try{
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testReadFrmInputStream(){
		TailApplication cmdApp = new TailApplication();
		
		File tempInputFile = null;
		String[] args = new String[0];
		
		try{
			tempInputFile = new File(
					"temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = new FileOutputStream(tempInputFile);
			String tenLines = "";
			for(int i = 0; i < 10; i++){
				tenLines += "line" + (i+1)
						+ System.getProperty("line.separator");
			}
			fileOutStream.write(tenLines.getBytes());
			fileOutStream.close();
			
			//<= 10 lines
			FileInputStream testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, testInputStream, testOutputStream);
			assertEquals(tenLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
			
			//>= 10 lines
			String moreThanTenLines = "";
			for(int i = 0; i < 10; i++){
				moreThanTenLines += "line" + (i+2)
						+ System.getProperty("line.separator");
			}
			fileOutStream = new FileOutputStream(tempInputFile, true);
			fileOutStream.write(("line11" + System.getProperty(
					"line.separator")).getBytes());
			fileOutStream.close();
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			cmdApp.run(args, testInputStream, testOutputStream);
			assertEquals(moreThanTenLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
		}catch(TailException e){
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
	public void testReadFrmInputStreamZeroLines(){
		TailApplication cmdApp = new TailApplication();
		
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
		}catch(TailException e){
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
		TailApplication cmdApp = new TailApplication();
		
		File tempInputFile = null;
		String[] args = new String[2];
		args[0] = "-n";
		args[1] = "15";
		
		try{
			tempInputFile = new File(
					"temp-input-file-name" + ".tmp");
			FileOutputStream fileOutStream = new FileOutputStream(tempInputFile);
			String lessThanNLines = "";
			for(int i = 0; i < (Integer.parseInt(args[1]) -1); i++){
				lessThanNLines += "line" + (i+1)
						+ System.getProperty("line.separator");
			}
			fileOutStream.write(lessThanNLines.getBytes());
			fileOutStream.close();
			
			//< n lines
			FileInputStream testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, testInputStream, testOutputStream);
			assertEquals(lessThanNLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
			
			//= n lines
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
			
			//> n lines
			String moreThanNLines = "";
			for(int i = 0; i < Integer.parseInt(args[1]); i++){
				moreThanNLines += "line" + (i+2)
						+ System.getProperty("line.separator");
			}
			fileOutStream = new FileOutputStream(tempInputFile, true);
			fileOutStream.write(("line" + (Integer.parseInt(args[1])+1)
					+ System.getProperty("line.separator")).getBytes());
			fileOutStream.close();
			testInputStream = new FileInputStream(
					"temp-input-file-name" + ".tmp");
			cmdApp.run(args, testInputStream, testOutputStream);
			assertEquals(moreThanNLines, testOutputStream.toString());
			
			testInputStream.close();
			testOutputStream.reset();
		}catch(TailException e){
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
	public void testInvalidFile() throws TailException{
		boolean thrown = false;
		TailApplication cmdApp = new TailApplication();
		
		String[] args = new String[1];
		
			args[0] = "temp-input-file-name" + ".tmp";
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			try{
				cmdApp.run(args, null, testOutputStream);
			}catch(TailException e){
				thrown = true;
			}
			assertTrue(thrown);
	}
	
	@Test
	public void testNegativeNumLines()
		throws IOException{
		boolean thrown = false;
		
		TailApplication cmdApp = new TailApplication();
		FileInputStream testInputStream = null;
		
		File tempInputFile = null;
		String[] args = new String[2];
		
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
		}catch(TailException e){
			testInputStream.close();
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
	public void testAlphabetNumLines()
		throws IOException{
		boolean thrown = false;
		
		TailApplication cmdApp = new TailApplication();
		FileInputStream testInputStream = null;
		
		File tempInputFile = null;
		String[] args = new String[2];
		
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
			fail();
		}catch(TailException e){
			testInputStream.close();
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
		
		TailApplication cmdApp = new TailApplication();
		
		String[] args = new String[4];
		args[0] = "abc";
		args[1] = "bcd";
		args[2] = "cde";
		args[3] = "def";
		
		try{
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, null, testOutputStream);
			fail();
		}catch(TailException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
}
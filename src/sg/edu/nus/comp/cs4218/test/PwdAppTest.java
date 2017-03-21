package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.DirectoryNotFoundException;
import sg.edu.nus.comp.cs4218.exception.EchoException;
import sg.edu.nus.comp.cs4218.exception.PwdException;
import sg.edu.nus.comp.cs4218.impl.app.PwdApplication;

public class PwdAppTest {

	@Test
	public void testNullOutputStream() 
		throws AbstractApplicationException{
		boolean thrown = false;
		
		try{
			PwdApplication cmdApp = new PwdApplication();
			cmdApp.run(null, null, null);
		}catch(PwdException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}

	@Test
	public void testPositiveWorkflow()
		throws DirectoryNotFoundException{
		
		PwdApplication cmdApp = new PwdApplication();
		File temp = null;
		try{
			temp = File.createTempFile("temp-file-name", ".tmp");
			OutputStream fileOutStream = new FileOutputStream(temp);
			
			String currentDir = Environment.getCurrentDir();
			
			cmdApp.run(null, null, fileOutStream);
			
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(temp)));
			
			assertEquals(buffReader.readLine(), currentDir);
			buffReader.close();
		}catch(PwdException e){
			fail();
		}catch(IOException e){
			e.printStackTrace();
			fail();
		}finally{
			if(temp != null){
				temp.delete();
			}
		}
	}
}

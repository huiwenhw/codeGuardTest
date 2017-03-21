package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.EchoException;
import sg.edu.nus.comp.cs4218.impl.app.EchoApplication;

public class EchoAppTest {

	@Test
	public void testWithNullArg() {
		boolean thrown = false;
		
		EchoApplication cmdApp = new EchoApplication();
		
		try{
			cmdApp.run(null, null, System.out);
			fail();
		}catch(EchoException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}

	@Test
	public void testWithNullOutputStream(){
		boolean thrown = false;
		
		EchoApplication cmdApp = new EchoApplication();
		String[] args = new String[2];
		args[0] = "test1";
		args[1] = "test2";
		
		try{
			cmdApp.run(args, null, null);
			fail();
		}catch(EchoException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testOneArg(){
		EchoApplication cmdApp = new EchoApplication();
		
		File temp = null;
		String[] args = new String[1];
		args[0] = "hello";
		
		try{
			temp = File.createTempFile("temp-file-name", ".tmp");
			OutputStream fileOutStream = new FileOutputStream(temp);
			cmdApp.run(args, null, fileOutStream);
			
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(temp)));
			assertEquals("hello", buffReader.readLine());
			buffReader.close();
		}catch(EchoException e){
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
	
	@Test
	public void testNoArg(){
		EchoApplication cmdApp = new EchoApplication();
		
		File temp = null;
		String[] args = new String[1];
		try{
			temp = File.createTempFile("temp-file-name", ".tmp");
			ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
			cmdApp.run(args, null, testOutputStream);
		}catch(EchoException e){
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








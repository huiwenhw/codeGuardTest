package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.CdException;
import sg.edu.nus.comp.cs4218.exception.DirectoryNotFoundException;
import sg.edu.nus.comp.cs4218.exception.EchoException;
import sg.edu.nus.comp.cs4218.impl.app.CdApplication;

public class CdAppTest {

	File tempTestDir = null;
	String currentDir = "";
	String tempFolder = "TempTest";
	
	@Before
	public void setUp() throws Exception{
		currentDir = System.getProperty("user.dir");
		
		tempTestDir = new File(currentDir
				+ File.separator + tempFolder);
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
		
		CdApplication cmdApp = new CdApplication();
		
		try{
			cmdApp.run(null, null, System.out);
		}catch(CdException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testWithNullOutputStreamArg()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		CdApplication cmdApp = new CdApplication();
		String[] args = new String[2];
		args[0] = tempFolder;
		
		try{
			cmdApp.run(args, null, null);
		}catch(CdException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}

	@Test
	public void testManyArgs()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		CdApplication cmdApp = new CdApplication();
		String[] args = new String[2];
		args[0] = tempFolder;
		args[1] = "dummy";
		
		try{
			cmdApp.run(args, null, System.out);
		}catch(CdException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNoArg()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		CdApplication cmdApp = new CdApplication();
		String[] args = {};
		
		try{
			cmdApp.run(args, null, System.out);
		}catch(CdException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testMissingDir()
		throws AbstractApplicationException{
		boolean thrown = false;
		
		CdApplication cmdApp = new CdApplication();
		
		tempTestDir.delete();
		
		String[] args = new String[1];
		args[0] = tempFolder;
		
		try{
			cmdApp.run(args, null, System.out);
		}catch(CdException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testPositiveWorkflow()
		throws AbstractApplicationException,
		DirectoryNotFoundException,
		IOException{
		CdApplication cmdApp = new CdApplication();
		String[] args = new String[1];
		args[0] = tempFolder;
		
		cmdApp.run(args, null, System.out);
		String dirAfterCd = Environment.getCurrentDir();
		
		assertEquals(currentDir + File.separator
				+ tempFolder, dirAfterCd);
	}
}











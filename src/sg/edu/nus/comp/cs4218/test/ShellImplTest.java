package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.cmd.CallCommand;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class ShellImplTest {
	
	@Test
	public void simpleCmdLine() 
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "echo abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		assertEquals("abc" + "\n", outputStream.toString());
	}
	
	@Test
	public void appNameInQuote() 
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "\"echo\" abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		assertEquals("abc" + "\n", outputStream.toString());
	}
	
	@Test
	public void appNameFrmCmdSubstitute()
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "`echo echo` abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		assertEquals("abc" + "\n", outputStream.toString());
	}
	
	@Test
	public void appNameFrmCmdSubstituteInQuote()
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "\"`echo echo`\" abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		assertEquals("abc" + "\n", outputStream.toString());
	}
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	@Test
	public void seqCommand()
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String cmdLine = "echo abc ; echo xyz";
		InputStream stdin = new ByteArrayInputStream(cmdLine.getBytes(StandardCharsets.UTF_8));
		
		try{
			shell.parseAndEvaluate(cmdLine, outputStream);
		}catch(ShellException e){
			fail();
		}
	
	}
	
	@Test
	public void pipeCommand()
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "echo abc | echo xyz";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		assertEquals("xyz" + "\n", outputStream.toString());
	}
	
	@Test
	public void substituteCommand()
		throws AbstractApplicationException,
		ShellException{
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "echo \"front `echo middle` back\"";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		assertEquals("front middle back" + "\n", outputStream.toString());
	}
	
	@Test
	public void incompleteCommand()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
					
			String cmdLine = "echo \"front `echo middle back\"";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try{
				shell.parseAndEvaluate(cmdLine, outputStream);
			}catch(ShellException e){
				thrown = true;
			}
			assertTrue(thrown);
	}
	
	/*
	@Test
	public void appError()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
		
		try{
			String cmdLine = "cat";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			shell.parseAndEvaluate(cmdLine, outputStream);
		}catch(ShellException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}*/
	
	@Test
	public void ioBeforeAppName()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
		
		String cmdLine = "<a.txt cat";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try{
			shell.parseAndEvaluate(cmdLine, outputStream);
		}catch(ShellException e){
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void emptyCommand()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
				
			String cmdLine = "";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try{
				shell.parseAndEvaluate(cmdLine, outputStream);
			}catch(ShellException e){
				thrown = true;
			}
			assertTrue(thrown);
		
	}
	
	@Test
	public void multipleInputIO()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
		
			String cmdLine = "<a.txt < b.txt";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try{
				shell.parseAndEvaluate(cmdLine, outputStream);
			}catch(ShellException e){
				thrown = true;
			}
			assertTrue(thrown);
	}
	/*
	@Test
	public void multipleOutputIO()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
		
		try{
			String cmdLine = ">a.txt > b.txt";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			shell.parseAndEvaluate(cmdLine, outputStream);
		}catch(ShellException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void invalidInputFile()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
		
		try{			
			String cmdLine = "echo abc < notExist.txt";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			shell.parseAndEvaluate(cmdLine, outputStream);
		}catch(ShellException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void invalidOutputFile()
		throws AbstractApplicationException,
		ShellException{
		boolean thrown = false;
		ShellImpl shell = new ShellImpl();
		
		try{
			String cmdLine = "echo abc <no#/tExist.txt";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			shell.parseAndEvaluate(cmdLine, outputStream);
		}catch(ShellException e){
			thrown = true;
		}
		
		assertTrue(thrown);
	}*/
}
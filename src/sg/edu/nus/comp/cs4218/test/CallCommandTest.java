package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.cmd.CallCommand;

public class CallCommandTest {
	
	@Test
	public void testTooManyOutputRedirect() {
		boolean thrown = false;
		
		String cmdLine = "pwd test >a.txt >b.txt";
		try{
			new CallCommand(cmdLine).evaluate(
					System.in, System.out);
			fail();
		}catch(ShellException e){
			thrown = true;
		}catch(Exception e){
			fail();
		}
		
		assertTrue(thrown);
	}

	@Test
	public void testNoOutputRedirect(){
		boolean thrown = false;
		
		String cmdLine = "pwd test >";
		try{
			new CallCommand(cmdLine).evaluate(
					System.in, System.out);
			fail();
		}catch(ShellException e){
			thrown = true;
		}catch(Exception e){
			fail();
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testInvalidOutputRedirect(){
		boolean thrown = false;
		
		String cmdLine = "pwd test > < a.txt";
		try{
			new CallCommand(cmdLine).evaluate(
					System.in, System.out);
			fail();
		}catch(ShellException e){
			thrown = true;
		}catch(Exception e){
			fail();
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testTooManyInputRedirect(){
		boolean thrown = false;
		
		String cmdLine = "pwd test <a.txt <b.txt";
		try{
			new CallCommand(cmdLine).evaluate(
					System.in, System.out);
			fail();
		}catch(ShellException e){
			thrown = true;
		}catch(Exception e){
			fail();
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testNoInputRedirect(){
		boolean thrown = false;
		
		String cmdLine = "pwd test <";
		try{
			new CallCommand(cmdLine).evaluate(
					System.in, System.out);
			fail();
		}catch(ShellException e){
			thrown = true;
		}catch(Exception e){
			fail();
		}
		
		assertTrue(thrown);
	}
	
	@Test
	public void testInvalidInputRedirect(){
		boolean thrown = false;
		
		String cmdLine = "pwd test < <";
		try{
			new CallCommand(cmdLine).evaluate(
					System.in, System.out);
			fail();
		}catch(ShellException e){
			thrown = true;
		}catch(Exception e){
			fail();
		}
		
		assertTrue(thrown);
	}
	

}

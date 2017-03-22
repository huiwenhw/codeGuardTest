package sg.edu.nus.comp.cs4218.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class HackathonShellTest {
	ShellImpl shell = new ShellImpl();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	/**
	 * The bug is due to not handling the possibility of not having a space
	 * between a semicolon operator and an argument with reference
	 * to project description 7.1.4
	 */
	@Test
	public void testSemicolonWithoutSpaceAfterArgument() throws AbstractApplicationException, ShellException {
		shell.parseAndEvaluate("echo apple; echo banana", outputStream);
		assertEquals("apple\nbanana", outputStream.toString().trim());
		outputStream.reset();
	}
}

package sg.edu.nus.comp.cs4218.test.hackathonTestFilesFolder;

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

	/**
	 * The bug is due to not handling passing of any argument through piping
	 * with reference to project description 7.1.7 where the output of the left argument should
	 * be piped to the right side, multiple pipings bug omitted as it is duplicate of this,
	 * i.e. run shell with "echo apple | echo | echo | echo" should result in apple
	 */
	@Test
	public void testPipingPassesArguments() throws AbstractApplicationException, ShellException {
		shell.parseAndEvaluate("echo banana | echo", outputStream);
		assertEquals("banana", outputStream.toString().trim());
		outputStream.reset();
	}

	/**
	 * The bug is due to not handling globbing of a directory
	 * with reference to project description 7.1.5 where the glob should be expanded to the list
	 * of files to be cat-ted
	 */
	@Test
	public void testGlobFolder() throws AbstractApplicationException, ShellException {
		shell.parseAndEvaluate("cat src/sg/edu/nus/comp/cs4218/test/hackathonTestFilesFolder/*", outputStream);
		assertEquals("Cat me!", outputStream.toString().trim());
		outputStream.reset();
	}

	/**
	 * The bug is due to not being able to use both command sub and piping together
	 * with reference to project description 7.1.8 and 7.1.3 where the command substitution should
	 * first be done and pipe should lead the first output to the second part
	 * Note: This produces a different error from testPipingPassesArguments, it encounters a
	 * ```shell: Invalid syntax encountered.``` instead of ```echo: No argument provided``` from
	 * testPipingPassesArguments
	 */
	@Test
	public void testSubCommandWithPipe() throws AbstractApplicationException, ShellException {
		shell.parseAndEvaluate("echo `echo apple` `echo banana` | echo", outputStream);
		assertEquals("apple banana", outputStream.toString().trim());
		outputStream.reset();
	}
}

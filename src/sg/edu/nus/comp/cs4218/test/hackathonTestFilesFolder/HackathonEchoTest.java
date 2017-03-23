package sg.edu.nus.comp.cs4218.test.hackathonTestFilesFolder;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.UtilityException;
import sg.edu.nus.comp.cs4218.impl.app.EchoApplication;

public class HackathonEchoTest {
	
	private EchoApplication echoApp;
	private ByteArrayOutputStream baos;

	@Before
	public void setUp() throws UtilityException, IOException {
		baos = new ByteArrayOutputStream();
		echoApp = new EchoApplication();
	}

	/**
	 * Bug is due to the inability to accept echo without arguments.
	 * Although it is stated in their assumption that echo should come with arguments,
	 * it is also stated in the project requirements that the args parameter for echo is optional.
	 * Therefore, the assumption is invalid.
	 */
	@Test
	public void testEchoWithoutArgs() {
		String[] args = {};
		try {
			echoApp.run(args, System.in, baos);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		assertEquals(System.lineSeparator(), baos.toString());
	}

}

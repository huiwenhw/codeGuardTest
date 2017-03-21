package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.Grep;
import sg.edu.nus.comp.cs4218.exception.EnvironmentException;
import sg.edu.nus.comp.cs4218.exception.GrepException;

/**
 * The grep command searches for lines containing a match to a specified
 * pattern. The output of the command is the list of the lines. Each line is
 * printed followed by a newline.
 * 
 * <p>
 * <b>Command format:</b> <code>grep PATTERN [FILE]...</code>
 * <dl>
 * <dt>PATTERN</dt>
 * <dd>specifies a regular expression in JAVA format</dd>
 * 
 * <dt>FILE</dt>
 * <dd>the name of the file(d). If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class GrepApplication implements Application, Grep {

	private static final String NEW_LINE = System.getProperty("line.separator");
	private final static String PATTERN_NOT_FOUND = "Pattern not found";

	/**
	 * Runs the grep application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application. Each array element is
	 *            the path to a file. If no files are specified stdin is used.
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @param stdout
	 *            An OutputStream. The output of the command is written to this
	 *            OutputStream.
	 * 
	 * @throws GrepException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws GrepException {

		if (stdout == null) {
			throw new GrepException("Null Pointer Exception");
		}

		boolean useStdin = false;
		if (args == null || args.length == 0) {
			throw new GrepException("No pattern argument found");
		} else if (args.length == 1) {
			if (stdin == null) {
				throw new GrepException("Null Pointer Exception");
			}
			useStdin = true;
		}

		String command = ApplicationUtility.convertArrToStr(args);
		String output = "";

		if (isValidPattern(args[0])) {
			// String[] files = null;
			if (useStdin) {
				/*
				 * try { files = ApplicationUtility.getFilesFrom(stdin); } catch
				 * (UtilityException e) { throw new GrepException(e); } command
				 * = command + " " + ApplicationUtility.convertArrToStr(files);
				 */
				output = grepFromStdin(command, stdin);
			} else {
				output = grepFromMultipleFiles(command);
			}
		} else {
			throw new GrepException("Invalid Pattern");
			/*
			 * if (useStdin) { output = grepInvalidPatternInStdin(args[0]); }
			 * else { output = grepInvalidPatternInFile(args[0]); }
			 */
		}

		printOutput(stdout, output);
	}

	private boolean isValidPattern(String pattern) {
		boolean validPattern;
		try {
			Pattern.compile(pattern);
			validPattern = true;
		} catch (PatternSyntaxException e) {
			validPattern = false;
		}
		return validPattern;
	}

	// write output to stdout
	private void printOutput(OutputStream stdout, String output) throws GrepException {
		try {
			stdout.write(output.getBytes());
			stdout.write(NEW_LINE.getBytes());
		} catch (IOException e) {
			throw new GrepException(e);
		}
	}

	/**
	 * Returns string containing lines which match the specified pattern in
	 * Stdin
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @param stdin
	 * @throws GrepException
	 */
	public String grepFromStdin(String args, InputStream stdin) throws GrepException {
		String[] tokens = args.split(" ");
		Pattern pattern = Pattern.compile(tokens[0]);
		Matcher matcher = pattern.matcher("");
		
		String output = "";

		BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
		String line;
		try {
			while ((line = reader.readLine()) != null && !line.equals("^D")) {
				line = line + NEW_LINE;
				matcher.reset(line);
				if (matcher.find()) {
					/*if (!output.isEmpty()) {
						output = output + NEW_LINE;
					}*/
					output = output + line;
				}
			}
		} catch (IOException e) {
			throw new GrepException(e);
		}

		if (output.isEmpty()) {
			output = PATTERN_NOT_FOUND;
		}
		return output.trim();
	}

	/**
	 * Returns string containing lines which match the specified pattern in the
	 * given file
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws GrepException
	 */
	@SuppressWarnings("resource")
	public String grepFromOneFile(String args) throws GrepException {
		String[] tokens = args.split(" ");

		Pattern pattern = Pattern.compile(tokens[0]);
		Matcher matcher = pattern.matcher("");

		String output = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(tokens[1])));

			String line;
			while ((line = reader.readLine()) != null) {
				matcher.reset(line);
				if (matcher.find()||"\n".equals(tokens[0])) {
					if (!output.isEmpty()||"\n".equals(tokens[0])) {
						output = output + NEW_LINE;
					}
					output = output + line;
				}
			}
		} catch (EnvironmentException | IOException e) {
			throw new GrepException(e);
		}

		if (output.isEmpty()) {
			output = PATTERN_NOT_FOUND;
		}
		return output;
	}

	/**
	 * Returns string containing lines which match the specified pattern in the
	 * given files
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String grepFromMultipleFiles(String args) {
		String[] tokens = args.split(" ");

		String output = "";
		for (int i = 1; i < tokens.length; i++) {
			try {
				String result = grepFromOneFile(tokens[0] + " " + tokens[i]);
				if (!result.equals(PATTERN_NOT_FOUND)) {
					if (!output.isEmpty()) {
						output = output + NEW_LINE;
					}
					output = output + result;
				}
			} catch (GrepException e) {
				if (!output.isEmpty()) {
					output = output + NEW_LINE;
				}
				output = output + "Cannot read from \"" + tokens[i] + "\"";
			}
		}

		if (output.isEmpty()) {
			output = PATTERN_NOT_FOUND;
		}
		return output;
	}

	/**
	 * Returns string when invalid pattern is specified in grep from Stdin
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String grepInvalidPatternInStdin(String args) {
		return "Invalid Pattern";
	}

	/**
	 * Returns string when invalid pattern is specified in grep from file
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String grepInvalidPatternInFile(String args) {
		return "Invalid Pattern";
	}
}

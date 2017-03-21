package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.Sed;
import sg.edu.nus.comp.cs4218.exception.EnvironmentException;
import sg.edu.nus.comp.cs4218.exception.SedException;
import sg.edu.nus.comp.cs4218.exception.UtilityException;

/**
 * The sed command copies input file (or input stream) to stdout performing
 * string replacement. For each line containing a match to a specified pattern
 * (in JAVA format), replaces the matched substring with the specified string.
 * 
 * <p>
 * <b>Command format:</b> <code>sed REPLACEMENT [FILE]</code>
 * <dl>
 * <dt>REPLACEMENT</dt>
 * <dd>specifies replacement rule, as follows:</dd>
 * <dd>- s/regexp/replacement/–replacethefirst(ineachline)substringmatchedby
 * regexp with the string replacement.</dd>
 * <dd>- s/regexp/replacement/g–replaceallthesubstringsmatchedbyregexpwith the
 * string replacement.</dd>
 * <dd>Note that the symbols “/” used to separate regexp and replacement string
 * can be substituted by any other symbols. For example, “s/a/b/” and “s|a|b|”
 * are the same replacement rules. However, this separation symbol should not be
 * used inside the regexp and the replacement string.</dd>
 * 
 * <dt>FILE</dt>
 * <dd>the name of the file. If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class SedApplication implements Application, Sed {

	private static String separator = "/";
	private static String replacementS, patternS;

	BufferedReader reader = null;

	/**
	 * Runs the sed application with the specified arguments.
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
	 * @throws SedException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws SedException {

		if (stdout == null) {
			throw new SedException("Null Pointer Exception");
		}

		PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stdout)));

		if (args == null || args.length == 0) {
			try {
				reader = ApplicationUtility.getReaderFromStdin(stdin);
			} catch (UtilityException e) {
				throw new SedException(e);
			}
		}

		boolean replaceAll = false;
		replaceAll = parseSedReplacement(args[0]);

		if (args.length == 1) {
			if (stdin == null) {
				throw new SedException("Null Pointer Exception");
			}
			reader = new BufferedReader(new InputStreamReader(stdin));
		} else {
			for (int i = 1; i < args.length; i++) {
				if (!args[i].isEmpty()) {
					try {
						reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(args[i])));
					} catch (FileNotFoundException | EnvironmentException e) {
						throw new SedException(e);
					}
					break;
				}
			}
		}

		replacePattern(writer, reader, replaceAll);

	}

	/**
	 * Parses replacement argument of sed application
	 * 
	 * @param replacement
	 *            Array of arguments for the application. Each array element is
	 *            the path to a file. If no files are specified stdin is used.
	 * @return
	 * @returns true if all matches should be replaced
	 * @throws SedException
	 */
	boolean parseSedReplacement(String args) throws SedException {
		boolean replaceAll = false;
		String replacement = args;
		if (!replacement.isEmpty() && replacement.charAt(0) == 's') {
			separator = replacement.substring(1, 2);
			replacement = replacement.substring(2);

			String[] tokens = replacement.split("\\" + separator);

			switch (tokens.length) {
			case 2:
				replaceAll = false;
				patternS = tokens[0];
				replacementS = tokens[1];
				break;
			case 3:
				patternS = tokens[0];
				replacementS = tokens[1];
				if (tokens[2].trim().equals("g")) {
					replaceAll = true;

				} else if (tokens[2].trim().isEmpty()) {
					replaceAll = false;
				} else {
					throw new SedException("Invalid replacement");
				}
				break;
			default:
				throw new SedException("Invalid replacement");
			}
		} else {
			throw new SedException("Replacement should start with s");
		}

		try {
			Pattern.compile(patternS);
		} catch (PatternSyntaxException exception) {
			replaceSubstringWithInvalidRegex(patternS);
		}

		return replaceAll;

	}

	/**
	 * Writes up to a limited number of lines specified from the top of the file
	 * 
	 * @param writer
	 *            : a PrintWriter object to write to
	 * @param reader:
	 *            the reader of the file to be read
	 * @param replaceAll
	 *            : whether all matches should be replaced
	 * @throws SedException
	 */
	void replacePattern(PrintWriter writer, BufferedReader reader, boolean replaceAll) throws SedException {

		String currLine;
		try {
			while ((currLine = reader.readLine()) != null && !currLine.equals("^D")) {

				if (replaceAll) {
					currLine = currLine.replaceAll(patternS, replacementS);
				} else {
					currLine = currLine.replaceFirst(patternS, replacementS);
				}

				writer.write(currLine);
				writer.write(System.getProperty("line.separator"));
			}

			// reader.close();
			writer.flush();

		} catch (IOException e) {
			throw new SedException(e);
		}
	}

	/**
	 * Returns string containing lines with the first matched substring replaced
	 * in file
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws SedException
	 */
	public String replaceFirstSubStringInFile(String args) throws SedException {
		String[] tokens = args.split(" ", 3);

		String pattern = tokens[0];
		String replacement = tokens[1];
		String content = tokens[2];

		String output = "";
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String currLine;
		try {
			while ((currLine = reader.readLine()) != null && !currLine.equals("^D")) {
				currLine = currLine.replaceFirst(pattern, replacement);
				output = output + currLine + System.getProperty("line.separator");
			}

		} catch (IOException e) {
			throw new SedException(e);
		}

		return output;
	}

	/**
	 * Returns string containing lines with all matched substring replaced in
	 * file
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws SedException
	 */
	public String replaceAllSubstringsInFile(String args) throws SedException {
		String[] tokens = args.split(" ", 3);

		String pattern = tokens[0];
		String replacement = tokens[1];
		String content = tokens[2];

		String output = "";
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String currLine;
		try {
			while ((currLine = reader.readLine()) != null && !currLine.equals("^D")) {
				currLine = currLine.replaceAll(pattern, replacement);
				output = output + currLine + System.getProperty("line.separator");
			}

		} catch (IOException e) {
			throw new SedException(e);
		}

		return output;
	}

	/**
	 * Returns string containing lines with first matched substring replaced in
	 * Stdin
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws SedException
	 */
	public String replaceFirstSubStringFromStdin(String args) throws SedException {
		return replaceFirstSubStringInFile(args);
	}

	/**
	 * Returns string containing lines with all matched substring replaced in
	 * Stdin
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws SedException
	 */
	public String replaceAllSubstringsInStdin(String args) throws SedException {
		return replaceAllSubstringsInFile(args);
	}

	/**
	 * Returns string containing lines when invalid replacement string is
	 * provided
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws SedException
	 */
	public String replaceSubstringWithInvalidReplacement(String args) throws SedException {
		throw new SedException("Invalid Replacement");
	}

	/**
	 * Returns string containing lines when invalid regex is provided
	 * 
	 * @param args
	 *            String containing command and arguments
	 * @throws SedException
	 */
	public String replaceSubstringWithInvalidRegex(String args) throws SedException {
		throw new SedException("Invalid Regex");
	}
}

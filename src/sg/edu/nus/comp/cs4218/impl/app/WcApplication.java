package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.Wc;
import sg.edu.nus.comp.cs4218.exception.EnvironmentException;
import sg.edu.nus.comp.cs4218.exception.WcException;

/**
 * The wc command prints the number of bytes, words, and lines in given files
 * (followed by a newline).
 * 
 * <p>
 * <b>Command format:</b> <code>wc [OPTIONS] [FILE]...</code>
 * <dl>
 * <dt>OPTIONS</dt>
 * <dd>-m : Print only the character counts</dd>
 * <dd>-w : Print only the word counts</dd>
 * <dd>-l : Print only the newline counts</dd>
 * 
 * <dt>FILE</dt>
 * <dd>the name of the file(d). If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class WcApplication implements Application, Wc {

	private boolean countWord, countChar, countNewLine;
	private int numWord, numChar, numNewLine;
	private String[][] result;

	/**
	 * Runs the wc application with the specified arguments.
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
	 * @throws WcException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws WcException {

		if (stdout == null) {
			throw new WcException("Null Pointer Exception");
		}

		numWord = numChar = numNewLine = 0;
		countWord = countChar = countNewLine = false;
		result = new String[args.length][4];

		boolean useStdin = true;

		boolean existValidInput = false;
		if (args == null || args.length == 0) {
			if (stdin == null) {
				throw new WcException("Null Pointer Exception");
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				if (args[i].charAt(0) == '-') {
					checkOption(args[i].trim());
					result[i][0] = "Option";
				} else {
					useStdin = false;
					result[i][3] = args[i];
					try {
						numChar += Integer.parseInt(result[i][0] = printCharacterCountInFile(args[i]));
						numWord += Integer.parseInt(result[i][1] = printWordCountInFile(args[i]));
						numNewLine += Integer.parseInt(result[i][2] = printNewlineCountInFile(args[i]));
						existValidInput = true;
					} catch (WcException e) {
						result[i][0] = "Cannot read";
					}
				}
			}
		}

		if (useStdin) {
			String content = getContent(stdin);
			numChar += Integer.parseInt(printCharacterCountInStdin(content));
			numWord += Integer.parseInt(printWordCountInStdin(content));
			numNewLine += Integer.parseInt(printNewlineCountInStdin(content));
			existValidInput = true;
		}

		printOutput(existValidInput, stdout);
	}

	private String getContent(InputStream stdin) throws WcException {
		if (stdin == null) {
			throw new WcException("Null Pointer Exception");
		}

		String output = "";

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
			String currLine;
			while ((currLine = reader.readLine()) != null && !currLine.equals("^D")) {
				output = output + currLine + "\n";
			}
		} catch (IOException e) {
			throw new WcException(e);
		}

		return output.trim();
	}

	private void printOutput(boolean existValidInput, OutputStream stdout) throws WcException {
		String output = "";
		if (existValidInput) {
			if (!countWord && !countChar && !countNewLine) {
				countWord = countChar = countNewLine = true;
			}

			int numFiles = 0;
			for (int i = 0; i < result.length; i++) {
				if (result[i][0].isEmpty() || "Option".equals(result[i][0])) {
					continue;
				} else if ("Cannot read".equals(result[i][0])) {
					output += "Cannot read ";
				} else {
					output += countChar ? (result[i][0] + " ") : "";
					output += countWord ? (result[i][1] + " ") : "";
					output += countNewLine ? (result[i][2] + " ") : "";
					numFiles++;
				}
				output = output + result[i][3] + "\n";
			}

			if (numFiles != 1) {
				output += countChar ? (numChar + " ") : "";
				output += countWord ? (numWord + " ") : "";
				output += countNewLine ? (numNewLine + " ") : "";
				if (numFiles > 1) {
					output += "total";
				}
			}

		} else {
			output = "Could not read file";
		}
		// write output to stdout
		try {
			stdout.write(output.trim().getBytes());
			stdout.write("\n".getBytes());
		} catch (IOException e) {
			throw new WcException(e);
		}
	}

	private void checkOption(String option) throws WcException {
		for (int j = 1; j < option.length(); j++) {
			switch (option.charAt(j)) {
			case 'm':
				countChar = true;
				break;
			case 'w':
				countWord = true;
				break;
			case 'l':
				countNewLine = true;
				break;
			default:
				throw new WcException("Invalid option type");
			}
		}
	}

	/**
	 * Returns string containing the character count in file
	 * 
	 * @param args
	 *            String containing filename
	 * @throws WcException
	 */
	@SuppressWarnings("resource")
	@Override
	public String printCharacterCountInFile(String args) throws WcException {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(args)));
			while (reader.read() > -1) {
				count++;
			}
		} catch (EnvironmentException | IOException e) {
			throw new WcException(e);
		}

		return count + "";
	}

	/**
	 * Returns string containing the word count in file
	 * 
	 * @param args
	 *            String containing filename
	 * @throws WcException
	 */
	@SuppressWarnings("resource")
	@Override
	public String printWordCountInFile(String args) throws WcException {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(args)));
			String currLine;
			while ((currLine = reader.readLine()) != null) {
				/*
				 * Matcher wordM = wordP.matcher(currLine); // matches a word
				 * while (wordM.find()) { count++; }
				 */
				if (!currLine.isEmpty()) {
					count += currLine.split(" ").length;
				}
			}
		} catch (EnvironmentException | IOException e) {
			throw new WcException(e);
		}

		return count + "";
	}

	/**
	 * Returns string containing the newline count in file
	 * 
	 * @param args
	 *            String containing filename
	 * @throws WcException
	 */
	@SuppressWarnings("resource")
	@Override
	public String printNewlineCountInFile(String args) throws WcException {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(args)));
			while (reader.readLine() != null) {
				count++;
			}
		} catch (EnvironmentException | IOException e) {
			throw new WcException(e);
		}

		return count + "";
	}

	/**
	 * Returns string containing all counts in file
	 * 
	 * @param args
	 *            String containing filename
	 * @throws WcException
	 */
	public String printAllCountsInFile(String args) throws WcException {
		return printCharacterCountInFile(args) + " " + printWordCountInFile(args) + " " + printNewlineCountInFile(args);
	}

	/**
	 * Returns string containing the character count in Stdin
	 * 
	 * @param args
	 *            String containing filename
	 */
	@Override
	public String printCharacterCountInStdin(String args) throws WcException {
		int count = 0;
		/*try {
			BufferedReader reader = new BufferedReader(new StringReader(args));
			int curr;
			while ((curr =reader.read()) > -1) {
				count++;
			}
			count
		} catch (IOException e) {
			throw new WcException(e);
		}*/
		count = args.length();
		return count + "";
	}

	/**
	 * Returns string containing the word count in Stdin
	 * 
	 * @param args
	 *            String containing filename
	 */
	@Override
	public String printWordCountInStdin(String args) throws WcException {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new StringReader(args));
			String currLine;
			while ((currLine = reader.readLine()) != null) {
				/*
				 * Matcher wordM = wordP.matcher(currLine); // matches a word
				 * while (wordM.find()) { count++; }
				 */
				if (!currLine.isEmpty()) {
					count += currLine.split(" ").length;
				}
			}
		} catch (IOException e) {
			throw new WcException(e);
		}

		return count + "";
	}

	/**
	 * Returns string containing the newline count in Stdin
	 * 
	 * @param args
	 *            String containing filename
	 * @throws WcException
	 */
	@Override
	public String printNewlineCountInStdin(String args) throws WcException {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new StringReader(args));
			while (reader.readLine() != null) {
				count++;
			}
		} catch (IOException e) {
			throw new WcException(e);
		}
		return count + "";
	}

	/**
	 * Returns string containing all counts in Stdin
	 * 
	 * @param args
	 *            String containing filename
	 * @throws WcException
	 */
	@Override
	public String printAllCountsInStdin(String args) throws WcException {
		return printCharacterCountInStdin(args) + " " + printWordCountInStdin(args) + " "
				+ printNewlineCountInStdin(args);
	}
}

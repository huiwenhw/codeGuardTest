package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.app.CalApplication;
import sg.edu.nus.comp.cs4218.impl.app.CatApplication;
import sg.edu.nus.comp.cs4218.impl.app.CdApplication;
import sg.edu.nus.comp.cs4218.impl.app.DateApplication;
import sg.edu.nus.comp.cs4218.impl.app.EchoApplication;
import sg.edu.nus.comp.cs4218.impl.app.GrepApplication;
import sg.edu.nus.comp.cs4218.impl.app.HeadApplication;
import sg.edu.nus.comp.cs4218.impl.app.PwdApplication;
import sg.edu.nus.comp.cs4218.impl.app.SedApplication;
import sg.edu.nus.comp.cs4218.impl.app.SortApplication;
import sg.edu.nus.comp.cs4218.impl.app.TailApplication;
import sg.edu.nus.comp.cs4218.impl.app.WcApplication;
import sg.edu.nus.comp.cs4218.impl.cmd.GlobCommand;
import sg.edu.nus.comp.cs4218.impl.cmd.CallCommand;

/**
 * A Shell is a command interpreter and forms the backbone of the entire
 * program. Its responsibility is to interpret commands that the user type and
 * to run programs that the user specify in her command lines.
 * 
 * <p>
 * <b>Command format:</b>
 * <code>&lt;Pipe&gt; | &lt;Sequence&gt; | &lt;Call&gt;</code>
 * </p>
 */

public class ShellImpl implements Shell {

	public static final String EXP_INVALID_APP = "Invalid app.";
	public static final String EXP_SYNTAX = "Invalid syntax encountered.";
	public static final String EXP_REDIR_PIPE = "File output redirection and "
			+ "pipe operator cannot be used side by side.";
	public static final String EXP_SAME_REDIR = "Input redirection file same " + "as output redirection file.";
	public static final String EXP_STDOUT = "Error writing to stdout.";
	public static final String EXP_NOT_SUPPORTED = " not supported yet";

	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution.If no back quotes are found, the argsArray from the
	 * input is returned unchanged. If back quotes are found, the back quotes
	 * and its enclosed commands substituted with the output from processing the
	 * commands enclosed in the back quotes.
	 * 
	 * @param argsArray
	 *            String array of the individual commands.
	 * 
	 * @return String array with the back quotes command processed.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 * @throws ShellException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 */
	public static String[] processBQ(String... argsArray) throws AbstractApplicationException, ShellException {
		// echo "this is space `echo "nbsp"`"
		// echo "this is space `echo "nbsp"` and `echo "2nd space"`"
		// Back quoted: any char except \n,`
		// String[] resultArr = new String[argsArray.length];
		// System.arraycopy(argsArray, 0, resultArr, 0, argsArray.length);
		// inefficient
		String patternBQ = "`([^\\n`]*)`";
		Pattern patternBQp = Pattern.compile(patternBQ);

		for (int i = 0; i < argsArray.length; i++) {
			Matcher matcherBQ = patternBQp.matcher(argsArray[i]);
			while (matcherBQ.find()) {// found backquoted
				String bqStr = matcherBQ.group(1);
				// cmdVector.add(bqStr.trim());
				// process back quote
				// System.out.println("backquote " + bqStr);
				OutputStream bqOutputStream = new ByteArrayOutputStream();
				ShellImpl shell = new ShellImpl();
				shell.parseAndEvaluate(bqStr, bqOutputStream);

				ByteArrayOutputStream outByte = (ByteArrayOutputStream) bqOutputStream;
				byte[] byteArray = outByte.toByteArray();
				String bqResult = new String(byteArray).replace("\n", "").replace("\r", "");

				// replace substring of back quote with result
				String replacedStr = argsArray[i].replace("`" + bqStr + "`", bqResult);
				argsArray[i] = replacedStr;
				matcherBQ = patternBQp.matcher(argsArray[i]);
			}
		}
		return argsArray;
	}

	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution.If no back quotes are found, the args from the input
	 * is returned unchanged. If back quotes are found, the back quotes and its
	 * enclosed commands substituted with the output from processing the
	 * commands enclosed in the back quotes.
	 * 
	 * @param arg
	 *            String of the individual command.
	 * 
	 * @return String with the back quotes command processed.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 * @throws ShellException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 */
	public String performCommandSubstitution(String args) {
		// echo "this is space `echo "nbsp"`"
		// echo "this is space `echo "nbsp"` and `echo "2nd space"`"
		// Back quoted: any char except \n,`
		// String[] resultArr = new String[argsArray.length];
		// System.arraycopy(argsArray, 0, resultArr, 0, argsArray.length);
		// inefficient
		String processedStr = args;
		String patternBQ = "`([^\\n`]*)`";
		Pattern patternBQp = Pattern.compile(patternBQ);

		Matcher matcherBQ = patternBQp.matcher(processedStr);
		while (matcherBQ.find()) {// found backquoted
			String bqStr = matcherBQ.group(1);
			// cmdVector.add(bqStr.trim());
			// process back quote
			// System.out.println("backquote " + bqStr);
			OutputStream bqOutputStream = new ByteArrayOutputStream();
			ShellImpl shell = new ShellImpl();
			try {
				shell.parseAndEvaluate(bqStr, bqOutputStream);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

			ByteArrayOutputStream outByte = (ByteArrayOutputStream) bqOutputStream;
			byte[] byteArray = outByte.toByteArray();
			String bqResult = new String(byteArray).replace("\n", "").replace("\r", "");

			// replace substring of back quote with result
			String replacedStr = processedStr.replace("`" + bqStr + "`", bqResult);
			processedStr = replacedStr;
			matcherBQ = patternBQp.matcher(processedStr);
		}
		return processedStr;
	}

	/**
	 * Searches for and processes the commands enclosed by back quotes for
	 * command substitution.If no back quotes are found, the args from the input
	 * is returned unchanged. If back quotes are found, the back quotes and its
	 * enclosed commands substituted with the output from processing the
	 * commands enclosed in the back quotes.
	 * 
	 * @param arg
	 *            String of the individual command.
	 * 
	 * @return String with the back quotes command processed.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 * @throws ShellException
	 *             If an exception happens while processing the content in the
	 *             back quotes.
	 */
	public String performCommandSubstitutionWithException(String args) {
		// echo "this is space `echo "nbsp"`"
		// echo "this is space `echo "nbsp"` and `echo "2nd space"`"
		// Back quoted: any char except \n,`
		// String[] resultArr = new String[argsArray.length];
		// System.arraycopy(argsArray, 0, resultArr, 0, argsArray.length);
		// inefficient
		String processedStr = args;
		String patternBQ = "`([^\\n`]*)`";
		Pattern patternBQp = Pattern.compile(patternBQ);

		Matcher matcherBQ = patternBQp.matcher(processedStr);
		while (matcherBQ.find()) {// found backquoted
			String bqStr = matcherBQ.group(1);
			// cmdVector.add(bqStr.trim());
			// process back quote
			// System.out.println("backquote " + bqStr);
			OutputStream bqOutputStream = new ByteArrayOutputStream();
			ShellImpl shell = new ShellImpl();
			try {
				shell.parseAndEvaluate(bqStr, bqOutputStream);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

			ByteArrayOutputStream outByte = (ByteArrayOutputStream) bqOutputStream;
			byte[] byteArray = outByte.toByteArray();
			String bqResult = new String(byteArray).replace("\n", "").replace("\r", "");

			// replace substring of back quote with result
			String replacedStr = processedStr.replace("`" + bqStr + "`", bqResult);
			processedStr = replacedStr;
			matcherBQ = patternBQp.matcher(processedStr);
		}
		return processedStr;
	}

	/**
	 * Searches for and processes the commands enclosed by double quotes for
	 * command substitution.If no double quotes are found, the arg from the
	 * input is returned unchanged. If valid double quotes are found, the double
	 * quotes will be processed and removed.
	 * 
	 * @param arg
	 *            String of the individual command.
	 * 
	 * @return String with the double quotes command processed.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while processing the content in the
	 *             double quotes.
	 * @throws ShellException
	 *             If an exception happens while processing the content in the
	 *             double quotes.
	 */
	public static String processDQ(String arg) throws AbstractApplicationException, ShellException {
		// echo "this is space `echo "nbsp"`"
		// echo "this is space `echo "nbsp"` and `echo "2nd space"`"
		// Double quoted: any char except \n,"
		// String[] resultArr = new String[argsArray.length];
		// System.arraycopy(argsArray, 0, resultArr, 0, argsArray.length);
		String processedStr = arg;
		String patternDQ = "\"([^\\n\"]*)\"";
		Pattern patternDQp = Pattern.compile(patternDQ);

		Matcher matcherDQ = patternDQp.matcher(processedStr);
		while (matcherDQ.find()) {// found doublequoted
			String dqStr = matcherDQ.group(1);

			// replace substring of double quote with result
			String replacedStr = processedStr.replace("\"" + dqStr + "\"", dqStr);
			processedStr = replacedStr;
			matcherDQ = patternDQp.matcher(processedStr);
		}
		return processedStr;
	}

	/**
	 * Searches for and processes the commands enclosed by single quotes for
	 * command substitution.If no single quotes are found, the arg from the
	 * input is returned unchanged. If valid single quotes are found, the single
	 * quotes will be processed and removed.
	 * 
	 * @param arg
	 *            String of the individual command.
	 * 
	 * @return String with the single quotes command processed.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while processing the content in the
	 *             single quotes.
	 * @throws ShellException
	 *             If an exception happens while processing the content in the
	 *             single quotes.
	 */
	public static String processSQ(String arg) throws AbstractApplicationException, ShellException {
		// echo "this is space `echo "nbsp"`"
		// echo "this is space `echo "nbsp"` and `echo "2nd space"`"
		// Double quoted: any char except \n,"
		// String[] resultArr = new String[argsArray.length];
		// System.arraycopy(argsArray, 0, resultArr, 0, argsArray.length);
		String processedStr = arg;
		String patternSQ = "'([^\\n']*)'";
		Pattern patternSQp = Pattern.compile(patternSQ);

		Matcher matcherSQ = patternSQp.matcher(processedStr);
		while (matcherSQ.find()) {// found singlequoted
			String sqStr = matcherSQ.group(1);

			// replace substring of single quote with result
			String replacedStr = processedStr.replace("'" + sqStr + "'", sqStr);
			processedStr = replacedStr;
			matcherSQ = patternSQp.matcher(processedStr);
		}
		return processedStr;
	}

	/**
	 * Static method to run the application as specified by the application
	 * command keyword and arguments.
	 * 
	 * @param app
	 *            String containing the keyword that specifies what application
	 *            to run.
	 * @param args
	 *            String array containing the arguments to pass to the
	 *            applications for running.
	 * @param inputStream
	 *            InputputStream for the application to get arguments from, if
	 *            needed.
	 * @param outputStream
	 *            OutputStream for the application to print its output to.
	 * 
	 * @throws AbstractApplicationException
	 *             If an exception happens while running any of the
	 *             application(s).
	 * @throws ShellException
	 *             If an unsupported or invalid application command is detected.
	 */
	public static void runApp(String app, String[] argsArray, InputStream inputStream, OutputStream outputStream)
			throws AbstractApplicationException, ShellException {
		Application absApp = null;
		switch (app) {
		case "cd":
			absApp = new CdApplication();
			break;
		case "pwd":
			absApp = new PwdApplication();
			break;
		case "cat":
			absApp = new CatApplication();
			break;
		case "echo":
			absApp = new EchoApplication();
			break;
		case "head":
			absApp = new HeadApplication();
			break;
		case "tail":
			absApp = new TailApplication();
			break;
		case "date":
			absApp = new DateApplication();
			break;
		case "sed":
			absApp = new SedApplication();
			break;
		case "wc":
			absApp = new WcApplication();
			break;
		case "grep":
			absApp = new GrepApplication();
			break;
		case "sort":
			absApp = new SortApplication();
			break;
		case "cal":
			absApp = new CalApplication();
			break;
		default:
			throw new ShellException(app + ": " + EXP_INVALID_APP);
		}
		absApp.run(argsArray, inputStream, outputStream);
	}

	/**
	 * Static method to creates an inputStream based on the file name or file
	 * path.
	 * 
	 * @param inputStreamS
	 *            String of file name or file path
	 * 
	 * @return InputStream of file opened
	 * 
	 * @throws ShellException
	 *             If file is not found.
	 */
	public static InputStream openInputRedir(String inputStreamS) throws ShellException {
		File inputFile = new File(inputStreamS);
		FileInputStream fInputStream = null;
		try {
			fInputStream = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			throw new ShellException(e);
		}
		return fInputStream;
	}

	/**
	 * Static method to creates an outputStream based on the file name or file
	 * path.
	 * 
	 * @param onputStreamS
	 *            String of file name or file path.
	 * 
	 * @return OutputStream of file opened.
	 * 
	 * @throws ShellException
	 *             If file destination cannot be opened or inaccessible.
	 */
	public static OutputStream openOutputRedir(String outputStreamS) throws ShellException {
		File outputFile = new File(outputStreamS);
		FileOutputStream fOutputStream = null;
		try {
			fOutputStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			throw new ShellException(e);
		}
		return fOutputStream;
	}

	/**
	 * Static method to close an inputStream.
	 * 
	 * @param inputStream
	 *            InputStream to be closed.
	 * 
	 * @throws ShellException
	 *             If inputStream cannot be closed successfully.
	 */
	public static void closeInputStream(InputStream inputStream) throws ShellException {
		if (inputStream != System.in) {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new ShellException(e);
			}
		}
	}

	/**
	 * Static method to close an outputStream. If outputStream provided is
	 * System.out, it will be ignored.
	 * 
	 * @param outputStream
	 *            OutputStream to be closed.
	 * 
	 * @throws ShellException
	 *             If outputStream cannot be closed successfully.
	 */
	public static void closeOutputStream(OutputStream outputStream) throws ShellException {
		if (outputStream != System.out) {
			try {
				outputStream.close();
			} catch (IOException e) {
				throw new ShellException(e);
			}
		}
	}

	/**
	 * Static method to write output of an outputStream to another outputStream,
	 * usually System.out.
	 * 
	 * @param outputStream
	 *            Source outputStream to get stream from.
	 * @param stdout
	 *            Destination outputStream to write stream to.
	 * @throws ShellException
	 *             If exception is thrown during writing.
	 */
	public static void writeToStdout(OutputStream outputStream, OutputStream stdout) throws ShellException {
		if (outputStream instanceof FileOutputStream) {
			return;
		}
		try {
			stdout.write(((ByteArrayOutputStream) outputStream).toByteArray());
		} catch (IOException e) {
			throw new ShellException(EXP_STDOUT, e);
		}
	}

	/**
	 * Static method to pipe data from an outputStream to an inputStream, for
	 * the evaluation of the Pipe Commands.
	 * 
	 * @param outputStream
	 *            Source outputStream to get stream from.
	 * 
	 * @return InputStream with data piped from the outputStream.
	 * 
	 * @throws ShellException
	 *             If exception is thrown during piping.
	 */
	public static InputStream outputStreamToInputStream(OutputStream outputStream) throws ShellException {
		return new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
	}

	/**
	 * Main method for the Shell Interpreter program.
	 * 
	 * @param args
	 *            List of strings arguments, unused.
	 */

	public static void main(String... args) {
		ShellImpl shell = new ShellImpl();

		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		String readLine = null;
		String currentDir;

		while (true) {
			try {
				currentDir = Environment.currentDirectory;
				System.out.print(currentDir + ">");
				readLine = bReader.readLine();
				if (readLine == null) {
					break;
				}
				if (("").equals(readLine)) {
					continue;
				}
				OutputStream output = new ByteArrayOutputStream();
				shell.parseAndEvaluate(readLine, output);
				ByteArrayOutputStream outByte = (ByteArrayOutputStream) output;
				outByte.writeTo(System.out);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public String pipeTwoCommands(String args) {
		InputStream input = System.in;
		ByteArrayOutputStream outByte;
		OutputStream output = new ByteArrayOutputStream();
		String[] arr;
		int pipeCount = 0;
		try {
			arr = parse(args);
			for (int i = 0; i < arr.length; i++) {
				if (pipeCount > 1) {
					throw new ShellException("Too many pipes!");
				}
				if (arr[i].equals("|")) {
					pipeCount++;
					continue;
				}
				CallCommand cmd = new CallCommand(arr[i]);
				cmd.parse();
				cmd.evaluate(input, output);
				outByte = (ByteArrayOutputStream) output;
				if (i < arr.length - 1) {
					input = ShellImpl.outputStreamToInputStream(output);
					outByte.reset(); // reset stout when piping
				}
				output = (OutputStream) outByte;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return output.toString();
	}

	public String pipeMultipleCommands(String args) {
		InputStream input = System.in;
		ByteArrayOutputStream outByte;
		OutputStream output = new ByteArrayOutputStream();
		String[] arr;
		try {
			arr = parse(args);
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].equals("|")) {
					continue;
				}
				CallCommand cmd = new CallCommand(arr[i]);
				cmd.parse();
				cmd.evaluate(input, output);
				outByte = (ByteArrayOutputStream) output;
				if (i < arr.length - 1) {
					input = ShellImpl.outputStreamToInputStream(output);
					outByte.reset(); // reset stout when piping
				}
				output = (OutputStream) outByte;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return output.toString();
	}

	public String pipeWithException(String args) {
		try {
			throw new ShellException("Pipe with exception!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * Parses and evaluates user's command line.
	 */
	public void parseAndEvaluate(String cmdLine, OutputStream stdout)  
			throws AbstractApplicationException, ShellException {
		ByteArrayOutputStream outByte;
		InputStream input;
		ShellImpl shell = new ShellImpl();
		try {
			String[] arr = parse(cmdLine);
			for (int i = 0; i < arr.length; i++) {
				input = System.in;
				String temp = "";
				if (arr[i].equals(";")) {
					outByte = (ByteArrayOutputStream) stdout;
					outByte.writeTo(System.out);
					outByte.reset();
					continue;
				}
				arr[i] = shell.performCommandSubstitution(arr[i]);
				if (arr.length > i + 1 && arr[i + 1].equals("|")) {
					int pipeCount = 0;
					int j = i;
					outByte = (ByteArrayOutputStream) stdout;
					outByte.writeTo(System.out);
					while (arr.length > j + 1 && arr[j + 1].equals("|")) {
						pipeCount++;
						j += 2;
					}
					while (arr.length > j && j >= i) {
						temp = temp.concat(" " + arr[i]);
						i++;
					}
					if (pipeCount > 2) {
						temp = shell.pipeMultipleCommands(temp);
					} else if (pipeCount == 1) {
						temp = shell.pipeTwoCommands(temp);
					} else {
						throw new ShellException("Pipe with exception!");
					}
				} else {
					int count = countGlobPaths(arr[i]);
					if (count == 0) {
						arr[i] = globNoPaths(arr[i]);
					} else if (count == 1) {
						arr[i] = globOneFile(arr[i]);
					} else {
						arr[i] = globFilesDirectories(arr[i]);
					}
					CallCommand cmd = new CallCommand(arr[i]);
					cmd.parse();
					cmd.evaluate(input, stdout);
				}
				if (temp == null) {
					throw new ShellException("Pipe with exception!");
				}
				if (temp != "") {
					outByte = (ByteArrayOutputStream) stdout;
					outByte.write(temp.getBytes());
					temp = "";
				}
			}
		} catch (ShellException | AbstractApplicationException e) {
			throw e;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public static int countGlobPaths(String cmdLine) throws ShellException {
		return GlobCommand.countGlob(cmdLine);
	}

	public String globNoPaths(String args) {
		return args;
	}

	public String globOneFile(String args) {
		String pattnUQ = "[\\s]+([^\\s\"'`\\n;]*)[\\s]"; // allowed |, <, >
		String pattnGlob = "[\\s]+([[^\\s\"'`\\n;|<>\\*]*[\\*]+[^\\s\"'`\\n;|<>\\*]*]*)[\\s]";
		String pattnBQinDQ = "[\\s]+\"([^\\n\"`]*`[^\\n]*`[^\\n\"`]*)\"[\\s]";
		String pattnBQ = "[\\s]+(`[^\\n`]*`)[\\s]";
		String pattnSQ = "[\\s]+\'([^\\n']*)\'[\\s]";
		String pattnDQ = "[\\s]+\"([^\\n\"`]*)\"[\\s]";
		String pattnDash = "[\\s]+(-[A-Za-z]*)[\\s]";
		String[] patterns = { pattnGlob, pattnDash, pattnUQ, pattnDQ, pattnSQ, pattnBQ, pattnBQinDQ };
		String substring;
		String str = " " + args + " ";
		int newStartIdx = 0, smallestStartIdx, smallestPattIdx, newEndIdx = 0;
		int count = 0;
		Vector<String> cmdVector = new Vector<String>();
		do {
			substring = str.substring(newEndIdx);
			smallestStartIdx = -1;
			smallestPattIdx = -1;
			for (int i = 0; i < patterns.length; i++) {
				Pattern pattern = Pattern.compile(patterns[i]);
				Matcher matcher = pattern.matcher(substring);
				if (matcher.find() && (matcher.start() < smallestStartIdx || smallestStartIdx == -1)) {
					smallestPattIdx = i;
					smallestStartIdx = matcher.start();
				}
			}
			if (smallestPattIdx != -1) { // if a pattern is found
				Pattern pattern = Pattern.compile(patterns[smallestPattIdx]);
				Matcher matcher = pattern.matcher(str.substring(newEndIdx));
				if (matcher.find()) {
					String matchedStr = matcher.group(1);
					newStartIdx = newEndIdx + matcher.start();
					try {
						if (newStartIdx != newEndIdx) {
							throw new ShellException(ShellImpl.EXP_SYNTAX);
						} // check if there's any invalid token not detected
						newEndIdx = newEndIdx + matcher.end() - 1;
						if (smallestPattIdx == 0) { // if a valid globbing is
													// found
							// process globbing
							count++;
							if (count > 1) {
								throw new ShellException("More than one glob path found!");
							}
							String glob = matchedStr;
							String processedStr = GlobCommand.procsGlob(glob);
							cmdVector.add(processedStr);
						} else {
							cmdVector.add(matchedStr);
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
						return null;
					}
				}
			}
		} while (smallestPattIdx != -1);
		String result = "";
		for (String cmd : cmdVector) {
			result = result.concat(" " + cmd);
		}
		return result;
	}

	public String globFilesDirectories(String args) {
		String result = null;
		try {
			result = parseGlob(args);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return result;
	}

	public static String parseGlob(String cmdLine) throws ShellException {
		String pattnUQ = "[\\s]+([^\\s\"'`\\n;]*)[\\s]"; // allowed |, <, >
		String pattnGlob = "[\\s]+([[^\\s\"'`\\n;|<>\\*]*[\\*]+[^\\s\"'`\\n;|<>\\*]*]*)[\\s]";
		String pattnBQinDQ = "[\\s]+\"([^\\n\"`]*`[^\\n]*`[^\\n\"`]*)\"[\\s]";
		String pattnBQ = "[\\s]+(`[^\\n`]*`)[\\s]";
		String pattnSQ = "[\\s]+\'([^\\n']*)\'[\\s]";
		String pattnDQ = "[\\s]+\"([^\\n\"`]*)\"[\\s]";
		String pattnDash = "[\\s]+(-[A-Za-z]*)[\\s]";
		String[] patterns = { pattnGlob, pattnDash, pattnUQ, pattnDQ, pattnSQ, pattnBQ, pattnBQinDQ };
		String substring;
		String str = " " + cmdLine + " ";
		int newStartIdx = 0, smallestStartIdx, smallestPattIdx, newEndIdx = 0;
		Vector<String> cmdVector = new Vector<String>();
		do {
			substring = str.substring(newEndIdx);
			smallestStartIdx = -1;
			smallestPattIdx = -1;
			for (int i = 0; i < patterns.length; i++) {
				Pattern pattern = Pattern.compile(patterns[i]);
				Matcher matcher = pattern.matcher(substring);
				if (matcher.find() && (matcher.start() < smallestStartIdx || smallestStartIdx == -1)) {
					smallestPattIdx = i;
					smallestStartIdx = matcher.start();
				}
			}
			if (smallestPattIdx != -1) { // if a pattern is found
				Pattern pattern = Pattern.compile(patterns[smallestPattIdx]);
				Matcher matcher = pattern.matcher(str.substring(newEndIdx));
				if (matcher.find()) {
					String matchedStr = matcher.group(1);
					newStartIdx = newEndIdx + matcher.start();
					if (newStartIdx != newEndIdx) {
						throw new ShellException(ShellImpl.EXP_SYNTAX);
					} // check if there's any invalid token not detected
					newEndIdx = newEndIdx + matcher.end() - 1;
					if (smallestPattIdx == 0) { // if a valid globbing is found
						// process globbing
						String glob = matchedStr;
						String processedStr = GlobCommand.procsGlob(glob);
						cmdVector.add(processedStr);
					} else {
						cmdVector.add(matchedStr);
					}
				}
			}
		} while (smallestPattIdx != -1);
		String result = "";
		for (String cmd : cmdVector) {
			result = result.concat(" " + cmd);
		}
		return result;
	}

	public String globWithException(String args) {
		try {
			throw new ShellException("Glob with exception!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	private static String[] parse(String cmdLine) throws ShellException {
		String patternPipe = "[\\s]+([^\\n;|\"'`]*)[\\s]([|])[\\s]";
		String patternCall = "[\\s]+([^\\n;|]*)[\\s]";
		String patternSeq = "[\\s]+([^\\n;|\"'`]*)[\\s]([;])[\\s]";
		String patternSQ = "[\\s]+(['][^\\n']*['])[\\s]";
		String patternDQ = "[\\s]+([\"][^\\n\"]*[\"])[\\s]";
		String patternBQ = "[\\s]+([`][^\\n`]*[`])[\\s]";
		String[] patterns = { patternSQ, patternDQ, patternBQ, patternSeq, patternPipe, patternCall };
		String substring;
		String temp = "";
		String str = " " + cmdLine + " ";
		LinkedList<String> list = new LinkedList<String>();
		int newStartIdx = 0, smallestStartIdx, smallestPattIdx, newEndIdx = 0;
		// check that cmdLine cannot end with ;
		if (cmdLine.length() > 0 && cmdLine.trim().substring(cmdLine.trim().length() - 1).equals(";")) {
			throw new ShellException(ShellImpl.EXP_SYNTAX);
		}
		// check that cmdLine cannot begin with ;
		if (cmdLine.length() > 0 && cmdLine.trim().substring(0, 1).equals(";")) {
			throw new ShellException(ShellImpl.EXP_SYNTAX);
		}
		// check that cmdLine cannot end with |
		if (cmdLine.length() > 0 && cmdLine.trim().substring(cmdLine.trim().length() - 1).equals("|")) {
			throw new ShellException(ShellImpl.EXP_SYNTAX);
		}
		// check that cmdLine cannot begin with |
		if (cmdLine.length() > 0 && cmdLine.trim().substring(0, 1).equals("|")) {
			throw new ShellException(ShellImpl.EXP_SYNTAX);
		}
		do {
			substring = str.substring(newEndIdx);
			smallestStartIdx = -1;
			smallestPattIdx = -1;
			// find the first matched pattern and get starting index
			for (int i = 0; i < patterns.length; i++) {
				Pattern pattern = Pattern.compile(patterns[i]);
				Matcher matcher = pattern.matcher(substring);
				if (matcher.find() && (matcher.start() < smallestStartIdx || smallestStartIdx == -1)) {
					smallestPattIdx = i;
					smallestStartIdx = matcher.start();
				}
			}
			if (smallestPattIdx != -1) { // if a pattern is found
				Pattern pattern = Pattern.compile(patterns[smallestPattIdx]);
				Matcher matcher = pattern.matcher(str.substring(newEndIdx));
				if (matcher.find()) {
					if (matcher.groupCount() > 1) {
						// either pattern ; or |
						temp = temp.concat(" " + matcher.group(1));
						list.add(temp);
						list.add(matcher.group(2));
						temp = "";
					} else {
						temp = temp.concat(" " + matcher.group(1));
					}
					
					newStartIdx = newEndIdx + matcher.start();
					if (newStartIdx != newEndIdx) {
						throw new ShellException(ShellImpl.EXP_SYNTAX);
					}
					newEndIdx = newEndIdx + matcher.end() - 1;
				}
			}
		} while (smallestPattIdx != -1);
		if (!temp.equals("")) {
			list.add(temp);
			temp = "";
		}
		String[] result = new String[list.size()];
		result = list.toArray(result);
		return result;
	}

}

package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.HeadException;
import sg.edu.nus.comp.cs4218.exception.UtilityException;

/**
 * The head command prints first N lines of the file (or input stream). If there
 * are less than N lines, print existing lines without rising an exception.
 * 
 * <p>
 * <b>Command format:</b> <code>head [OPTIONS] [FILE]</code>
 * <dl>
 * <dt>OPTIONS</dt>
 * <dd>“-n 15” means printing 15 lines. Print first 10 lines if not
 * specified.</dd>
 * <dt>FILE</dt>
 * <dd>the name of the file. If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class HeadApplication implements Application {

	static BufferedReader reader;

	/**
	 * Runs the head application with the specified arguments.
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
	 * @throws UtilityException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws HeadException {

		if (stdout == null) {
			throw new HeadException("Null Pointer Exception");
		}

		int numOfLines = 10;

		reader = null;
		PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stdout)));

		if (args == null || args.length == 0) {
			if (stdin == null) {
				throw new HeadException("Null Pointer Exception");
			}
			reader = new BufferedReader(new InputStreamReader(stdin));
			/*
			 * try { reader = ApplicationUtility.getReaderFromStdin(stdin); }
			 * catch (UtilityException e) { throw new HeadException(e); }
			 */
		} else {
			try {
				numOfLines = parseHeadTailArgs(args, stdin);
			} catch (UtilityException e) {
				throw new HeadException(e);
			}
		}

		writeFirstNLines(writer, numOfLines, reader);
	}

	/**
	 * Parses arguments of head/tail application
	 * 
	 * @param args
	 *            Array of arguments for the application. Each array element is
	 *            the path to a file. If no files are specified stdin is used.
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @param reader
	 *            BufferedReader of the file. If no file is specified stdin is
	 *            used.
	 * @param numOfLines
	 *            : number of lines to limit writing to
	 * @return
	 * @returns numOfLines
	 * @throws UtilityException
	 */
	int parseHeadTailArgs(String[] args, InputStream stdin) throws UtilityException {
		int numOfLines = 10;
		int numOfArgs = args.length;
		try {

			int fileIndex = -1;

			for (int i = 0; i < args.length; i++) {
				if(args[i]==null){
					continue;
				}
				String opt = args[i].trim();
				if (opt == null || opt.isEmpty()) {
					continue;
				} else if ("-n".equals(opt)) {
					if (i < numOfArgs - 1) {
						numOfLines = Integer.parseInt(args[i + 1]);
						i++;
					} else {
						throw new UtilityException("Invalid option argument");
					}
				} else if (opt.substring(0, 2).equals("-n")) {
					numOfLines = Integer.parseInt(opt.replaceAll("[^0-9]", ""));
				} else if (fileIndex == -1) {
					fileIndex = i;
					// break;
				}
			}

			if (fileIndex == -1) {
				if (stdin == null) {
					throw new HeadException("Null Pointer Exception");
				}
				reader = new BufferedReader(new InputStreamReader(stdin));
			} else {
				reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(args[fileIndex])));
			}
		} catch (Exception e) {
			throw new UtilityException(e);
		}

		return numOfLines;
	}

	/**
	 * Writes up to a limited number of lines specified from the top of the file
	 * 
	 * @param writer
	 *            : a PrintWriter object to write to
	 * @param numOfLines
	 *            : number of lines to limit writing to
	 * @param reader
	 *            the reader of the file to be read
	 * @throws UtilityException
	 */
	void writeFirstNLines(PrintWriter writer, int numOfLines, BufferedReader reader) throws HeadException {

		if (numOfLines < 0) {
			throw new HeadException("Invalid number of lines");
		}

		try {
			String line = null;
			for (int i = 0; i < numOfLines; i++) {
				if ((line = reader.readLine()) == null || line.equals("^D")) {
					break;
				} else {
					writer.write(line);
				}
				writer.write(System.getProperty("line.separator"));
			}

			// reader.close();
			writer.flush();

		} catch (IOException e) {
			throw new HeadException(e);
		}
	}
}

package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.EnvironmentException;
import sg.edu.nus.comp.cs4218.exception.UtilityException;

/**
 * The cat command concatenates the content of given files and prints on the
 * standard output.
 * 
 * <p>
 * <b>Command format:</b> <code>cat [FILE]...</code>
 * <dl>
 * <dt>FILE</dt>
 * <dd>the name of the file(s). If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public final class ApplicationUtility {

	private ApplicationUtility() {
	}

	/**
	 * Converts stdin into array of string
	 * 
	 * @param args
	 *            Array of arguments for the application. Each array element is
	 *            the path to a file. If no files are specified stdin is used.
	 * @return files array of string
	 * @throws UtilityException
	 *             If the stdin is empty
	 */
	public static String[] getFilesFrom(InputStream stdin) throws UtilityException {
		if (stdin == null) {
			throw new UtilityException("Null Pointer Exception");
		}

		String[] files = null;
		ByteArrayOutputStream buf = new ByteArrayOutputStream();

		int intCount;
		try {
			while (stdin.available() != 0 && (intCount = stdin.read()) != -1) {
				buf.write((byte) intCount);
			}
		} catch (IOException e) {
			throw new UtilityException(e);
		}

		files = buf.toString().replace("\n", " ").trim().split(" ");
		if (files == null || files.length == 0 || (files.length == 1 && files[0].isEmpty())) {
			throw new UtilityException("Cannot parse stdin");
		}

		return files;
	}

	/**
	 * Converts array of string into a string
	 * 
	 * @param arr
	 *            Array of strings.
	 * @return concatenated string
	 */
	public static String convertArrToStr(String... arr) {
		String str = "";

		for (int i = 0; i < arr.length; i++) {
			str = str + " " + arr[i];
		}

		return str.trim();
	}

	/**
	 * Checks if a file is readable.
	 * 
	 * @param filePath
	 *            The path to the file
	 * @return True if the file is readable.
	 * @throws UtilityException
	 *             If the file is not readable
	 */
	static boolean checkIfFileIsReadable(Path filePath) throws UtilityException {

		if (Files.isDirectory(filePath)) {
			throw new UtilityException("This is a directory");
		}
		if (Files.exists(filePath) && Files.isReadable(filePath)) {
			return true;
		} else {
			throw new UtilityException("Could not read file");
		}
	}

	/**
	 * Set file reader based on stdin
	 * 
	 * @param stdin
	 *            An InputStream. The input for the command is read from this
	 *            InputStream if no files are specified.
	 * @throws UtilityException
	 */
	static BufferedReader getReaderFromStdin(InputStream stdin) throws UtilityException {

		if (stdin == null) {
			throw new UtilityException("Null Pointer Exception");
		}

		BufferedReader reader = null;
		String[] file = getFilesFrom(stdin);

		for (int i = 0; i < file.length; i++) {
			if (!file[i].isEmpty()) {
				try {
					reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(file[i])));
					break;
				} catch (FileNotFoundException e) {
					throw new UtilityException(e);
				} catch (EnvironmentException e) {
					throw new UtilityException(e);
				}

			}
		}

		return reader;
	}
}

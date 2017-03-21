package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.app.Sort;
import sg.edu.nus.comp.cs4218.exception.EnvironmentException;
import sg.edu.nus.comp.cs4218.exception.SortException;

/**
 * The sort command orders the lines of the specified file or input and prints
 * the same lines but in sorted order. It compares each line character by
 * character. A special character (e.g., +) comes before numbers. A number comes
 * before capital letters. A capital letter comes before small letters, etc.
 * Within each character class, the characters are sorted according to their
 * ASCII value.
 * 
 * <p>
 * <b>Command format:</b> <code>sort[-n][FILE]</code>
 * <dl>
 * <dt>-n</dt>
 * <dd>If specified, treat the first word of a line as a number.</dd>
 * 
 * <dt>FILE</dt>
 * <dd>the name of the file(d). If no files are specified, use stdin.</dd>
 * </dl>
 * </p>
 */
public class SortApplication implements Application, Sort {

	/**
	 * Runs the sort application with the specified arguments.
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
	 * @throws SortException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws SortException {

		if (stdout == null) {
			throw new SortException("Null Pointer Exception");
		}

		boolean useStdin = false;
		boolean numberComp = false;

		if (args == null || args.length == 0) {
			if (stdin == null) {
				throw new SortException("Null Pointer Exception");
			}
			useStdin = true;
		} else if (args.length == 1) {
			if (args[0].charAt(0) == '-') {
				if ("-n".equals(args[0])) {
					numberComp = true;
					useStdin = true;
				} else {
					throw new SortException("Invalid option");
				}
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				if (args[i].charAt(0) == '-') {
					if ("-n".equals(args[i])) {
						numberComp = true;
						break;
					} else {
						throw new SortException("Invalid option");
					}
				}
			}
		}

		String toSort = getContent(args, stdin, useStdin);

		String output = "";
		if (numberComp) {
			output = sortAllWithFirstWordNumber(toSort);
		} else {
			output = sortAll(toSort);
		}
		printOutput(stdout, output);
	}

	private String getContent(String[] args, InputStream stdin, boolean useStdin) throws SortException {
		BufferedReader reader = null;
		if (useStdin) {
			reader = new BufferedReader(new InputStreamReader(stdin));
			/*
			 * try { reader = ApplicationUtility.getReaderFromStdin(stdin); }
			 * catch (UtilityException e) { throw new SortException(e); }
			 */
		} else {
			boolean existValidFile = false;
			for (int i = 0; i < args.length; i++) {
				if (!args[i].isEmpty()) {
					if (args[i].equals("-n")) {
						continue;
					}
					try {
						reader = new BufferedReader(new FileReader(Environment.checkIfFileIsReadable(args[i])));
						existValidFile = true;
					} catch (FileNotFoundException | EnvironmentException e) {
						// throw new SortException(e);
					}
					break;
				}
			}
			if (!existValidFile) {
				throw new SortException("Cannot read file");
			}
		}

		String content = "";
		String line;
		try {
			while ((line = reader.readLine()) != null && !line.equals("^D")) {
				if (!content.isEmpty()) {
					content = content + System.getProperty("line.separator");
				}
				content = content + line;
			}
		} catch (IOException e) {
			throw new SortException(e);
		}
		return content;
	}

	// write output to stdout
	private void printOutput(OutputStream stdout, String output) throws SortException {
		try {
			stdout.write(output.getBytes());
			if (!output.isEmpty()) {
				stdout.write("\n".getBytes());
			}
		} catch (IOException e) {
			throw new SortException(e);
		}
	}

	/**
	 * Returns a sorted string containing only simple letters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortStringsSimple(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing only capital letters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortStringsCapital(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing only numbers
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortNumbers(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing only special characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSpecialChars(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing simple and capital letters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSimpleCapital(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing simple letters and numbers
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSimpleNumbers(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing simple letters and special characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSimpleSpecialChars(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing capital letters and numbers
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortCapitalNumbers(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing capital letters and special character
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortCapitalSpecialChars(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing numbers and special characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortNumbersSpecialChars(String toSort) {
		return sort(toSort);
	}

	private String sort(String toSort) {
		if (toSort.startsWith("-n ")) {
			return sortAllWithFirstWordNumber(toSort.substring(3));
		} else {
			return sortAll(toSort);
		}
	}

	/**
	 * Returns a sorted string containing simple and capital letters and numbers
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSimpleCapitalNumber(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing simple and capital letters and special
	 * characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSimpleCapitalSpecialChars(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing simple letters, numbers and special
	 * characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortSimpleNumbersSpecialChars(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing capital letters, numbers and special
	 * characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortCapitalNumbersSpecialChars(String toSort) {
		return sort(toSort);
	}

	/**
	 * Returns a sorted string containing simple and capital letters, numbers
	 * and special characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortAll(String toSort) {
		String[] lines = toSort.split("\n");
		boolean swapped = true;
		while (swapped) {
			swapped = false;
			for (int i = 1; i < lines.length; i++) {
				String temp = "";
				if (!lines[i - 1].equals(lines[i]) && latterSmall(lines[i - 1], lines[i])) {
					temp = lines[i - 1];
					lines[i - 1] = lines[i];
					lines[i] = temp;
					swapped = true;
				}
			}
		}
		return convertToStr(lines);
	}

	private boolean latterSmall(String str1, String str2) {
		if (str1.startsWith(str2) && str1.length() >= str2.length()) {
			return true;
		}

		int len = Math.min(str1.length(), str2.length());

		for (int i = 0; i < len; i++) {
			char char1 = str1.charAt(i);
			char char2 = str2.charAt(i);

			if (char2 == char1) {
				continue;
			}

			if (Character.isDigit(char2)) { // c2 digit
				if (Character.isLetter(char1)) { // c1 letter
					return true;
				} else if (Character.isDigit(char1)) {// c1 also digit
					return char2 < char1;
				} else {// c1 special char
					return false;
				}
			} else if (Character.isUpperCase(char2)) { // c2 upper case
				if (Character.isLowerCase(char1)) { // c1 lower case
					return true;
				} else if (Character.isUpperCase(char1)) {// both upper case
					return char2 < char1;
				} else {// c1 special/digit
					return false;
				}
			} else if (Character.isLowerCase(char2)) {// c2 lower case
				if (Character.isLowerCase(char1)) { // both lower case
					return char2 < char1;
				} else { // c1 special/digit/upper case
					return false;
				}
			} else { // c2 special
				if (Character.isLetterOrDigit(char1)) {// c1 non-special
					return true;
				} else {// c1 also special
					return char2 < char1;
				}
			}
		}
		return str2.isEmpty();

	}

	/**
	 * Returns a sorted string containing simple and capital letters, numbers
	 * and special characters
	 * 
	 * @param args
	 *            String containing command and arguments
	 */
	public String sortAllWithFirstWordNumber(String toSort) {
		String sorted = sortAll(toSort);
		String[] lines = sorted.split("\n");

		int start = 0;
		int end = lines.length;

		for (int i = 0; i < lines.length; i++) {
			// sort only when first word is number
			if (lines[i].isEmpty()) {
				continue;
			}
			if (!Character.isLetterOrDigit(lines[i].charAt(0))) {
				start = i + 1;
			}
			if (Character.isLetter(lines[i].charAt(0))) {
				end = i;
				break;
			}
		}

		boolean swapped = true;
		while (swapped) {
			swapped = false;
			boolean firstNumber = true;
			for (int i = start + 1; i < end; i++) {
				String temp = "";
				int num1 = 0, num2 = 0;
				try {
					num1 = Integer.parseInt(lines[i - 1].split(" ")[0]);
				} catch (Exception e) {
					firstNumber = false;
				}

				try {
					num2 = Integer.parseInt(lines[i].split(" ")[0]);
				} catch (Exception e) {
					continue;
				}
				if (!firstNumber || (num1 > num2)) {
					temp = lines[i - 1];
					lines[i - 1] = lines[i];
					lines[i] = temp;
					swapped = true;
				}
			}
		}
		return convertToStr(lines);
	}

	public String convertToStr(String... lines) {
		String output = "";
		for (int i = 0; i < lines.length; i++) {
			if (i > 0) {
				output = output + System.getProperty("line.separator");
			}
			output = output + lines[i];
		}
		return output;
	}
}

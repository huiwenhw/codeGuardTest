package sg.edu.nus.comp.cs4218.impl.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.CatException;
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
public class CatApplication implements Application {

	/**
	 * Runs the cat application with the specified arguments.
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
	 * @throws CatException
	 *             If the file(s) specified do not exist or are unreadable.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws CatException {

		if (stdout == null) {
			throw new CatException("Null Pointer Exception");
		}

		String[] files = args;
		if (args == null || args.length == 0) {
			if (stdin == null) {
				throw new CatException("Null Pointer Exception");
			}
			try {
				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
				while ((line = reader.readLine()) != null && !line.equals("^D")) {
					stdout.write((line + "\n").getBytes());
				}
			} catch (Exception exIO) {
				throw new CatException(exIO);
			}
		}

		int numOfFiles = files.length;
		if (numOfFiles > 0) {
			Path filePath;
			Path currentDir = Paths.get(Environment.currentDirectory);

			for (int i = 0; i < numOfFiles; i++) {
				filePath = currentDir.resolve(files[i]);
				try {
					try {
						ApplicationUtility.checkIfFileIsReadable(filePath);
						byte[] byteFileArray = Files.readAllBytes(filePath);
						stdout.write(byteFileArray);
					} catch (UtilityException uE) {
						if (!uE.getMessage().contains("directory")) {
							stdout.write((files[i] + ": ").getBytes());
						}
						stdout.write(uE.getMessage().getBytes());
					}
					//stdout.write('\n');
				} catch (IOException ioE) {
					throw new CatException(ioE);
				}
			}
		}
	}
}

package sg.edu.nus.comp.cs4218.impl.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.CdException;

public class CdApplication implements Application {
	/**
	 * Runs the cd application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application.
	 * @param stdin
	 *            An InputStream, not used.
	 * @param stdout
	 *            An OutputStream. Elements of args will be output to stdout,
	 *            separated by a space character.
	 * 
	 * @throws CdException
	 *             If an I/O exception occurs.
	 */
	public void run(String[] args, InputStream stdin, OutputStream stdout)
			throws CdException {
		String dirPath = "";
		if (args == null) {
			throw new CdException("Null arguments");
		}
		if (stdout == null) {
			throw new CdException("OutputStream not provided");
		}
		if (args.length == 0) {
			throw new CdException("No argument provided");
		} 
		else {
			for (int i = 0; i < args.length; i++) {
				dirPath = dirPath.concat(args[i].trim());
			}
			if (dirPath.substring(0, 1).equals(File.separator)) {
				dirPath = Environment.currentDirectory.concat(dirPath);
			}
			else {
				dirPath = Environment.currentDirectory + File.separator + dirPath;
			}
			File dir = new File(dirPath);
			if (dir.exists()) {
				try {
					Environment.currentDirectory = dir.toPath().normalize().toString();
					stdout.write((Environment.currentDirectory + "\n").getBytes());
				} catch (IOException e) {
					throw new CdException(e);
				}
			}
			else {
				throw new CdException(dirPath + ": No such file or directory\n");
			}
		}
	}
}

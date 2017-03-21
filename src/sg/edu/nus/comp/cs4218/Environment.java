package sg.edu.nus.comp.cs4218;

import java.io.File;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.exception.DirectoryNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.exception.EnvironmentException;

public final class Environment {
	
	/**
	 * Java VM does not support changing the current working directory. 
	 * For this reason, we use Environment.currentDirectory instead.
	 */
	public static volatile String currentDirectory = System.getProperty("user.dir");
	
	//Get OS information
	public static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
	
	private Environment() {
	};
	
	public static void setCurrentDir(String targetDir)
		throws DirectoryNotFoundException, IOException{
		
		currentDirectory = checkIsDir(targetDir);
	}

	private static String checkIsDir(String targetDir) 
		throws DirectoryNotFoundException, IOException{
		// TODO Auto-generated method stub
		File reqPathAsFile = new File(targetDir);
		
		if(!reqPathAsFile.isAbsolute()){
			reqPathAsFile = new File(Environment.currentDirectory,
					targetDir);
		}
		
		return reqPathAsFile.getCanonicalPath();
	}
	
	public static String getCurrentDir()
		throws DirectoryNotFoundException, IOException{
		checkIsDir(currentDirectory);
		
		return currentDirectory;
	}
	
	/**
	 * Checks if a file is readable.
	 * 
	 * @param fileName
	 *            The path to the file
	 * @return FilePath if the file is readable.
	 * @throws EnvironmentException
	 *             If the file is not readable
	 */
	public static String checkIfFileIsReadable(String fileName) throws EnvironmentException {

		Path filePath = Paths.get(Environment.currentDirectory).resolve(fileName);

		if (Files.isDirectory(filePath)) {
			throw new EnvironmentException("This is a directory");
		}
		if (Files.exists(filePath) && Files.isReadable(filePath)) {
			return filePath.toString();
		} else {
			throw new EnvironmentException("Could not read file");
		}
	}
}














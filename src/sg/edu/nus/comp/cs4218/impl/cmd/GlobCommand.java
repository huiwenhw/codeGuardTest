package sg.edu.nus.comp.cs4218.impl.cmd;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class GlobCommand extends CallCommand {
	
	public GlobCommand(String cmdline) {
		super(cmdline);
	}
	
	/**
	 * Parses the command string and processes all globbing by replacing all
	 * with globbed results
	 * 
	 * @return result String that has been processed.
	 * 
	 * @throws ShellException
	 *             If an error in the syntax of the command is detected while
	 *             parsing.
	 */
	public String parseGlob() throws ShellException {
		String pattnUQ = "[\\s]+([^\\s\"'`\\n;]*)[\\s]"; // allowed |, <, >
		String pattnGlob = "[\\s]+([[^\\s\"'`\\n;|<>\\*]*[\\*]+[^\\s\"'`\\n;|<>\\*]*]*)[\\s]";
		String[] patterns = { pattnGlob, PATTN_DASH, pattnUQ, PATTN_DQ, PATTN_SQ,
				PATTN_BQ, PATTN_BQ_IN_DQ };
		String substring;
		String str = " " + cmdline + " ";
		int newStartIdx = 0, smallestStartIdx, smallestPattIdx, newEndIdx = 0;
		Vector<String> cmdVector = new Vector<String>();
		do {
			substring = str.substring(newEndIdx);
			smallestStartIdx = -1;
			smallestPattIdx = -1;
			if (substring.trim().startsWith("<")
					|| substring.trim().startsWith(">")) {
				//break;
			}
			smallestPattIdx = matchPattern(patterns, substring, smallestStartIdx, smallestPattIdx);
			if (smallestPattIdx != -1) { // if a pattern is found
				Pattern pattern = Pattern.compile(patterns[smallestPattIdx]);
				Matcher matcher = pattern.matcher(str.substring(newEndIdx));
				if (matcher.find()) {
					String matchedStr = matcher.group(1);
					newStartIdx = newEndIdx + matcher.start();
					checkIdx(newStartIdx, newEndIdx);
					newEndIdx = newEndIdx + matcher.end() - 1;
					addToCmdVector(smallestPattIdx, cmdVector, matchedStr);
				}
			}
		} while (smallestPattIdx != -1);
		String result = "";
		for (String cmd:cmdVector) {
			result = result.concat(" " + cmd);
		}
		return result;
	}
	
	/**
	 * Adds matchedStr to cmdVector if it contains no glob syntax.
	 * If it contains glob syntax, processes it and adds to cmdVector.
	 * 
	 * @param smallestPattIdx
	 * @param cmdVector
	 * @param matchedStr
	 * @throws ShellException
	 */
	private void addToCmdVector(int smallestPattIdx, Vector<String> cmdVector, String matchedStr)
			throws ShellException {
		if (smallestPattIdx == 0) { // if a valid globbing is found
			// process globbing
			String glob = matchedStr;
			String processedStr = processGlob(glob);
			cmdVector.add(processedStr);
		}
		else {
			cmdVector.add(matchedStr);
		}
	}
	
	/**
	 * Processes globbing by replacing all globbing with the replaced results
	 * 
	 * @param glob
	 * @return processedStr
	 */
	private String processGlob(String glob) throws ShellException {
		try {
			File dir = new File(Environment.getCurrentDir());
			GlobFinder finder = new GlobFinder(glob);
			Files.walkFileTree(dir.toPath(), finder);
			return finder.returnFound();
		} catch (Exception e) {
			throw new ShellException(e);
		}
	}
	
	public static String procsGlob(String glob) throws ShellException {
		try {
			File dir = new File(Environment.getCurrentDir());
			GlobFinder finder = new GlobFinder(glob);
			Files.walkFileTree(dir.toPath(), finder);
			return finder.returnFound();
		} catch (Exception e) {
			throw new ShellException(e);
		}
	}
	
	public static int countGlob(String glob) throws ShellException {
		try {
			File dir = new File(Environment.getCurrentDir());
			GlobFinder finder = new GlobFinder(glob);
			Files.walkFileTree(dir.toPath(), finder);
			return finder.returnCount();
		} catch (Exception e) {
			throw new ShellException(e);
		}
	}
	
	private static class GlobFinder extends SimpleFileVisitor<Path> {
		
		private final PathMatcher matcher;
	    private String found;
	    private int count;

	    public GlobFinder(String glob) {
	        matcher = FileSystems.getDefault().getPathMatcher("glob:"+glob);
	        found = "";
	        count = 0;
	    }
	    
	    public String returnFound() {
	    	return found;
	    }
	    
	    public int returnCount() {
	    	return count;
	    }
	    
	    private void find(Path file) {
	        Path name = file.getFileName();
	        if (name != null && matcher.matches(name)) {
	            found = found.concat(" " + file.toString());
	            count++;
	        }
	    }
	    
	    @Override
	    public FileVisitResult visitFile(Path file,
	            BasicFileAttributes attrs) {
	        find(file);
	        return CONTINUE;
	    }

	    @Override
	    public FileVisitResult preVisitDirectory(Path dir,
	            BasicFileAttributes attrs) {
	        find(dir);
	        return CONTINUE;
	    }

	    @Override
	    public FileVisitResult visitFileFailed(Path file,
	            IOException err) {
	        System.err.println(err);
	        return CONTINUE;
	    }
	}
}

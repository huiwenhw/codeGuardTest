package sg.edu.nus.comp.cs4218.impl.cmd;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;

public class SeqCommand extends CallCommand {
	
	public SeqCommand(String cmdline) {
		super(cmdline);
	}
	
	/**
	 * Parses the command line and splits it into different commands based on 
	 * semicolon ; operators.
	 * 
	 * 
	 * @return cmdVector Vector of String to store the split command line into.
	 * 
	 * @throws ShellException
	 *             If an error in the syntax of the command is detected while
	 *             parsing.
	 */
	public Vector<String> parseCmdSeq() throws ShellException {
		String patternUQ = "[\\s]+([^\\s\"'`\\n;]*)[\\s]"; // allowed |
		String[] patterns = { PATTN_DASH, patternUQ, PATTN_DQ, PATTN_SQ,
				PATTN_BQ, PATTN_BQ_IN_DQ, PATTN_SC };
		String substring;
		String str = " " + cmdline + " ";
		int newStartIdx = 0, smallestStartIdx, smallestPattIdx, newEndIdx = 0;
		int cmdStartIdx = 0;
		boolean flag = false;
		Vector<String> cmdVector = new Vector<String>();
		String checkStr = ";";
		cmdEndCheck(checkStr);
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
					newStartIdx = newEndIdx + matcher.start();
					checkIdx(newStartIdx, newEndIdx);
					newEndIdx = newEndIdx + matcher.end() - 1;
					flag = true;
				}
				if (flag && smallestPattIdx == 6) { // if a valid ; is found
					//; is not added
					cmdVector.add(str.substring(cmdStartIdx, newEndIdx));
					cmdStartIdx = ++newEndIdx;
					flag = false;
				}
			}
		} while (smallestPattIdx != -1);
		if (cmdStartIdx < str.length() - 1) {
			newEndIdx = str.length();
			// add the remainder of the cmdline
			cmdVector.add(str.substring(cmdStartIdx, newEndIdx));
		}
		return cmdVector;
	}
	
	/**
	 * Checks the end of cmdline for invalid ending character such as |, ;
	 * 
	 * @param checkStr
	 * @throws ShellException
	 */
	private void cmdEndCheck(String checkStr) throws ShellException {
		if (cmdline.length() > 0 && cmdline.substring(cmdline.length() - 1)
				.equals(checkStr)) {
			error = true;
			errorMsg = ShellImpl.EXP_SYNTAX;
			throw new ShellException(errorMsg);
		}
	}
}

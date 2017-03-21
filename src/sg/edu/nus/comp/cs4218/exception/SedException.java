package sg.edu.nus.comp.cs4218.exception;

import java.util.regex.PatternSyntaxException;

public class SedException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public SedException(String message) {
		super("sed: " + message);
	}
	
	public SedException(Exception error) {
		super("sed: " + error.getMessage());
	}
	
	public SedException(PatternSyntaxException exp){
		super("sed: invalid regular expression" );
	}
}
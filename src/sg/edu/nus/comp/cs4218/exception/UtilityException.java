package sg.edu.nus.comp.cs4218.exception;

import java.io.IOException;

public class UtilityException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public UtilityException(String message) {
		super(message);
	}

	public UtilityException(IOException exp) {
		super("IO exception");
	}

	public UtilityException(EnvironmentException exp) {
		super("File not found");
	}

	public UtilityException(Exception exp) {
		super(exp.getMessage());
	}
}
package sg.edu.nus.comp.cs4218.exception;

import java.io.IOException;

public class CatException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public CatException(String message) {
		super("cat: " + message);
	}

	public CatException(IOException ioE) {
		super("cat: " + "Could not write to output stream");
	}

	public CatException(Exception exp) {
		super("cat: " + exp.getMessage());
	}
}
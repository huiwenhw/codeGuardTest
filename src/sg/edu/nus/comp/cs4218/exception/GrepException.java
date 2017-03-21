package sg.edu.nus.comp.cs4218.exception;

public class GrepException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public GrepException(String message) {
		super("grep: " + message);
	}
	
	public GrepException(Exception error) {
		super("grep: " + error.getMessage());
	}
}
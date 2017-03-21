package sg.edu.nus.comp.cs4218.exception;

public class WcException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public WcException(String message) {
		super("wc: " + message);
	}
	
	public WcException(Exception error) {
		super("wc: " + error.getMessage());
	}
}
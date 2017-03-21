package sg.edu.nus.comp.cs4218.exception;

public class DateException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public DateException(String message) {
		super("date: " + message);
	}
	
	public DateException(Exception error) {
		super("date: " + error.getMessage());
	}
}
package sg.edu.nus.comp.cs4218.exception;

public class SortException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public SortException(String message) {
		super("sort: " + message);
	}
	
	public SortException(Exception error) {
		super("sort: " + error.getMessage());
	}
}
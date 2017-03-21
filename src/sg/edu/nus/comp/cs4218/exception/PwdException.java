package sg.edu.nus.comp.cs4218.exception;

public class PwdException extends AbstractApplicationException {
	
	private static final long serialVersionUID = 7499486452467089104L;

	public PwdException(String message) {
		super("pwd: " + message);
	}
	
	public PwdException(Exception error) {
		super("pwd: " + error.getMessage());
	}
}

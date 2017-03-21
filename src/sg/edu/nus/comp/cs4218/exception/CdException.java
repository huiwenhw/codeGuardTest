package sg.edu.nus.comp.cs4218.exception;

public class CdException extends AbstractApplicationException {
	
	private static final long serialVersionUID = 7499486452467089104L;

	public CdException(String message) {
		super("cd: " + message);
	}
	
	public CdException(Exception error) {
		super("cd: " + error.getMessage());
	}
}
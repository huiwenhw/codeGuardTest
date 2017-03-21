package sg.edu.nus.comp.cs4218.exception;

public class EnvironmentException extends AbstractApplicationException {

	private static final long serialVersionUID = 2333796686823942499L;

	public EnvironmentException(String message) {
		super("enviroment: " + message);
	}
}
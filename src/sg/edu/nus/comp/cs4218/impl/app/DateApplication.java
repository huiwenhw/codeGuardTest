package sg.edu.nus.comp.cs4218.impl.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.exception.DateException;

/**
 * The date command Print the current date and time, eg., Wed Dec 30 17:24:43
 * SGT 2009 where the pattern is specified as [week day] [month] [day]
 * [hh:mm:ss] [time zone] [year]
 * <p>
 * <b>Command format:</b> <code>date </code>
 * </p>
 */
public class DateApplication implements Application, sg.edu.nus.comp.cs4218.app.Date {

	private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";

	/**
	 * Runs the date application
	 * 
	 * @param args
	 *            Array of arguments for the application.
	 * @param stdin
	 *            An InputStream, not used.
	 * @param stdout
	 *            An OutputStream. Elements of args will be output to stdout,
	 *            separated by a space character.
	 * 
	 * @throws DateException
	 *             If an I/O exception occurs.
	 */
	public void run(String[] args, InputStream stdin, OutputStream stdout) throws DateException {
		if (stdout == null) {
			throw new DateException("Null Pointer Exception");
		}

		try {
			stdout.write(printCurrentDate("").getBytes());
			stdout.write("\n".getBytes());
		} catch (IOException e) {
			throw new DateException(e);
		}
	}

	@Override
	public String printCurrentDate(String args) {
		Date now = new Date();
		DateFormat dFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
		String dateStr = dFormat.format(now);

		return dateStr;
	}

}

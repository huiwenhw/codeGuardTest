package sg.edu.nus.comp.cs4218.impl.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import sg.edu.nus.comp.cs4218.impl.app.CalendarUtility;

import sg.edu.nus.comp.cs4218.app.Cal;
import sg.edu.nus.comp.cs4218.exception.CalException;

public class CalApplication implements Cal {
	
	private static final Integer CAL_WIDTH = 20;
	private static final String[] MONTHS = { 
			"January", "February", "March", "April", "May", "June", 
			"July", "August", "September", "October", "November", "December" 
	};
	
	private OutputStream outStream;
	
	/**
	 * Runs the cal application with the specified arguments.
	 * 
	 * @param args
	 *            Array of arguments for the application.
	 *            The format of args should be [-m] [[month] year].
	 * @param stdin
	 *            Not required.
	 * @param stdout
	 *            An OutputStream. The output of the command is written to this
	 *            OutputStream.
	 * 
	 * @throws CalException
	 *             If there are any errors.
	 */
	@Override
	public void run(String[] args, InputStream stdin, OutputStream stdout) 
			throws CalException {
		
		if (stdout == null) {
			throw new CalException("Null Pointer Exception");
		}
		
		outStream = stdout;
		String calendarString = null;
		
		if (args == null || args.length <= 0) {
			// run cal
			CalendarUtility calendar = constructCurrentDate();
			calendarString = printMonthCal(calendar, false);
			
		} else if (args.length == 1) {
			if (args[0].equals("-m")) {
				// run cal -m
				CalendarUtility calendar = constructCurrentDate();
				calendarString = printMonthCal(calendar, true);
			} else {
			
				Integer year = parseYear(args[0]);
				checkYearBounds(year);
				
				// year is integer and run cal [year]
				calendarString = printYearCal(year, false);
			}
			
		} else if (args.length == 2) {
			if (args[0].equals("-m")) {
				Integer year = parseYear(args[1]);
				checkYearBounds(year);
				
				// run cal -m year
				calendarString = printYearCal(year, true);
				
			} else {
				Integer month = parseMonth(args[0]);
				checkMonthBounds(month);
				month -= 1;
				Integer year = parseYear(args[1]);
				checkYearBounds(year);
				
				// run cal [month] [year]
				CalendarUtility calendar = constructDate(month, year);
				calendarString = printMonthCal(calendar, false);
			}
			
		} else if (args.length == 3){
			if (!args[0].equals("-m")) {
				throw new CalException("Wrong arguments supplied to Cal application");
			} 
			
			Integer month = parseMonth(args[1]);
			checkMonthBounds(month);
			month -= 1;
			Integer year = parseYear(args[2]);
			checkYearBounds(year);
			
			// run cal -m [month] [year]
			CalendarUtility calendar = constructDate(month, year);
			calendarString = printMonthCal(calendar, true);
			
		} else {
			throw new CalException("Too many arguments are supplied to Cal application");
		}
		
		try {
			outStream.write(calendarString.getBytes());
		} catch (IOException e) {
			throw new CalException("IO Exception");
		}
		
	}
	
	// Interface for testing
	@Override
	public String printCal(String[] args) {
		CalendarUtility calendar = constructCurrentDate();
		return printMonthCal(calendar, false);
	}

	@Override
	public String printCalWithMondayFirst(String[] args) {
		CalendarUtility calendar = constructCurrentDate();
		return printMonthCal(calendar, true);
	}

	@Override
	public String printCalForMonthYear(String[] args) {
		int month = Integer.valueOf(args[0]) - 1;
		int year = Integer.valueOf(args[1]);
		CalendarUtility calendar = constructDate(month, year);
		return printMonthCal(calendar, false);	
	}

	@Override
	public String printCalForYear(String[] args) {
		int year = Integer.valueOf(args[0]);
		return printYearCal(year, false);
	}

	@Override
	public String printCalForMonthYearMondayFirst(String[] args) {
		int month = Integer.valueOf(args[0]) - 1;
		int year = Integer.valueOf(args[1]);
		CalendarUtility calendar = constructDate(month, year);
		return printMonthCal(calendar, true);
	}

	@Override
	public String printCalForYearMondayFirst(String[] args) {
		int year = Integer.valueOf(args[0]);
		return printYearCal(year, true);
	}
	
	/**
	 * Construct current calendar
	 * 
	 * @return current calendar
	 */
	private CalendarUtility constructCurrentDate() {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		return new CalendarUtility(day, month, year);
	}
	
	/**
	 * Construct calendar based on given month and year
	 * 
	 * @param month
	 * @param year
	 * @return the calendar based on month and year
	 */
	private CalendarUtility constructDate(int month, int year) {
		return new CalendarUtility(1, month, year);
	}
	
	/**
	 * Gets the result of printing one month calendar
	 * 
	 * @param calendar The calendar object to be processed
	 * @param isMonday The boolean to specify whether the starting day is Sunday or Monday. True for Monday, False for Sunday.
	 * @return The result of printing a monthly calendar
	 */
	private String printMonthCal(CalendarUtility calendar, boolean isMonday) {
		StringBuilder sb = new StringBuilder();
		String header = getHeader(calendar.getMonth(), 
				calendar.getYear(), false);
		String monthHeader = getMonthHeader(isMonday);
		String monthDates = getMonthDatesStartingAt(getFirstDayOfMonth(calendar, isMonday), 
				calendar.getNumberOfDaysInAMonth());
		
		sb.append(header).append(System.lineSeparator())
		  .append(monthHeader).append(System.lineSeparator())
		  .append(monthDates);
		
		return sb.toString();	
	}
	
	/**
	 * Gets the result of printing one-year calendar
	 * 
	 * @param year The year to be processed
	 * @param isMonday The boolean to specify whether the starting day is Sunday or Monday. True for Monday, False for Sunday.
	 * @return The result of printing a yearly calendar
	 */
	private String printYearCal(int year, boolean isMonday) {
		StringBuilder sb = new StringBuilder();
		List<CalendarUtility> calList = new ArrayList<CalendarUtility>();
		
		for (int i = 0; i < 12; i++) {
			CalendarUtility calendar = constructDate(i, year);
			calList.add(calendar);
		}
		
		for (int i = 0; i < 4; i++) {
			CalendarUtility[] calendars = new CalendarUtility[3];
			int[] offsets = new int[3]; 
			int[] maxDates = new int[3];
			int[] lastValues = {1, 1, 1};
			
			for (int j = 0; j < 3; j++) {
				CalendarUtility cal = calList.get(i * 3 + j);
				calendars[j] = cal;
				offsets[j] = getFirstDayOfMonth(cal, isMonday);
				maxDates[j] = cal.getNumberOfDaysInAMonth();
			}
			
			sb.append(getThreeHeader(calendars[0], calendars[1], calendars[2]))
			  .append(System.lineSeparator())
			  .append(getThreeMonthHeader(isMonday))
			  .append(System.lineSeparator())
			  .append(getFirstRowOfThreeCal(offsets, lastValues))
			  .append(System.lineSeparator())
			  .append(getMonthDatesOfThreeCal(lastValues, maxDates));
		}
		
		return sb.toString();
	}
	
	/**
	 * Gets the header of a given month and year, centralised.
	 * For example, January 2016.
	 * 
	 * @param month
	 * @param year
	 * @param hasTrailingSpace The option to include trailing spaces.
	 * @return The centralised header (based on the width of calendar per month - 20 characters)
	 */
	private String getHeader(int month, int year, boolean hasTrailingSpace) {
		String header = MONTHS[month] + " " + year;
		int leadingSpace = (CAL_WIDTH - header.length()) / 2;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < leadingSpace; i++) {
			sb.append(" ");
		}
		sb.append(header);
		
		if (hasTrailingSpace) {
			int trailingSpace = CAL_WIDTH - header.length() -  leadingSpace;
			for (int i = 0; i < trailingSpace; i++) {
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Gets the day title
	 * 
	 * @param isMonday The option of starting day. True for Monday, False for Sunday.
	 * @return The day title
	 */
	private String getMonthHeader(boolean isMonday) {
		if (isMonday) {
			return "Mo Tu We Th Fr Sa Su";
		}
		
		return "Su Mo Tu We Th Fr Sa";
	}
	
	/**
	 * Gets the monthly dates, positioned correctly based on the calendar
	 * 
	 * @param dayOfWeek starting day of week used as the offset
	 * @param maxDate maximum number os days in a month
	 * 
	 * @return The monthly dates
	 */
	private String getMonthDatesStartingAt(int dayOfWeek, int maxDate) {
		StringBuilder sb = new StringBuilder();
		int offset = dayOfWeek;
		int numRows = 1;
		
		for (int i = 0; i < offset; i++) {
			sb.append("   ");
		}
		
		for (int i = 1; i <= maxDate; i++) {
			if (i < 10) {
				sb.append(" " + i);
			} else {
				sb.append(i);
			}
			
			if (i == maxDate) {
				if (numRows <= 5) {
					sb.append(System.lineSeparator());
				}
				break;
			}
			
			if ((i + offset) % 7 == 0) {
				sb.append(System.lineSeparator());
				numRows++;
			} else {
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Get the first day of the month. 
	 * Sun - 1
	 * Mon - 2, etc.
	 * 
	 * @param calendar
	 * @param isMonday The option for the starting day. True for Monday. False for Sunday. 
	 * @return
	 */
	private Integer getFirstDayOfMonth(CalendarUtility calendar, boolean isMonday) {
		int day = calendar.getDayOfFirstDayInMonth() - 1;
		if (isMonday) {
			day -= 1;
			if (day < 0) {
				day += 7;
			}
		}
		
		return day;
	}
	
	/**
	 * Gets the header of 3 calendars specified, side by side.
	 * 
	 * @param cal1 Calendar to be processed
	 * @param cal2 Calendar to be processed
	 * @param cal3 Calendar to be processed
	 * @return header of 3 calendars side by side.
	 */
	private String getThreeHeader(CalendarUtility cal1, CalendarUtility cal2, CalendarUtility cal3) {
		StringBuilder sb = new StringBuilder();
		sb.append(getHeader(cal1.getMonth(), cal1.getYear(), true));
		sb.append("  ");
		sb.append(getHeader(cal2.getMonth(), cal2.getYear(), true));
		sb.append("  ");
		sb.append(getHeader(cal3.getMonth(), cal3.getYear(), false));
		
		return sb.toString();
	}
	
	/**
	 * Gets the day header for 3 calendars
	 * 
	 * @param isMonday The option of starting day. True for Monday. False for Sunday.
	 * @return the day header of 3 calendars
	 */
	private String getThreeMonthHeader(boolean isMonday) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			sb.append(getMonthHeader(isMonday));
			if (i < 2) {
				sb.append("  ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Get the first row of the 3 calendars, considering the offset based on the first day of the calendar.
	 * 
	 * @param offsets 
	 * @param lastValues
	 * @return the first row of the 3 calendars.
	 */
	private String getFirstRowOfThreeCal(int[] offsets, int[] lastValues) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			int offset = offsets[i];
				
			for (int j = 0; j < offsets[i]; j++) {
				sb.append("   ");
			}
			
			int end = 7 - offset;	
			for (int j = 1; j <= end; j++) {
				if (j < 10) {
					sb.append(" " + j);
				} else {
					sb.append(j);
				}
				
				if (j <= end - 1) {
					sb.append(" ");
				}
			}
			
			lastValues[i] = end + 1;
			
			if (i < 2) {
				sb.append("  ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Gets the monthly dates for 3 calendars
	 * 
	 * @param lastValues values to be used as starting day for each of the calendar
	 * @param maxDates the maximum dates of the 3 calendars in an array
	 * @return The monthly dates for 3 calendars
	 */
	private String getMonthDatesOfThreeCal(int[] lastValues, int[] maxDates) {
		StringBuilder sb = new StringBuilder();
		int numRows = 5;
		
		for (int k = 0; k < numRows; k++) {
			for (int i = 0; i < 3; i++) {
				int start = lastValues[i];
				int end = (start + 7 <= maxDates[i])? start + 7 : maxDates[i] + 1;
				
				for (int j = start; j <= start + 6; j++) {
					if (j >= end) {
						sb.append("  ");
					} else {
						if (j < 10) {
							sb.append(" " + j);
						} else {
							sb.append(j);
						}
					}
					
					if (j <= start + 5) {
						sb.append(" ");
					}
				}
				
				lastValues[i] = end;
				
				if (i < 2) {
					sb.append("  ");
				} else {
					sb.append(System.lineSeparator());
				}
			}
		}
		
		return sb.toString();	
	}
	
	/**
	 * Parses year
	 * 
	 * @param yearString an integer to be parsed.
	 * @return the year
	 * @throws CalException if year cannot be parsed to an integer.
	 */
	private Integer parseYear(String yearString) throws CalException {
		try {
			Integer year = Integer.valueOf(yearString);
			return year;
		} catch (NumberFormatException e) {
			throw new CalException("The year specified is not valid");
		}
	}
	
	/**
	 * Parses month
	 * 
	 * @param monthString an integer to be parsed.
	 * @return the month
	 * @throws CalException if month cannot be parsed to an integer.
	 */
	private Integer parseMonth(String monthString) throws CalException {
		try {
			Integer month = Integer.valueOf(monthString);
			return month;
		} catch (NumberFormatException e) {
			throw new CalException("The month specified is not valid");
		}
	}
	
	/**
	 * Check the bounds of the year
	 * 
	 * @param year
	 * @throws CalException if year is outside the range of 1 - 9999
	 */
	private void checkYearBounds(int year) throws CalException {
		if (year < 1 || year > 9999) {
			throw new CalException("The year specified should be in between 1..9999");
		}
	}
	
	/**
	 * Check the bounds of the month
	 * 
	 * @param month
	 * @throws CalException if month is outside the range of 1 - 12
	 */
	private void checkMonthBounds(int month) throws CalException {
		if (month < 1 || month > 12) {
			throw new CalException("The month specified should be in between 1..12");
		}
	}
}

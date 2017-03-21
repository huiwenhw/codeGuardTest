package sg.edu.nus.comp.cs4218.impl.app;

import java.util.Date;

/**
 * BESHCalendar does not make use of any date libraries and provides functionality for CalApplication
 */
public class CalendarUtility {
	
	private int day;
	private int month;
	private int year;

	/**
	 * Constructor for BESHCalendar using the following variables
	 * 
	 * @param d
	 * 		day from 1 - 31
	 * @param m
	 * 		month starting from 0 - 11
	 * @param y
	 * 		year
	 */
	public CalendarUtility(int d, int m, int y) {
		setDay(d);
		month = m + 1;
		year = y;
	}
	
	public int getNumberOfDaysInAMonth() {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 2:
			if (isLeapYear(year)) {
				return 29;
			}
			return 28;
		default:
			return 30;
		}
	}
	
	/**
	 * Gets the first day of the current month set whereby
	 * 1 - Sunday and 7 - Saturday
	 * @return the day of the first day of month
	 */
	public int getDayOfFirstDayInMonth() {
		int thisMonth = getZellerMonth();
		int thisYear = getZellerYear();
		int last2Year = thisYear % 100;
		int century = thisYear / 100;
		
		int dayOfWeek = 1 + (((13*thisMonth)-1)/5) + last2Year + (last2Year/4) + (century/4) - (2*century);
		dayOfWeek = dayOfWeek % 7;
		if (dayOfWeek < 0) {
			while (dayOfWeek < 0) {
				dayOfWeek += 7;
			}
			return dayOfWeek + 1;
		}
		return dayOfWeek + 1;
	}
	
	/**
	 * 1 - March, 12 - February
	 * @return the month used in zeller's formula
	 */
	private int getZellerMonth() {
		int zMonth = (month-2) % 12;
		if (zMonth <= 0) {
			return zMonth + 12;
		}
		return zMonth;
	}
	
	private int getZellerYear() {
		if (month < 3) {
			return year - 1;
		}
		return year;
	}
	
	public Date getCurrentDate() {
		return new Date();
	}
	
	/**
	 * Checks if a year is a leap year
	 * @param y
	 * 			to check
	 * @return
	 * 			boolean of where year y is a leap year
	 */
	public boolean isLeapYear(int y) {
		if (y % 400 ==0) {
			return true;
		} else if (y % 100 == 0) {
			return false;
		} else if (y % 4 == 0) {
			return true;
		}
		return false;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	public int getMonth() {
		return month - 1;
	}
	
	public int getYear() {
		return year;
	}
}

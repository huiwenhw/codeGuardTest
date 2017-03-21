package ef1test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.impl.app.CalendarUtility;

public class CalendarUtilityTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testConstructionValidArgs() {
		CalendarUtility cal = new CalendarUtility(1,1,1);
	}
	
	@Test
	public void testIsLeapYearForSmallYears() throws Exception {
		CalendarUtility cal = new CalendarUtility(1,1,1);
		assertEquals(false, cal.isLeapYear(1));
		assertEquals(false, cal.isLeapYear(2));
		assertEquals(false, cal.isLeapYear(3));
		assertEquals(true, cal.isLeapYear(4));
	}
	
	@Test
	public void testIsLeapYearForCenturies() throws Exception {
		CalendarUtility cal = new CalendarUtility(1,1,1);
		assertEquals(false, cal.isLeapYear(100));
		assertEquals(false, cal.isLeapYear(200));
		assertEquals(false, cal.isLeapYear(300));
		assertEquals(true, cal.isLeapYear(400));
	}
	
	@Test
	public void testIsLeapYearForMillenia() throws Exception {
		CalendarUtility cal = new CalendarUtility(1,1,1);
		assertEquals(false, cal.isLeapYear(1000));
		assertEquals(true, cal.isLeapYear(2000));
		assertEquals(false, cal.isLeapYear(3000));
		assertEquals(true, cal.isLeapYear(4000));
	}
	
	@Test
	public void testAllDayNamesInAYear() throws Exception {
		for (int year = 1752; year <= 2016; year++) {
			for (int month = 0; month < 12; month++) {
				GregorianCalendar oracle = new GregorianCalendar(year, month, 1);
				int firstDayOfWkInMon = oracle.get(Calendar.DAY_OF_WEEK);
				CalendarUtility candidate = new CalendarUtility(1, month, year);
				assertEquals(firstDayOfWkInMon, candidate.getDayOfFirstDayInMonth());
			}
		}
	}
}

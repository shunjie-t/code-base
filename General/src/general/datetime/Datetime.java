package general.datetime;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Timer;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class Datetime {
	private static void javaTextLibrary() {
		/*java.text library*/
		DateFormat dateFormat = DateFormat.getInstance();
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
	}
	
	private static void javaTimeLibrary() {
		System.out.println("==javaTimeLibrary==");
		LocalDate localDate1 = LocalDate.now();
		p(localDate1,"localDate1");
		LocalDate localDate2 = LocalDate.of(2019, 12, 1);
		p(localDate2,"localDate2");		
		LocalDate localDate2Plus4Day1Month = LocalDate.parse(localDate2.plusMonths(1).plusDays(4).toString());
		p(localDate2Plus4Day1Month,"localDate2Plus4Day1Month");
		LocalDate dayOfYear = LocalDate.ofYearDay(2020, 366);
		p(dayOfYear,"dayOfYear");
		System.out.println();
		
		Year year = Year.now();
		p(year);
		p(year.atDay(300),"year.atDay()");
		p(year.isAfter(year),"year.isAfter()");
		p(year.isBefore(year),"year.isBefore()");
		p(year.isLeap(),"year.isLeap()");
		p(year.minusYears(1),"year.minusYears()");
		p(year.plusYears(1),"year.plusYears()");
		Year yearOf = Year.of(2004);
		p(yearOf);
		Year yearParse = Year.parse("2019");
		p(yearParse);
		
		Month month = Month.MARCH;
		p(month);
		p(month.getValue(),"month.getValue()");
		month.compareTo(month);
		month.firstDayOfYear(false);
		month.firstMonthOfQuarter();
		month.getLong(null);
		month.adjustInto(yearParse);
		month.compareTo(month);
		month.describeConstable();
		month.get(null);
		month.isSupported(null);
		month.length(false);
		month.minus(0);
		Month monthOf = Month.of(5);
		p(monthOf);
		
		MonthDay monthDay = MonthDay.of(Month.FEBRUARY, 29);
//		p(monthDay);
		
		LocalTime localTime = LocalTime.now();
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zoneDateTime = ZonedDateTime.now();
		Instant instant = Instant.now();
		Duration duartion = Duration.ZERO;
		Period period = Period.ZERO;
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy");
		Clock clock = Clock.systemUTC();
	}
	
	private static void javaUtilLibrary() {
		/*java.util library*/
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		SimpleTimeZone simpleTimeZone = new SimpleTimeZone(1,"a");
		Timer timer = new Timer();
		TimeZone timeZone = TimeZone.getDefault();
	}
	
	private static void apacheCommonLang3Library() {
		/*apache.commons.lang3 library*/
		DateUtils dateUtils = new DateUtils();
		DurationFormatUtils durationFormatUtils = new DurationFormatUtils();
	}
	
	private static <T> void p(T input) {
		p(input, null);
	}
	
	private static <T> void p(T input, String varName) {
		if(varName != null ) {			
			System.out.printf("%s, %s: %s\n",input.getClass().getSimpleName(), varName, input);
		}
		else {
			System.out.printf("%s: %s\n",input.getClass().getSimpleName(), input);
		}
	}
}

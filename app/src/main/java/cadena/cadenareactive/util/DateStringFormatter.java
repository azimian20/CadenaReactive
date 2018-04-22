package cadena.cadenareactive.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringFormatter {

	private DateStringFormatter() {
		
	}
	public static Date format(String input) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			System.out.println("****************** Formatting Date");
			return formatter.parse(input);
		} catch (ParseException e) {
			System.out.println("Date formatter failed on input:" + input + " server date will be applied");
			return new Date();
		}
	}
}

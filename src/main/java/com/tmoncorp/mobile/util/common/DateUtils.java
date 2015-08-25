package com.tmoncorp.mobile.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);

	public static boolean isToday(String string){
		try {
			Date date=formatter.parse(string);
			Calendar today=Calendar.getInstance();
			Calendar compare=Calendar.getInstance();
			compare.setTime(date);

			if (today.get(Calendar.ERA) == compare.get(Calendar.ERA) &&
					today.get(Calendar.YEAR) == compare.get(Calendar.YEAR) &&
					today.get(Calendar.DAY_OF_YEAR) == compare.get(Calendar.DAY_OF_YEAR))
				return true;
		} catch (ParseException e) {

		}
		return false;
	}
}

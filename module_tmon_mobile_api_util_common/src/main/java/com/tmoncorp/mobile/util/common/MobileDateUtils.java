package com.tmoncorp.mobile.util.common;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MobileDateUtils {

	private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);

	private MobileDateUtils() {
		throw new AssertionError("static utility class");
	}

	public static boolean isToday(String string) {
		Date date = null;
		try {
			date = FORMATTER.parse(string);
		} catch (ParseException e) {
			return false;
		}
		Calendar today = Calendar.getInstance();
		Calendar compare = Calendar.getInstance();
		compare.setTime(date);
		return DateUtils.isSameDay(today, compare);

	}
}

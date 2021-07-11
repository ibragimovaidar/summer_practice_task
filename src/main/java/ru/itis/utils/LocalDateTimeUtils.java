package ru.itis.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class LocalDateTimeUtils {

	/**
	 * @param dateString string in format dd.mm.yyyy.
	 * @return LocalDate object of given date string.
	 * */
	public static LocalDate localDateOfDateString(String dateString){
		int[] localDateValues = Arrays.stream(dateString.split("\\."))
				.mapToInt(Integer::parseInt)
				.toArray();
		return LocalDate.of(localDateValues[2], localDateValues[1], localDateValues[0]);
	}

	public static String dateStringOfLocalDate(LocalDate localDate){
		return localDate.getDayOfMonth() + "." + localDate.getMonthValue() + "." + localDate.getYear();
	}

	public static String timeStringOfLocalTime(LocalTime localTime){
		return localTime.getHour() + "." + localTime.getMinute();
	}

	public static LocalTime localTimeOfTimeString(String timeString){
		int[] localTimeValues = Arrays.stream(timeString.split(":"))
				.mapToInt(Integer::parseInt)
				.toArray();
		return LocalTime.of(localTimeValues[0], localTimeValues[1]);
	}

}

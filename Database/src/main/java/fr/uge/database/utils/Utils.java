package fr.uge.database.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Détermine si la date donné sous forme de String est conforme au format DATE_FORMAT,
	 * C'est à dire au format du Timestamp sous postgreSQL.
	 * 
	 * @param dateStr La date sous forme de String
	 * @return True si la String est conforme, False sinon
	 */
	public static boolean isValidDate(String dateStr) {
		var format = new SimpleDateFormat(DATE_FORMAT);
		format.setLenient(false);
		
		try {
			format.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		
		return true;
	}
}

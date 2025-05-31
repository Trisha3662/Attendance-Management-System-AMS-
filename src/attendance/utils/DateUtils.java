package attendance.utils;

import java.util.Date;       // Represents date and time.
import java.text.SimpleDateFormat;  //Formats dates into a specific pattern (yyyy-MM-dd in this case).

public class DateUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static boolean isSameDay(Date date1, Date date2) {
        return dateFormat.format(date1).equals(dateFormat.format(date2));
    }
    
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
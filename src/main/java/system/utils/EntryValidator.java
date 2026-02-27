package system.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryValidator {
    private static final Pattern p = Pattern.compile("^[A-Za-z]+ [A-Za-z]+$");

    public static boolean isInteger(String nmec) {
        try {
            Integer.parseInt(nmec);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } 
    }

    public static boolean isValidFullName(String full_name) {
        Matcher m = p.matcher(full_name);
        return m.matches();       
    }

    public static boolean isValidGrade(String grade) {
        try {
            double p_grade = Float.parseFloat(grade);
            return p_grade >= 0 && p_grade <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

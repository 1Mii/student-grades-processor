package system.java_app.service.importer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryValidator {
    private static final Pattern p = Pattern.compile("^[A-Za-z]+ [A-Za-z]+$");

    public static boolean isLong(String nmec) {
        try {
            Long.parseLong(nmec);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } 
    }

    public static boolean isValidFullName(String full_name) {
        if (full_name == null) return false;
        String trimmedName = full_name.trim();
        if (trimmedName.isEmpty()) return false;
        return p.matcher(trimmedName).matches();      
    }

    public static boolean isValidGrade(String grade) {
        try {
            double p_grade = Double.parseDouble(grade);
            return p_grade >= 0 && p_grade <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
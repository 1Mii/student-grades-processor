package system.java_app.service.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import system.java_app.model.Grade;
import system.java_app.model.Student;

public class CSVReader implements Reader {

    @Override
    public Student extractEntry(String entry) {
        String[] info = entry.strip().split(";");
        if (info.length != 5) 
            throw new IllegalArgumentException("Entry must have 5 fields");
        
        if (!EntryValidator.isLong(info[0])) 
            throw new IllegalArgumentException("Nmec must be a long");

            
        if (!EntryValidator.isValidFullName(info[1])) 
            throw new IllegalArgumentException("Name must be valid");

        Student student = new Student();
        student.setNmec(Long.parseLong(info[0]));
        String[] name = info[1].split(" ");
        student.setFirst_name(name[0]);
        student.setLast_name(name[1]);

        List<Grade> grades = new ArrayList<>();
    
        for (int i = 2; i < info.length; i++) {
            if (!EntryValidator.isValidGrade(info[i])) 
                throw new IllegalArgumentException("Grades given must be valid");
            
            grades.add(new Grade(student, (i - 1), Double.parseDouble(info[i])));
        }

        student.setGrades(grades);

        return student;
    }
}

package utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import structs.DataExtracted;
import structs.Grade;
import structs.Student;

public class CSVReader implements Reader {

    @Override
    public DataExtracted extractData(String filename) throws IOException {
        List<String> temp = Files.readAllLines(Paths.get(filename));
        int invalid_records = 0;
        int valid_records = 0;
        List<Student> students = new ArrayList<>();

        for (String entry : temp) {
            String[] info = entry.strip().split(";");
            if (info.length != 5) {
                invalid_records++;
                System.out.println("Entry: " + entry + " NOT ADDED");
                continue;
            }
            if (!EntryValidator.isInteger(info[0])) {
                invalid_records++;
                System.out.println("Entry: " + entry + " NOT ADDED");
                continue;
            }
            if (!EntryValidator.isValidFullName(info[1])) {
                invalid_records++;
                System.out.println("Entry: " + entry + " NOT ADDED");
                continue;
            }

            List<Grade> grades = new ArrayList<>();
            int num_of_exam = 1;
            int i;
            for (i=2; i<info.length; i++) {
                if (!EntryValidator.isValidGrade(info[i])) 
                    break;
                
                grades.add(new Grade(num_of_exam, Float.parseFloat(info[i])));
            }

            if (i != info.length) {
                System.out.println("Entry: " + entry + " NOT ADDED");
                invalid_records++;
                continue;
            }
            
            String[] name = info[1].split(" ");
            Student student = new Student(Integer.parseInt(info[0]), name[0], name[1], grades);
            students.add(student);
            valid_records++;
        }

        return new DataExtracted(students, invalid_records, valid_records);
    }
}
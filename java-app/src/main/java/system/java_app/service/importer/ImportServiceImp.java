package system.java_app.service.importer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import system.java_app.model.Student;
import system.java_app.repository.StudentRepository;

@Service
@AllArgsConstructor
public class ImportServiceImp implements ImportService {
    private final StudentRepository repo;

    public DataExtracted storeFileData(Reader reader, BufferedReader br) {
        int invalid_records = 0;
        int valid_records = 0;
        String entry;
        List<Student> students_accepted = new ArrayList<>();
        try {
            while ((entry = br.readLine()) != null) {
                try {
                    Student student = reader.extractEntry(entry);
                    repo.save(student);
                    students_accepted.add(student);
                    valid_records++;
                } catch (Exception e) {
                    invalid_records++;
                    System.err.println("Error in the entry: " + entry);
                } 
            }
        } catch (IOException e) {
            System.err.println("Error reading file" + e.getMessage());
        }
        return new DataExtracted(students_accepted, invalid_records, valid_records);
    }
}

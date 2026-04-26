package system.java_app.service.importer;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import system.java_app.model.Student;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DataExtracted {
    private List<Student> students;
    private int invalid_records;
    private int valid_records;
}
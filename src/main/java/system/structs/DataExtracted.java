package system.structs;

import java.util.List;

public class DataExtracted {
    private List<Student> students;
    private int invalid_records;
    private int valid_records;

    public DataExtracted(List<Student> students, int invalid_records, int valid_records) {
        this.students = students;
        this.invalid_records = invalid_records;
        this.valid_records = valid_records;
    }

    public List<Student> getStudents() {
        return students;
    }

    public int getInvalid_records() {
        return invalid_records;
    }

    public int getValid_records() {
        return valid_records;
    }
}

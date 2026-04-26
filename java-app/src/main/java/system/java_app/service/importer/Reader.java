package system.java_app.service.importer;

import system.java_app.model.Student;

public interface Reader {
    public Student extractEntry(String entry);
}

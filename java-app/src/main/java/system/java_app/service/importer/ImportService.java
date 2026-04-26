package system.java_app.service.importer;

import java.io.BufferedReader;

public interface ImportService {
    DataExtracted storeFileData(Reader reader, BufferedReader br);
}
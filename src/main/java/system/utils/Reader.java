package system.utils;
import java.io.IOException;

import system.structs.DataExtracted;

public interface Reader {
    public DataExtracted extractData(String filename) throws IOException;
}

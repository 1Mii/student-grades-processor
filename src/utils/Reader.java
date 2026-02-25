package utils;
import java.io.IOException;

import structs.DataExtracted;

public interface Reader {
    public DataExtracted extractData(String filename) throws IOException;
}

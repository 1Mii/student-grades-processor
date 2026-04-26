package system.java_app.controler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import system.java_app.service.importer.CSVReader; 
import system.java_app.service.importer.ImportService;
import system.java_app.service.importer.DataExtracted;

@AllArgsConstructor
@RestController
@RequestMapping("api/upload-file")
@CrossOrigin(origins = "http://localhost:8000")
public class ImportControler {
    
    private final ImportService importService;

    @PostMapping()
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !"text/csv".equals(file.getContentType()))
            return new ResponseEntity<>("Make sure to send a valid csv file", HttpStatus.BAD_REQUEST);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            DataExtracted data = importService.storeFileData(new CSVReader(), br);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing csv file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

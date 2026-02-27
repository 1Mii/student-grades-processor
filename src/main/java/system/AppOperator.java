package system;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import system.db.DatabaseConnection;
import system.db.StudentRepository;
import system.structs.DataExtracted;
import system.structs.Grade;
import system.structs.Student;
import system.utils.CSVReader;
import system.utils.Reader;

public class AppOperator {
    private final StudentRepository repo;
    private final Reader reader;
    private int valid_records;
    private int invalid_records;
    private final String PATH = System.getenv("DATA_PATH");

    public AppOperator(StudentRepository repo, Reader reader) {
        this.repo = repo;
        this.reader = reader;
    }

    public void storeFileData(String filename) {
        try {
            DataExtracted data = reader.extractData(Paths.get(PATH, filename).toString());
            invalid_records += data.getInvalid_records();
            List<Student> students = data.getStudents();

            DataExtracted savedData = repo.save(students);

            valid_records += savedData.getValid_records();
            invalid_records += savedData.getInvalid_records();
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public void displayAverageById(int id) {
        Student student = repo.findById(id);
        
        if (student != null)
            System.out.printf("Average of Student with id %d: %.2f%n", id, student.getAverageGrade());
        else
            System.out.println("The student id provided doesn't exists");
    }

    public void displayStudentGradesById(int id) {
        Student student = repo.findById(id);

        if (student == null) {
            System.out.println("Student id provided doesn't exists");
            return;
        }

        System.out.printf("%s grades:%n", student.toString());

        for (Grade grade : student.getGrades()) {
            System.out.println(" - " + grade.toString());
        }
    }

    public void shutdownProcedure() {
        System.out.println("Records successfully processed: " + valid_records);
        System.out.println("Records rejected: " + invalid_records);
        System.out.println("Program finished");
    }

    public void printOptionList() {
        System.out.println("\n\nSelect a option (<option> <filename/id>). 'Enter' to stop:");
        System.out.println("1) Process and store data from CSV file");
        System.out.println("2) Calculate the average of a student based on the student identifier");
        System.out.println("3) Display all grades associated with a specific student (assuming via id)");
    }

    public static void main(String[] args) {
        AppOperator operator = new AppOperator(new StudentRepository(), new CSVReader());
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            operator.printOptionList();
            String option = sc.nextLine();

            if (option.isEmpty()) {
                DatabaseConnection.closeConnection();
                operator.shutdownProcedure();
                break;
            }

            switch (option) {
                case "1":
                    System.out.print("Give file name: ");
                    String filename = sc.nextLine();
                    operator.storeFileData(filename);
                    break;

                case "2":
                    System.out.print("Give student id: ");
                    String id_2 = sc.nextLine();
                    try {
                        int student_id_2 = Integer.parseInt(id_2);
                        operator.displayAverageById(student_id_2);
                    } catch (NumberFormatException e) {
                        System.out.println("Student id should be an positive integer");
                    }
                    break;
                
                case "3":
                    System.out.print("Give student id: ");
                    String id_3 = sc.nextLine();
                    try {
                        int student_id_3 = Integer.parseInt(id_3);
                        operator.displayStudentGradesById(student_id_3); 
                    } catch (NumberFormatException e) {
                        System.out.println("Student id should be an positive integer");
                    }
                default:
                    break;
            }
        }

        sc.close();
    }
}

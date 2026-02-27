package system.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import system.structs.DataExtracted;
import system.structs.Grade;
import system.structs.Student;

public class StudentRepository {

    public DataExtracted save(List<Student> students) {
        // IA também ajudou no seguinte: ON CONFLICT (num_mec) DO NOTHING faz com que que a transação nao fique considerada como abortada 
        // (sem isto como ficava abortada o programa só partia)
        String studentSQL = "INSERT INTO Students (num_mec, first_name, last_name) VALUES (?, ?, ?) ON CONFLICT (num_mec) DO NOTHING";
        String gradeSQL = "INSERT INTO Grades (student_id, exam, grade) VALUES (?, ?, ?)";
        int invalid_records = 0;
        int valid_records = 0;

        try (Connection connection = DatabaseConnection.getConnection()) {
            // IA recomenda assim para poder ter mais controle sobre as transações
            // Entendo neste problema mas se tivesse feito SPs acho que já nao seria necessário talvez
            connection.setAutoCommit(false); 

            try (PreparedStatement studentStmt = connection.prepareStatement(studentSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement gradeStmt = connection.prepareStatement(gradeSQL)) {

                for (Student student : students) {
                    studentStmt.setInt(1, student.getNum_mec());
                    studentStmt.setString(2, student.getFirst_name());
                    studentStmt.setString(3, student.getLast_name());
                    
                    int affected = studentStmt.executeUpdate();
                    long studentId;

                    if (affected > 0) {
                        valid_records++;
                        try (ResultSet rs = studentStmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                studentId = rs.getLong(1);
                            } else {
                                throw new SQLException("Error getting student id");
                            }
                        }
                        System.out.println(student.toString() + " ADDED with id=" + studentId);
                    } else {
                        System.out.println(student.toString() + " NOT ADDED");
                        invalid_records++;
                        continue; // Considero que a entry está incorreta
                    }

                    for (Grade grade : student.getGrades()) {
                        gradeStmt.setLong(1, studentId);
                        gradeStmt.setInt(2, grade.getExam());
                        gradeStmt.setFloat(3, grade.getGrade());
                        gradeStmt.addBatch();
                        System.out.println("\t" + grade.toString() + " ADDED");
                    }
                }
                
                gradeStmt.executeBatch();
                connection.commit();
                System.out.println("\n-- SAVE PROCEDURE DONE --\n");
            } catch (SQLException e) {
                System.err.println("Error defining prepared statements");
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error conecting to Database");
            e.printStackTrace();
            System.exit(1);
        }

        return new DataExtracted(null, invalid_records, valid_records);
    }

    public Student findById(int id) {
        String getStudentSQL = "SELECT * FROM Students WHERE id = ?";
        String getGradesStudentSQL = "SELECT * FROM Grades WHERE student_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement getStudentStm = connection.prepareStatement(getStudentSQL);
            PreparedStatement getGradesStm = connection.prepareStatement(getGradesStudentSQL)) {

            getStudentStm.setInt(1, id);
            try (ResultSet rs_student = getStudentStm.executeQuery()) {
                if (rs_student.next()) {
                    int student_id = rs_student.getInt("id");
                    int num_mec = rs_student.getInt("num_mec");
                    String first_name = rs_student.getString("first_name");
                    String last_name = rs_student.getString("last_name");
                    List<Grade> grades = new ArrayList<>();
    
                    getGradesStm.setInt(1, id);
                    try (ResultSet rs_grades = getGradesStm.executeQuery()) {
                        while (rs_grades.next()) {
                            int grade_id = rs_grades.getInt("id");
                            int exam = rs_grades.getInt("exam");
                            float grade = rs_grades.getFloat("grade");
    
                            grades.add(new Grade(grade_id, exam, grade));
                        }
                    }
                    
                    return new Student(student_id, num_mec, first_name, last_name, grades);
                }
            } 
        } catch (SQLException e) {
            System.err.println("Error connection to Database/defining prepared statements/other");
            e.printStackTrace();
        }
        return null;
    }
    
}

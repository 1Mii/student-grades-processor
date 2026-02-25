package structs;
import java.util.List;

public class Student {
    private int id;
    private int num_mec;
    private String first_name;
    private String last_name;
    private List<Grade> grades;

    public Student(int id, int num_mec, String first_name, String last_name, List<Grade> grades) {
        this.id = id;
        this.num_mec = num_mec;
        this.first_name = first_name;
        this.last_name = last_name;
        this.grades = grades;
    }

    public Student(int num_mec, String first_name, String last_name, List<Grade> grades) {
        this.num_mec = num_mec;
        this.first_name = first_name;
        this.last_name = last_name;
        this.grades = grades;
    }

    public int getId() {
        return id;
    }

    public int getNum_mec() {
        return num_mec;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFull_name() {
        return first_name + " " + last_name;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public double getAverageGrade() {
        float sumGrades = 0;
        int numExams = 0;
        for (Grade grade : grades) {
            sumGrades += grade.getGrade();
            numExams += 1;
        }
        return sumGrades / numExams;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", num_mec=" + num_mec + ", first_name=" + first_name + ", last_name=" + last_name
                + "]";
    }
}

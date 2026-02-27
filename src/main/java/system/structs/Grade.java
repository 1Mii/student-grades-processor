package system.structs;

public class Grade {
    private int id;
    private int exam;
    private float grade;

    public Grade(int id, int exam, float grade) {
        this.id = id;
        this.exam = exam;
        this.grade = grade;
    }

    public Grade(int exam, float grade) {
        this.exam = exam;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getExam() {
        return exam;
    }

    public float getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Grade [id=" + id + ", exam=" + exam + ", grade=" + grade + "]";
    }
}

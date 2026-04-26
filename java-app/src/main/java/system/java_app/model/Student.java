package system.java_app.model;

import jakarta.persistence.*;
import lombok.*;
import system.java_app.model.Grade;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {

    public Student(Long nmec, String firstName, String lastName, List<Grade> grades) {
        this.nmec = nmec;
        this.first_name = firstName;
        this.last_name = lastName;
        this.grades = grades;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_mec", nullable = false, unique = true)
    private Long nmec;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> grades;

    public Double getGradesMean() {
        if (grades == null || grades.isEmpty())
            return 0.0;
        
        Double sum = 0.0;
        for (Grade grade : grades) 
            sum += grade.getGrade();
        
        return sum / grades.size();
    }
}

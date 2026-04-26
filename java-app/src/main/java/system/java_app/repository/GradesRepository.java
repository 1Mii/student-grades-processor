package system.java_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.java_app.model.Grade;

@Repository

public interface GradesRepository extends JpaRepository<Grade, Long> {
}
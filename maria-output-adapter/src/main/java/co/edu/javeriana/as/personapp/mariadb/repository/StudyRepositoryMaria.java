package co.edu.javeriana.as.personapp.mariadb.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

@Repository
public interface StudyRepositoryMaria extends JpaRepository<EstudiosEntity, EstudiosEntityPK> {
}

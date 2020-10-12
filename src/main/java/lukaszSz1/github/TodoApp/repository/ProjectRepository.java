package lukaszSz1.github.TodoApp.repository;


import lukaszSz1.github.TodoApp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAll();

    Optional<Project> findById(Long id);

    Project save(Project entity);

}

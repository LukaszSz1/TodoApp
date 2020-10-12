package lukaszSz1.github.TodoApp.repository;



import lukaszSz1.github.TodoApp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlProjectRepository extends JpaRepository<Project, Integer>, ProjectRepository {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

}

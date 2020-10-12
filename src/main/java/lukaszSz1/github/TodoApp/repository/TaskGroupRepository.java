package lukaszSz1.github.TodoApp.repository;



import lukaszSz1.github.TodoApp.model.TaskGroup;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(int id);

    TaskGroup save(TaskGroup entity);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

}

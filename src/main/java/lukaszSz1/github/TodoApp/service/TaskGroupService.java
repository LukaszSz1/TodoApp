package lukaszSz1.github.TodoApp.service;


import lukaszSz1.github.TodoApp.model.Project;
import lukaszSz1.github.TodoApp.model.TaskGroup;
import lukaszSz1.github.TodoApp.model.dtos.GroupReadModel;
import lukaszSz1.github.TodoApp.model.dtos.GroupWriteModel;
import lukaszSz1.github.TodoApp.repository.TaskGroupRepository;
import lukaszSz1.github.TodoApp.repository.TaskRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {

    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository taskGroupRepository, final TaskRepository taskRepository) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {

        return taskGroupRepository.findAll().stream().map(source -> new GroupReadModel(source)).collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = taskGroupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}

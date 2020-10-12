package lukaszSz1.github.TodoApp.controller;

import lukaszSz1.github.TodoApp.model.Task;
import lukaszSz1.github.TodoApp.model.dtos.GroupReadModel;
import lukaszSz1.github.TodoApp.model.dtos.GroupWriteModel;
import lukaszSz1.github.TodoApp.repository.TaskRepository;
import lukaszSz1.github.TodoApp.service.TaskGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {


//    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository taskRepository;
    private final TaskGroupService taskGroupService;

    TaskGroupController(final TaskRepository taskRepository, final TaskGroupService taskGroupService) {
        this.taskRepository = taskRepository;
        this.taskGroupService = taskGroupService;
    }


    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = taskGroupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }


    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

}

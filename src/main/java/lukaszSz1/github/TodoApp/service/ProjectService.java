package lukaszSz1.github.TodoApp.service;


import lukaszSz1.github.TodoApp.TaskConfigurationProperties;
import lukaszSz1.github.TodoApp.model.Project;
import lukaszSz1.github.TodoApp.model.dtos.GroupReadModel;
import lukaszSz1.github.TodoApp.model.dtos.GroupTaskWriteModel;
import lukaszSz1.github.TodoApp.model.dtos.GroupWriteModel;
import lukaszSz1.github.TodoApp.model.dtos.ProjectWriteModel;
import lukaszSz1.github.TodoApp.repository.ProjectRepository;
import lukaszSz1.github.TodoApp.repository.TaskGroupRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private TaskConfigurationProperties taskConfigurationProperties;
    private TaskGroupRepository taskGroupRepository;
    private ProjectRepository projectRepository;
    private TaskGroupService taskGroupService;

    ProjectService(final TaskConfigurationProperties taskConfigurationProperties, final TaskGroupRepository taskGroupRepository, final ProjectRepository projectRepository, final TaskGroupService taskGroupService) {
        this.taskConfigurationProperties = taskConfigurationProperties;
        this.taskGroupRepository = taskGroupRepository;
        this.projectRepository = projectRepository;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!taskConfigurationProperties.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}


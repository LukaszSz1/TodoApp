package lukaszSz1.github.TodoApp.model.dtos;


import lukaszSz1.github.TodoApp.model.Project;
import lukaszSz1.github.TodoApp.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {

    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    // tworzenie nowegj Grupy
    public TaskGroup toGroup(final Project project) {
        var result = new TaskGroup();
        result.setDescription(description);

        // dla każdego taska writeModel robimy toTask
        result.setTasks(tasks.stream()
                .map(source -> source.toTask(result))
                .collect(Collectors.toSet()));
        result.setProject(project);
        return result;
    }
}

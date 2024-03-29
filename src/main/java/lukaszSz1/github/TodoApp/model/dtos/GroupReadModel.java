package lukaszSz1.github.TodoApp.model.dtos;


import lukaszSz1.github.TodoApp.model.Task;
import lukaszSz1.github.TodoApp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {

    private int id;

    private String description;

    // last Task deadline in group
    private LocalDateTime deadline;

    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();

        //pobieramy taski i znajdujemy najstarszą datę jeśli jakąś znajdziemy to ustawiamy ją jako deadline
        source.getTasks().stream().map(Task::getDeadline).max(LocalDateTime::compareTo).ifPresent(date -> deadline = date);

        // ustawiamy taksi na to co jest w GroupTaskReadModel
        tasks = source.getTasks().stream().map(source1 -> new GroupTaskReadModel(source1)).collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}

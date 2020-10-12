package lukaszSz1.github.TodoApp.model.dtos;

import lukaszSz1.github.TodoApp.model.Task;
import lukaszSz1.github.TodoApp.model.TaskGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {

    private String description;
    private LocalDateTime deadline;

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

    // tworzenie nowego taska
    public Task toTask(final TaskGroup group) {
        return new Task(description, deadline, group);
    }
}

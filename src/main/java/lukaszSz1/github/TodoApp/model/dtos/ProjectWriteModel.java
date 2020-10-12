package lukaszSz1.github.TodoApp.model.dtos;

import lukaszSz1.github.TodoApp.model.Project;
import lukaszSz1.github.TodoApp.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectWriteModel {

    @NotBlank(message = "Project's description must not be empty")
    private String description;

    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectStep> steps) {
        this.steps = steps;
    }
}

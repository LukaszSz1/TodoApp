package lukaszSz1.github.TodoApp.service;


import lukaszSz1.github.TodoApp.TaskConfigurationProperties;
import lukaszSz1.github.TodoApp.model.Project;
import lukaszSz1.github.TodoApp.model.ProjectStep;
import lukaszSz1.github.TodoApp.model.TaskGroup;
import lukaszSz1.github.TodoApp.model.dtos.GroupReadModel;
import lukaszSz1.github.TodoApp.repository.ProjectRepository;
import lukaszSz1.github.TodoApp.repository.TaskGroupRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    void createGroup_throwsIllegalStateException_when_multipleTasksConfigNotAllowedResultFalse_and_undoneGroupExists() {

        //given
        TaskConfigurationProperties mockConfig = configurationReturning(false);

        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);

        // system under test
        var toTest = new ProjectService(mockConfig, mockGroupRepository, null, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");
    }

    @Test
    void createGroup_throwsIllegalStateException_when_multipleTasksConfigNotAllowedResultFalse_and_undoneGroupNotExists_and_NoProjectsForGivenId() {

        //given
        TaskConfigurationProperties mockConfig = configurationReturning(false);

        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());

        // system under test
        var toTest = new ProjectService(mockConfig, mockGroupRepository, mockRepository, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    void createGroup_throwsIllegalStateException_when_multipleTasksConfigNotAllowedResultTrue_and_undoneGroupNotExists_and_NoProjectsForGivenId() {

        //given
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());

        // system under test
        var toTest = new ProjectService(mockConfig, mockGroupRepository, mockRepository, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Disabled
    @Test
    void createGroup_createsAndSavesNewGroup_from_givenProjectId() {

        //given
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        InMemoryGroupRepository inMemoryGroupRepository = inMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepository.count();

        var project = createProjectWith("bar", Set.of(-1, -2));

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(project));

        var today = LocalDate.now().atStartOfDay();

        var serviceWithInMemRepo = new TaskGroupService(inMemoryGroupRepository,null);

        //system under test
        var toTest = new ProjectService(mockConfig, inMemoryGroupRepository, mockRepository, serviceWithInMemRepo);

        //when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then - check if something was save
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }

    //metoda zwracająca obiekt
    private Project createProjectWith(String projectDescription, Set<Integer> daysToDeadline) {

        Set<ProjectStep> steps = daysToDeadline.stream().map(days -> {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    // TaskGroupRepository Mock existsByDoneIsFalseAndProject_Id returns true/true
    private TaskGroupRepository groupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }


    //    TaskConfigurationProperties Mock isAllowMultipleTasks() method returns true/false
    private TaskConfigurationProperties configurationReturning(boolean result) {

        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);


        // bierzemy Template, który wiemy, że zwróci true/flase
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private long index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final int id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                map.put(entity.getId(), entity);
            }
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream().filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

}
package lukaszSz1.github.TodoApp.service;


import lukaszSz1.github.TodoApp.model.TaskGroup;
import lukaszSz1.github.TodoApp.repository.TaskGroupRepository;
import lukaszSz1.github.TodoApp.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    void toggleGroup_throwsIllegalStateException_when_tasksAreUndone() {

        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);

        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("undone tasks");
    }

    @Test
    void toggleGroup_throwsIllegalArgumentException_when_tasksDone_and_noGroup() {

        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);

        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        //system under test
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("id not found");
    }

    @Test
    void toggleGroup_worksAsExpected() {

        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);

        var group = new TaskGroup();
        var beforeToggle = group.isDone();

        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(group));

        //system under test
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(0);

        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}

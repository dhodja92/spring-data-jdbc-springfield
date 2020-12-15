package com.github.dhodja92.springdatajpademo.domain.model.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import com.github.dhodja92.springdatajpademo.domain.project.Project;
import com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository;
import com.github.dhodja92.springdatajpademo.domain.task.Task;
import com.github.dhodja92.springdatajpademo.domain.task.TaskRepository;
import com.github.dhodja92.springdatajpademo.testcontainers.PostgreSqlContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskBugfix;
import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskEstimate;
import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskEstimateId;
import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskEstimateProjectId;
import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskRefactor;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Sql(config = @SqlConfig(separator = ScriptUtils.EOF_STATEMENT_SEPARATOR))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgreSqlContainerConfiguration.class)
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void existsById_existingTaskshouldReturnTrue() {
        boolean exists = this.taskRepository.existsById(UUID.fromString("618899c-6446-4c9d-9307-b8c9ca821b98"));
        assertThat(exists).isTrue();
    }

    @Test
    public void existsById_nonExistingTask_shouldReturnFalse() {
        boolean exists = this.taskRepository.existsById(UUID.randomUUID());
        assertThat(exists).isFalse();
    }

    @Test
    public void findAll_shouldReturnAllTasks() {
        Iterable<Task> tasks = this.taskRepository.findAll();
        assertThat(tasks).containsAll(List.of(taskEstimate, taskRefactor, taskBugfix));
    }

    @Test
    public void findAllPaged_shouldReturnAllTasks() {
        Page<Task> tasks = this.taskRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"))
        );
        assertThat(
                tasks.getContent()
        ).containsAll(
                List.of(taskEstimate, taskRefactor, taskBugfix)
        );
    }

    @Test
    public void findByProjectId_shouldReturnProjectTasks() {
        List<Task> tasks = this.taskRepository.findByProjectId(taskEstimateProjectId);
        assertThat(tasks.size()).isEqualTo(1);
    }

    @Test
    public void findByProjectIdPaged_shouldReturnProjectTasks() {
        Page<Task> pagedTasks = this.taskRepository.findByProjectId(
                taskEstimateProjectId,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"))
        );
        assertThat(pagedTasks.getContent().size()).isEqualTo(1);
    }

    @Test
    public void save_nonExistingTask_shouldCreateTask() {
        Task taskDocumentation = new Task(
                "Documentation",
                false,
                Label.TASK,
                Priority.LOW
        );
        Project projectSignify = this.projectRepository.findByName("Signify");
        projectSignify.addTask(taskDocumentation);

        UUID newTaskId = this.projectRepository.save(projectSignify).getTasks().stream()
                .filter(t -> Objects.equals(t.getName(), "Documentation"))
                .map(Task::getId)
                .findFirst()
                .orElseThrow(NullPointerException::new);

        Task newTask = this.taskRepository.findById(newTaskId)
                .orElseThrow(NullPointerException::new);
        assertThat(newTask.getId()).isEqualTo(newTaskId);
        assertThat(newTask.getName()).isEqualTo(taskDocumentation.getName());
        assertThat(newTask.getLabel()).isEqualTo(taskDocumentation.getLabel());
        assertThat(newTask.getPriority()).isEqualTo(taskDocumentation.getPriority());
    }

    @Test
    public void save_existingTask_shouldUpdateTask() {
        Task taskDocumentation = new Task(
                taskEstimateId,
                "Maintenance",
                false,
                Label.TASK,
                Priority.HIGH
        );
        this.taskRepository.save(taskDocumentation);

        Task updatedTask = this.taskRepository.findById(taskEstimate.getId())
                .orElseThrow(NullPointerException::new);
        assertThat(updatedTask.getId()).isEqualTo(taskEstimate.getId());
        assertThat(updatedTask.getName()).isEqualTo("Maintenance");
        assertThat(updatedTask.getLabel()).isEqualTo(Label.TASK);
        assertThat(updatedTask.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    public void deleteById_existingTask_shouldDeleteTask() {
        this.taskRepository.deleteById(taskEstimate.getId());

        Optional<Task> task = this.taskRepository.findById(taskEstimate.getId());
        assertThat(task).isNotPresent();
    }

    static class TaskTestContext {

        private TaskTestContext() {
        }

        static UUID taskEstimateId = UUID.fromString("0618899c-6446-4c9d-9307-b8c9ca821b98");
        static Task taskEstimate = new Task(
                taskEstimateId,
                "Estimate user story",
                false,
                Label.ESTIMATE,
                Priority.MEDIUM
        );
        static UUID taskRefactorId = UUID.fromString("1ab29750-e9b5-4d80-87ee-d13726bc03f8");
        static Task taskRefactor = new Task(
                taskRefactorId,
                "Refactor payment gateway module",
                false,
                Label.TASK,
                Priority.LOW
        );
        static UUID taskBugfixId = UUID.fromString("0618899c-6446-4c9d-9307-b8c9ca821b98");
        static Task taskBugfix = new Task(
                taskBugfixId,
                "Resolve DB deadlock bug",
                false,
                Label.BUG,
                Priority.HIGH
        );

        static UUID taskEstimateProjectId = UUID.fromString("92cdc2eb-7773-4265-a036-64130e05c0a6");
    }
}

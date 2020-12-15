package com.github.dhodja92.springdatajpademo.domain.model.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
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
import java.util.UUID;

import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskBugfix;
import static com.github.dhodja92.springdatajpademo.domain.model.task.TaskRepositoryTests.TaskTestContext.taskEstimate;
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
//        Page<Task> tasks = this.taskRepository.findByProjectId(
//                taskEstimateProjectId,
//                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"))
//        );
        List<Task> tasks = this.taskRepository.findByProjectId(taskEstimateProjectId);
        assertThat(tasks.size()).isEqualTo(1);
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

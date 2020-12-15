package com.github.dhodja92.springdatajpademo.domain.model.project;

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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.github.dhodja92.springdatajpademo.domain.model.project.ProjectRepositoryTests.ProjectTestContext.projectNutriu;
import static com.github.dhodja92.springdatajpademo.domain.model.project.ProjectRepositoryTests.ProjectTestContext.projectPdc;
import static com.github.dhodja92.springdatajpademo.domain.model.project.ProjectRepositoryTests.ProjectTestContext.projectSignify;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Sql(config = @SqlConfig(separator = ScriptUtils.EOF_STATEMENT_SEPARATOR))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgreSqlContainerConfiguration.class)
public class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void existsById_existingProject_shouldReturnTrue() {
        boolean exists = this.projectRepository.existsById(UUID.fromString("8008d6ce-9bf9-4c03-8b32-e89f11d6a518"));
        assertThat(exists).isTrue();
    }

    @Test
    public void existsById_nonExistingProject_shouldReturnFalse() {
        boolean exists = this.projectRepository.existsById(UUID.randomUUID());
        assertThat(exists).isFalse();
    }

    @Test
    public void findAll_shouldReturnAllProjects() {
        List<Project> projects = this.projectRepository.findAll();
        assertThat(
                projects
        ).containsAll(
                List.of(projectNutriu, projectPdc, projectSignify)
        );
    }

    @Test
    public void findAllPaged_shouldReturnAllProjects() {
        Page<Project> projects = this.projectRepository.findAll(
                PageRequest.of(0, 2)
        );
        assertThat(
                projects
        ).containsAll(
                List.of(projectNutriu, projectSignify)
        );
    }

    @Test
    public void findByName_shouldReturnProject() {
        Project project = this.projectRepository.findByName("NutriU");
        assertThat(project.getId()).isEqualTo(projectNutriu.getId());
        assertThat(project.getName()).isEqualTo(projectNutriu.getName());
        assertThat(project.getColor()).isEqualTo(projectNutriu.getColor());
    }

    @Test
    public void save_nonExistingProject_shouldCreateProject() {
        Project project = new Project(
                "Pivotal",
                "#161C1F",
                null
        );
        UUID newProjectId = this.projectRepository.save(project).getId();

        Project newProject = this.projectRepository.findById(newProjectId)
                .orElseThrow(NullPointerException::new);
        assertThat(newProject.getId()).isEqualTo(project.getId());
        assertThat(newProject.getName()).isEqualTo(project.getName());
        assertThat(newProject.getColor()).isEqualTo(project.getColor());
    }

    @Test
    public void save_existingProject_shouldUpdateProject() {
        Project project = new Project(
                projectNutriu.getId(),
                "Pivotal",
                projectNutriu.getColor(),
                projectNutriu.getTasks()
        );
        this.projectRepository.save(project);

        Project updatedProject = this.projectRepository.findById(projectNutriu.getId())
                .orElseThrow(NullPointerException::new);
        assertThat(updatedProject.getId()).isEqualTo(projectNutriu.getId());
        assertThat(updatedProject.getName()).isEqualTo("Pivotal");
        assertThat(updatedProject.getColor()).isEqualTo(projectNutriu.getColor());
        assertThat(updatedProject.getTasks()).containsAll(projectNutriu.getTasks());
    }

    @Test
    public void delete_existingProject_shouldDeleteProjectAndItsTasks() {
        this.projectRepository.delete(projectNutriu);

        Optional<Project> project = this.projectRepository.findById(projectNutriu.getId());
        assertThat(project).isNotPresent();

        List<Task> tasks = this.taskRepository.findByProjectId(projectNutriu.getId());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void deleteById_existingProject_shouldDeleteProjectAndItsTasks() {
        this.projectRepository.deleteById(projectNutriu.getId());

        Optional<Project> project = this.projectRepository.findById(projectNutriu.getId());
        assertThat(project).isNotPresent();

        List<Task> tasks = this.taskRepository.findByProjectId(projectNutriu.getId());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void deleteAll_existingProjects_shouldDeleteProjectsAndTheirTasks() {
        this.projectRepository.deleteAll(List.of(projectNutriu, projectPdc));

        Optional<Project> maybeProjectNutriu = this.projectRepository.findById(projectNutriu.getId());
        assertThat(maybeProjectNutriu).isNotPresent();
        Optional<Project> maybeProjectPdc = this.projectRepository.findById(projectPdc.getId());
        assertThat(maybeProjectPdc).isNotPresent();

        List<Task> nutriuTasks = this.taskRepository.findByProjectId(projectNutriu.getId());
        assertThat(nutriuTasks).isEmpty();
        List<Task> pdcTasks = this.taskRepository.findByProjectId(projectPdc.getId());
        assertThat(pdcTasks).isEmpty();
    }

    static class ProjectTestContext {

        ProjectTestContext init() {
            return this;
        }

        static Project projectNutriu = new Project(
                UUID.fromString("8008d6ce-9bf9-4c03-8b32-e89f11d6a518"),
                "NutriU",
                "#3366ff",
                Set.of(new Task(
                        UUID.fromString("cf970dae-3552-43d5-b2b9-2dc8e05c8559"),
                        "Estimate user story",
                        false,
                        Label.ESTIMATE,
                        Priority.MEDIUM
                ))
        );
        static Project projectPdc = new Project(
                UUID.fromString("c29bd274-1b07-4d43-9539-e67f54792627"),
                "Porsche Digital Croatia",
                "#b32d00",
                Set.of(
                        new Task(
                                UUID.fromString("1ab29750-e9b5-4d80-87ee-d13726bc03f8"),
                                "Refactor payment gateway module",
                                false,
                                Label.TASK,
                                Priority.LOW
                        )
                )
        );
        static Project projectSignify = new Project(
                UUID.fromString("92cdc2eb-7773-4265-a036-64130e05c0a6"),
                "Signify",
                "#00cc66",
                Set.of(
                        new Task(
                                UUID.fromString("0618899c-6446-4c9d-9307-b8c9ca821b98"),
                                "Resolve DB deadlock bug",
                                false,
                                Label.BUG,
                                Priority.HIGH
                        )
                )
        );
    }
}

package com.github.dhodja92.springdatajpademo.interfaces.project;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import com.github.dhodja92.springdatajpademo.domain.project.Project;
import com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository;
import com.github.dhodja92.springdatajpademo.domain.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.github.dhodja92.springdatajpademo.interfaces.project.ProjectResourceControllerTests.ProjectTestContext.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectResourceController.class)
public class ProjectResourceControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectRepository projectRepository;

    @Test
    public void getAllCountries_NoCountries_ShouldReturnOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        given(this.projectRepository.findAll(pageable)).willReturn(Page.empty());

        this.mvc.perform(get("/api/projects")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links.length()", is(0)));

        verify(this.projectRepository).findAll(eq(pageable));
        verifyNoMoreInteractions(this.projectRepository);
    }

    @Test
    public void getAllCountries_SomeCountries_ShouldReturnOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        given(this.projectRepository.findAll(pageable)).willReturn(new PageImpl<>(
                List.of(projectNutriu, projectPdc, projectSignify)
        ));

        this.mvc.perform(get("/api/projects")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links.length()", is(3)));

        verify(this.projectRepository).findAll(eq(pageable));
        verifyNoMoreInteractions(this.projectRepository);
    }

    @TestConfiguration
    static class ProjectTestConfig {

        @Bean
        public ProjectResourceAssembler projectResourceAssembler(ProjectResourceAssembler projectResourceAssembler) {
            return new ProjectResourceAssembler();
        }
    }

    static class ProjectTestContext {
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

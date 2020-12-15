package com.github.dhodja92.springdatajpademo.domain.project;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends CrudRepository<Project, UUID>, PagingProjectRepository {

    boolean existsById(UUID id);

    @Query(
            value = "SELECT p.id p_id, p.name p_name, p.color p_color," +
                    " t.id t_id, t.name t_name, t.finished t_finished, t.label t_label, t.priority t_priority" +
                    " FROM project p" +
                    " JOIN task t ON p.id = t.project_id",
            resultSetExtractorClass = ProjectResultSetExtractor.class
    )
    List<Project> findAll();

    long count();

    Project findByName(String name);

    Project save(Project project);

    void delete(Project project);
}

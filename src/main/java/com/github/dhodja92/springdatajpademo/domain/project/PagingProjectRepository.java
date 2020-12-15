package com.github.dhodja92.springdatajpademo.domain.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagingProjectRepository {

    Page<Project> findAll(Pageable pageable);
}

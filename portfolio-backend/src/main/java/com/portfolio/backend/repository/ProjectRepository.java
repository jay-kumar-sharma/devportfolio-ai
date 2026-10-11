package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProjectRepository
        extends JpaRepository<Project, Long>,
        JpaSpecificationExecutor<Project> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    Optional<Project> findByNameIgnoreCase(String name);
}
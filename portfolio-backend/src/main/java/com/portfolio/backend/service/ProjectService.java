package com.portfolio.backend.service;

import com.portfolio.backend.dto.ProjectRequest;
import com.portfolio.backend.dto.ProjectResponse;
import com.portfolio.backend.enums.ProjectCategory;
import org.springframework.data.domain.Page;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request);

    Page<ProjectResponse> getAllProjects(
            int page,
            int size,
            String sortBy,
            String direction,
            ProjectCategory category,
            Boolean featured,
            String keyword
    );

    ProjectResponse getProjectById(Long id);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);
}
package com.portfolio.backend.service.impl;

import com.portfolio.backend.dto.ProjectRequest;
import com.portfolio.backend.dto.ProjectResponse;
import com.portfolio.backend.entity.Project;
import com.portfolio.backend.enums.ProjectCategory;
import com.portfolio.backend.exception.DuplicateProjectException;
import com.portfolio.backend.exception.ProjectNotFoundException;
import com.portfolio.backend.repository.ProjectRepository;
import com.portfolio.backend.service.ProjectService;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "category",
            "featured",
            "displayOrder",
            "createdAt",
            "updatedAt"
    );

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {

        String normalizedName = normalizeName(request.name());

        if (projectRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateProjectException(
                    "A project with this name already exists: " + normalizedName
            );
        }

        Project project = new Project();

        copyRequestToEntity(request, project);

        Project savedProject = projectRepository.save(project);

        return toResponse(savedProject);
    }

    @Override
    public Page<ProjectResponse> getAllProjects(
            int page,
            int size,
            String sortBy,
            String direction,
            ProjectCategory category,
            Boolean featured,
            String keyword) {

        Pageable pageable = createPageable(page, size, sortBy, direction);

        Specification<Project> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    if (category != null) {
                        predicates.add(
                                criteriaBuilder.equal(root.get("category"), category)
                        );
                    }

                    if (featured != null) {
                        predicates.add(
                                criteriaBuilder.equal(root.get("featured"), featured)
                        );
                    }

                    if (keyword != null && !keyword.isBlank()) {

                        String searchTerm =
                                "%" + keyword.trim().toLowerCase() + "%";

                        Predicate nameMatch = criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                searchTerm
                        );

                        Predicate descriptionMatch = criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                searchTerm
                        );

                        Predicate problemMatch = criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("problemSolved")),
                                searchTerm
                        );

                        predicates.add(
                                criteriaBuilder.or(
                                        nameMatch,
                                        descriptionMatch,
                                        problemMatch
                                )
                        );
                    }

                    return criteriaBuilder.and(
                            predicates.toArray(new Predicate[0])
                    );
                };

        return projectRepository.findAll(specification, pageable)
                .map(this::toResponse);
    }

    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = findProjectOrThrow(id);

        return toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(
            Long id,
            ProjectRequest request) {

        Project existingProject = findProjectOrThrow(id);

        String normalizedName = normalizeName(request.name());

        if (projectRepository.existsByNameIgnoreCaseAndIdNot(
                normalizedName, id)) {

            throw new DuplicateProjectException(
                    "Another project already uses this name: " + normalizedName
            );
        }

        copyRequestToEntity(request, existingProject);

        Project updatedProject = projectRepository.save(existingProject);

        return toResponse(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {

        Project project = findProjectOrThrow(id);

        projectRepository.delete(project);
    }

    private Project findProjectOrThrow(Long id) {

        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with ID: " + id
                        )
                );
    }

    private Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed fields: "
                            + ALLOWED_SORT_FIELDS
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new IllegalArgumentException(
                    "Direction must be ASC or DESC"
            );
        }

        Sort sort = Sort.by(sortDirection, sortBy);

        return PageRequest.of(page, size, sort);
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    private void copyRequestToEntity(
            ProjectRequest request,
            Project project) {

        project.setName(normalizeName(request.name()));
        project.setDescription(request.description().trim());

        project.setProblemSolved(
                request.problemSolved() == null
                        ? null
                        : request.problemSolved().trim()
        );

        List<String> normalizedTechnologies = request.technologies()
                .stream()
                .map(String::trim)
                .distinct()
                .toList();

        project.setTechnologies(normalizedTechnologies);
        project.setGithubUrl(normalizeOptionalValue(request.githubUrl()));
        project.setLiveDemoUrl(normalizeOptionalValue(request.liveDemoUrl()));
        project.setCategory(request.category());
        project.setFeatured(request.featured());
        project.setDisplayOrder(request.displayOrder());
    }

    private String normalizeOptionalValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private ProjectResponse toResponse(Project project) {

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getProblemSolved(),
                List.copyOf(project.getTechnologies()),
                project.getGithubUrl(),
                project.getLiveDemoUrl(),
                project.getCategory(),
                project.isFeatured(),
                project.getDisplayOrder(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
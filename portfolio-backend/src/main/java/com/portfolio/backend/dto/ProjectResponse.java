package com.portfolio.backend.dto;

import com.portfolio.backend.enums.ProjectCategory;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String problemSolved,
        List<String> technologies,
        String githubUrl,
        String liveDemoUrl,
        ProjectCategory category,
        boolean featured,
        Integer displayOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
package com.portfolio.backend.dto;

import com.portfolio.backend.enums.ProjectCategory;
import jakarta.validation.constraints.*;

import java.util.List;

public record ProjectRequest(

        @NotBlank(message = "Project name is required")
        @Size(max = 150, message = "Project name cannot exceed 150 characters")
        String name,

        @NotBlank(message = "Project description is required")
        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,

        @Size(max = 2000, message = "Problem solved cannot exceed 2000 characters")
        String problemSolved,

        @NotEmpty(message = "At least one technology is required")
        @Size(max = 30, message = "A maximum of 30 technologies is allowed")
        List<
                @NotBlank(message = "Technology name cannot be blank")
                @Size(max = 100, message = "Technology name cannot exceed 100 characters")
                        String
                > technologies,

        @Size(max = 500, message = "GitHub URL cannot exceed 500 characters")
        String githubUrl,

        @Size(max = 500, message = "Live demo URL cannot exceed 500 characters")
        String liveDemoUrl,

        @NotNull(message = "Project category is required")
        ProjectCategory category,

        boolean featured,

        @NotNull(message = "Display order is required")
        @PositiveOrZero(message = "Display order cannot be negative")
        Integer displayOrder

) {
}
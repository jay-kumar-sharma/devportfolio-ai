package com.portfolio.backend.dto;

import com.portfolio.backend.enums.SkillCategory;
import com.portfolio.backend.enums.SkillProficiency;
import jakarta.validation.constraints.*;

public record SkillRequest(

        @NotBlank(message = "Skill name is required")
        @Size(max = 100, message = "Skill name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Skill category is required")
        SkillCategory category,

        @NotNull(message = "Skill proficiency is required")
        SkillProficiency proficiency,

        @NotNull(message = "Years of experience is required")
        @Min(value = 0, message = "Years of experience cannot be negative")
        @Max(value = 60, message = "Years of experience cannot exceed 60")
        Integer yearsOfExperience,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Display order is required")
        @Min(value = 0, message = "Display order cannot be negative")
        Integer displayOrder
) {
}
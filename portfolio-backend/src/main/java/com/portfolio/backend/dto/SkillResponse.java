package com.portfolio.backend.dto;

import com.portfolio.backend.enums.SkillCategory;
import com.portfolio.backend.enums.SkillProficiency;

import java.time.LocalDateTime;

public record SkillResponse(
        Long id,
        String name,
        SkillCategory category,
        SkillProficiency proficiency,
        Integer yearsOfExperience,
        String description,
        Integer displayOrder,
        LocalDateTime createdAt
) {
}
package com.portfolio.backend.service;

import com.portfolio.backend.dto.SkillRequest;
import com.portfolio.backend.dto.SkillResponse;
import com.portfolio.backend.enums.SkillCategory;

import org.springframework.data.domain.Page;

public interface SkillService {

    SkillResponse createSkill(SkillRequest request);

    Page<SkillResponse> getAllSkills(int page, int size, String sortBy);

    Page<SkillResponse> getSkillsByCategory(
            SkillCategory category,
            int page,
            int size,
            String sortBy
    );

    SkillResponse getSkillById(Long id);

    SkillResponse updateSkill(Long id, SkillRequest request);

    void deleteSkill(Long id);
}
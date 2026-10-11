package com.portfolio.backend.repository;

import com.portfolio.backend.entity.Skill;
import com.portfolio.backend.enums.SkillCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsByNameIgnoreCaseAndCategory(
            String name,
            SkillCategory category
    );

    boolean existsByNameIgnoreCaseAndCategoryAndIdNot(
            String name,
            SkillCategory category,
            Long id
    );

    Optional<Skill> findByNameIgnoreCaseAndCategory(
            String name,
            SkillCategory category
    );

    Page<Skill> findByCategory(
            SkillCategory category,
            Pageable pageable
    );
}
package com.portfolio.backend.service.impl;

import com.portfolio.backend.dto.SkillRequest;
import com.portfolio.backend.dto.SkillResponse;
import com.portfolio.backend.entity.Skill;
import com.portfolio.backend.enums.SkillCategory;
import com.portfolio.backend.exception.DuplicateSkillException;
import com.portfolio.backend.exception.SkillNotFoundException;
import com.portfolio.backend.repository.SkillRepository;
import com.portfolio.backend.service.SkillService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class SkillServiceImpl implements SkillService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "category",
            "proficiency",
            "yearsOfExperience",
            "displayOrder",
            "createdAt"
    );

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional
    public SkillResponse createSkill(SkillRequest request) {

        if (skillRepository.existsByNameIgnoreCaseAndCategory(
                request.name(), request.category())) {

            throw new DuplicateSkillException(
                    "Skill already exists in category: "
                            + request.category()
            );
        }

        Skill skill = new Skill();

        copyRequestToEntity(request, skill);

        Skill savedSkill = skillRepository.save(skill);

        return toResponse(savedSkill);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SkillResponse> getAllSkills(
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = createPageable(page, size, sortBy);

        return skillRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SkillResponse> getSkillsByCategory(
            SkillCategory category,
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = createPageable(page, size, sortBy);

        return skillRepository.findByCategory(category, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SkillResponse getSkillById(Long id) {

        return toResponse(getSkillOrThrow(id));
    }

    @Override
    @Transactional
    public SkillResponse updateSkill(
            Long id,
            SkillRequest request
    ) {

        Skill skill = getSkillOrThrow(id);

        boolean duplicateExists =
                skillRepository.existsByNameIgnoreCaseAndCategoryAndIdNot(
                        request.name(),
                        request.category(),
                        id
                );

        if (duplicateExists) {
            throw new DuplicateSkillException(
                    "Another skill already exists in category: "
                            + request.category()
            );
        }

        copyRequestToEntity(request, skill);

        Skill updatedSkill = skillRepository.save(skill);

        return toResponse(updatedSkill);
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {

        Skill skill = getSkillOrThrow(id);

        skillRepository.delete(skill);
    }

    private Skill getSkillOrThrow(Long id) {

        return skillRepository.findById(id)
                .orElseThrow(() ->
                        new SkillNotFoundException(
                                "Skill not found with id: " + id
                        )
                );
    }

    private Pageable createPageable(
            int page,
            int size,
            String sortBy
    ) {

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

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported sort field: " + sortBy
            );
        }

        Sort sort = Sort.by(
                Sort.Direction.ASC,
                sortBy
        );

        return PageRequest.of(page, size, sort);
    }

    private void copyRequestToEntity(
            SkillRequest request,
            Skill skill
    ) {

        skill.setName(request.name().trim());
        skill.setCategory(request.category());
        skill.setProficiency(request.proficiency());
        skill.setYearsOfExperience(request.yearsOfExperience());
        skill.setDescription(request.description());
        skill.setDisplayOrder(request.displayOrder());
    }

    private SkillResponse toResponse(Skill skill) {

        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                skill.getProficiency(),
                skill.getYearsOfExperience(),
                skill.getDescription(),
                skill.getDisplayOrder(),
                skill.getCreatedAt()
        );
    }
}
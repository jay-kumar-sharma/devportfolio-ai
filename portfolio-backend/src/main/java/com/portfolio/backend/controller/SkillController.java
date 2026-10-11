package com.portfolio.backend.controller;

import com.portfolio.backend.dto.SkillRequest;
import com.portfolio.backend.dto.SkillResponse;
import com.portfolio.backend.enums.SkillCategory;
import com.portfolio.backend.service.SkillService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> createSkill(
            @Valid @RequestBody SkillRequest request) {

        SkillResponse response = skillService.createSkill(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<SkillResponse>> getAllSkills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy) {

        return ResponseEntity.ok(
                skillService.getAllSkills(page, size, sortBy)
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<SkillResponse>> getSkillsByCategory(
            @PathVariable SkillCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy) {

        return ResponseEntity.ok(
                skillService.getSkillsByCategory(
                        category,
                        page,
                        size,
                        sortBy
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> getSkillById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                skillService.getSkillById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequest request) {

        return ResponseEntity.ok(
                skillService.updateSkill(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(
            @PathVariable Long id) {

        skillService.deleteSkill(id);

        return ResponseEntity.noContent().build();
    }
}
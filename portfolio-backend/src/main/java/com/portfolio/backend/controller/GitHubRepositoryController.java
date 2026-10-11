package com.portfolio.backend.controller;

import com.portfolio.backend.dto.GitHubRepositoryResponse;
import com.portfolio.backend.service.GitHubRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubRepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;

    @GetMapping("/repositories")
    public List<GitHubRepositoryResponse> getPublicRepositories() {
        return gitHubRepositoryService.getPublicRepositories();
    }
}
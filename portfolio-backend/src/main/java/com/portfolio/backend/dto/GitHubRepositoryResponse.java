package com.portfolio.backend.dto;

import java.time.Instant;
import java.util.List;

public record GitHubRepositoryResponse(
        Long id,
        String name,
        String fullName,
        String description,
        String htmlUrl,
        String homepage,
        String language,
        List<String> topics,
        boolean fork,
        boolean archived,
        boolean privateRepository,
        Instant createdAt,
        Instant updatedAt,
        Instant pushedAt,
        String defaultBranch,
        Integer stargazersCount
) {
}
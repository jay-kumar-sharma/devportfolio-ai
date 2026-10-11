package com.portfolio.backend.dto;

import java.time.LocalDateTime;

public record ProfileResponse(
        Long id,
        String fullName,
        String headline,
        String summary,
        String email,
        String phoneNumber,
        String location,
        String githubUrl,
        String linkedinUrl,
        String profileImageUrl,
        String yearsOfExperience,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
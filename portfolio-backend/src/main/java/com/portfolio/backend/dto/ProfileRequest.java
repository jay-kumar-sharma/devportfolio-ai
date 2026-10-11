package com.portfolio.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 150, message = "Full name cannot exceed 150 characters")
        String fullName,

        @NotBlank(message = "Headline is required")
        @Size(max = 200, message = "Headline cannot exceed 200 characters")
        String headline,

        @NotBlank(message = "Summary is required")
        @Size(max = 2000, message = "Summary cannot exceed 2000 characters")
        String summary,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,

        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        String phoneNumber,

        @Size(max = 150, message = "Location cannot exceed 150 characters")
        String location,

        @Size(max = 500, message = "GitHub URL cannot exceed 500 characters")
        String githubUrl,

        @Size(max = 500, message = "LinkedIn URL cannot exceed 500 characters")
        String linkedinUrl,

        @Size(max = 500, message = "Profile image URL cannot exceed 500 characters")
        String profileImageUrl,

        @Size(max = 50, message = "Experience cannot exceed 50 characters")
        String yearsOfExperience

) {
}
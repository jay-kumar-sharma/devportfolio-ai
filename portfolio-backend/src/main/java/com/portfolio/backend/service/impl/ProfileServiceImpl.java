package com.portfolio.backend.service.impl;

import com.portfolio.backend.dto.ProfileRequest;
import com.portfolio.backend.dto.ProfileResponse;
import com.portfolio.backend.entity.Profile;
import com.portfolio.backend.exception.DuplicateEmailException;
import com.portfolio.backend.exception.ProfileNotFoundException;
import com.portfolio.backend.repository.ProfileRepository;
import com.portfolio.backend.service.ProfileService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public ProfileResponse createProfile(ProfileRequest request) {

        String email = request.email().trim();

        if (profileRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(
                    "A profile already exists with email: " + email
            );
        }

        Profile profile = new Profile();

        copyRequestToEntity(request, profile);

        Profile savedProfile = profileRepository.save(profile);

        return toResponse(savedProfile);
    }

    @Override
    public List<ProfileResponse> getAllProfiles() {

        return profileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProfileResponse getProfileById(Long id) {

        Profile profile = findProfileOrThrow(id);

        return toResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(
            Long id,
            ProfileRequest request) {

        Profile existingProfile = findProfileOrThrow(id);

        String email = request.email().trim();

        if (profileRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateEmailException(
                    "Another profile already uses email: " + email
            );
        }

        copyRequestToEntity(request, existingProfile);

        Profile updatedProfile =
                profileRepository.save(existingProfile);

        return toResponse(updatedProfile);
    }

    @Override
    @Transactional
    public void deleteProfile(Long id) {

        Profile profile = findProfileOrThrow(id);

        profileRepository.delete(profile);
    }

    private Profile findProfileOrThrow(Long id) {

        return profileRepository.findById(id)
                .orElseThrow(() ->
                        new ProfileNotFoundException(
                                "Profile not found with ID: " + id
                        )
                );
    }

    private void copyRequestToEntity(
            ProfileRequest request,
            Profile profile) {

        profile.setFullName(request.fullName().trim());
        profile.setHeadline(request.headline().trim());
        profile.setSummary(request.summary().trim());
        profile.setEmail(request.email().trim());

        profile.setPhoneNumber(
                normalizeOptionalValue(request.phoneNumber())
        );

        profile.setLocation(
                normalizeOptionalValue(request.location())
        );

        profile.setGithubUrl(
                normalizeOptionalValue(request.githubUrl())
        );

        profile.setLinkedinUrl(
                normalizeOptionalValue(request.linkedinUrl())
        );

        profile.setProfileImageUrl(
                normalizeOptionalValue(request.profileImageUrl())
        );

        profile.setYearsOfExperience(
                normalizeOptionalValue(request.yearsOfExperience())
        );
    }

    private String normalizeOptionalValue(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private ProfileResponse toResponse(Profile profile) {

        return new ProfileResponse(
                profile.getId(),
                profile.getFullName(),
                profile.getHeadline(),
                profile.getSummary(),
                profile.getEmail(),
                profile.getPhoneNumber(),
                profile.getLocation(),
                profile.getGithubUrl(),
                profile.getLinkedinUrl(),
                profile.getProfileImageUrl(),
                profile.getYearsOfExperience(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
package com.portfolio.backend.service;

import com.portfolio.backend.dto.ProfileRequest;
import com.portfolio.backend.dto.ProfileResponse;

import java.util.List;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest request);

    List<ProfileResponse> getAllProfiles();

    ProfileResponse getProfileById(Long id);

    ProfileResponse updateProfile(Long id, ProfileRequest request);

    void deleteProfile(Long id);
}
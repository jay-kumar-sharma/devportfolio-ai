import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import {
  Profile,
  ProfileService
} from '../../core/services/profile.service';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent implements OnInit {

  profile: Profile | null = null;
  loading = true;
  errorMessage = '';

  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;
    this.errorMessage = '';

    this.profileService.getAllProfiles().subscribe({
      next: (profiles) => {
        this.profile = profiles.length > 0 ? profiles[0] : null;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load profile:', error);
        this.errorMessage = 'Unable to load profile information.';
        this.loading = false;
      }
    });
  }

  onProfileImageError(): void {
    if (this.profile) {
      this.profile.profileImageUrl = '';
    }
  }
}
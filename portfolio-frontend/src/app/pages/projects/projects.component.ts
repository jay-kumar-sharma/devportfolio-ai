import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GitHubRepositoryService } from '../../models/github-repository.service';
import type { GitHubRepository } from '../../models/github-repository.model';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css'
})
export class ProjectsComponent implements OnInit {

  repositories: GitHubRepository[] = [];

  isLoading = true;
  errorMessage = '';

  constructor(
    private readonly githubRepositoryService: GitHubRepositoryService
  ) {}

  ngOnInit(): void {
    this.loadRepositories();
  }

  loadRepositories(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.githubRepositoryService.getRepositories().subscribe({
      next: (repositories: GitHubRepository[]) => {
        this.repositories = repositories;
        this.isLoading = false;
      },

      error: (error: unknown) => {
        console.error(
          'Failed to load GitHub repositories:',
          error
        );

        this.errorMessage =
          'Unable to load projects right now. Please try again later.';

        this.isLoading = false;
      }
    });
  }

  getRepositoryDescription(
    repository: GitHubRepository
  ): string {
    return repository.description?.trim()
      || 'Explore this repository to learn more about the project.';
  }

  getLastUpdated(date: string): string {
    if (!date) {
      return 'Unknown';
    }

    const parsedDate = new Date(date);

    if (Number.isNaN(parsedDate.getTime())) {
      return 'Unknown';
    }

    return parsedDate.toLocaleDateString('en-IN', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    });
  }

  trackByRepositoryId(
    index: number,
    repository: GitHubRepository
  ): number {
    return repository.id;
  }
}
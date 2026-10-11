import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import type { GitHubRepository } from '../models/github-repository.model';

interface GitHubApiRepository {
  id: number;
  name: string;
  full_name: string;
  description: string | null;
  html_url: string;
  homepage: string | null;
  language: string | null;
  topics?: string[];
  fork: boolean;
  archived: boolean;
  private: boolean;
  created_at: string;
  updated_at: string;
  pushed_at: string;
  default_branch: string;
  stargazers_count: number;
}

@Injectable({
  providedIn: 'root'
})
export class GitHubRepositoryService {

  // The Java backend is not deployed yet, so repositories are
  // loaded directly from GitHub's public API in the browser.
  private readonly apiUrl =
    'https://api.github.com/users/jay-kumar-sharma/repos?per_page=100&sort=updated';

  private readonly headers = new HttpHeaders({
    Accept: 'application/vnd.github+json'
  });

  constructor(
    private readonly http: HttpClient
  ) {}

  getRepositories(): Observable<GitHubRepository[]> {
    return this.http
      .get<GitHubApiRepository[]>(this.apiUrl, { headers: this.headers })
      .pipe(
        map((repositories) => repositories.map((repository) => ({
          id: repository.id,
          name: repository.name,
          fullName: repository.full_name,
          description: repository.description,
          htmlUrl: repository.html_url,
          homepage: repository.homepage || null,
          language: repository.language,
          topics: repository.topics ?? [],
          fork: repository.fork,
          archived: repository.archived,
          privateRepository: repository.private,
          createdAt: repository.created_at,
          updatedAt: repository.updated_at,
          pushedAt: repository.pushed_at,
          defaultBranch: repository.default_branch,
          stargazersCount: repository.stargazers_count
        })))
      );
  }
}

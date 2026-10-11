import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import type { GitHubRepository } from '../models/github-repository.model';

@Injectable({
  providedIn: 'root'
})
export class GitHubRepositoryService {

  private readonly apiUrl =
    'http://localhost:9090/api/github/repositories';

  constructor(
    private readonly http: HttpClient
  ) {}

  getRepositories(): Observable<GitHubRepository[]> {
    return this.http.get<GitHubRepository[]>(this.apiUrl);
  }
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Profile {
  id: number;
  fullName: string;
  headline: string;
  summary: string;
  email: string;
  phoneNumber: string;
  location: string;
  githubUrl: string;
  linkedinUrl: string;
  profileImageUrl: string;
  yearsOfExperience: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private readonly apiUrl = 'http://localhost:9090/api/profile';

  constructor(private http: HttpClient) {}

  getAllProfiles(): Observable<Profile[]> {
    return this.http.get<Profile[]>(this.apiUrl);
  }

  getProfileById(id: number): Observable<Profile> {
    return this.http.get<Profile>(`${this.apiUrl}/${id}`);
  }
}
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  fullName = 'Jay Kumar Sharma';

  headline =
    'Java Full Stack Developer | Spring Boot | Microservices';

  introduction =
    'I build full-stack applications and scalable backend services using Java, Spring Boot, REST APIs, and modern frontend technologies. I am interested in AI-powered applications and building reliable, maintainable software.';

  focusAreas: string[] = [
    'Backend Engineering',
    'Full Stack Development',
    'Microservices',
    'AI-Powered Applications'
  ];

}
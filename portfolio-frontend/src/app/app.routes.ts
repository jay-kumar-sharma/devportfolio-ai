import { Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { AboutComponent } from './pages/about/about.component';
import { SkillsComponent } from './pages/skills/skills.component';
import { ProjectsComponent } from './pages/projects/projects.component';
import { ContactComponent } from './pages/contact/contact.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'Home | Portfolio'
  },
  {
    path: 'about',
    component: AboutComponent,
    title: 'About | Portfolio'
  },
  {
    path: 'skills',
    component: SkillsComponent,
    title: 'Skills | Portfolio'
  },
  {
    path: 'projects',
    component: ProjectsComponent,
    title: 'Projects | Portfolio'
  },
  {
    path: 'contact',
    component: ContactComponent,
    title: 'Contact | Portfolio'
  },
  {
    path: '**',
    redirectTo: ''
  }
];
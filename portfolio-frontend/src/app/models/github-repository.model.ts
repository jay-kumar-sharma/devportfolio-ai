export interface GitHubRepository {
   id: number;
   name: string;
   fullName: string;
   description: string | null;
   htmlUrl: string;
   homepage: string | null;
   language: string | null;
   topics: string[];
   fork: boolean;
   archived: boolean;
   privateRepository: boolean;
   createdAt: string;
   updatedAt: string;
   pushedAt: string;
   defaultBranch: string;
   stargazersCount: number;
 }
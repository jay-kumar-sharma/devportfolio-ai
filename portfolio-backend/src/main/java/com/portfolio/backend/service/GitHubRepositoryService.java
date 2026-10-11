package com.portfolio.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.backend.dto.GitHubRepositoryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubRepositoryService {

    private static final String GITHUB_USERNAME = "jay-kumar-sharma";

    private static final String GITHUB_API_URL =
            "https://api.github.com/users/" + GITHUB_USERNAME + "/repos";

    // Cache repositories for 30 minutes.
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Cached repository data.
    private List<GitHubRepositoryResponse> cachedRepositories;

    // Time when the cache was last successfully updated.
    private Instant cacheUpdatedAt;

    public GitHubRepositoryService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Returns cached repositories when available and fresh.
     * Otherwise, fetches repositories from GitHub.
     *
     * If GitHub fails but cached data exists, the cached
     * repositories are returned as a fallback.
     */
    public synchronized List<GitHubRepositoryResponse> getPublicRepositories() {

        // 1. Return cached data if it is still fresh.
        if (isCacheValid()) {
            System.out.println("Returning GitHub repositories from cache.");
            return new ArrayList<>(cachedRepositories);
        }

        // 2. Fetch fresh data from GitHub.
        try {
            List<GitHubRepositoryResponse> repositories =
                    fetchRepositoriesFromGitHub();

            // Update the cache only after a successful fetch.
            cachedRepositories = new ArrayList<>(repositories);
            cacheUpdatedAt = Instant.now();

            System.out.println(
                    "GitHub repositories fetched successfully. Cached for 30 minutes."
            );

            return new ArrayList<>(cachedRepositories);

        } catch (RuntimeException exception) {

            // 3. If GitHub fails, serve previously cached data if available.
            if (cachedRepositories != null && !cachedRepositories.isEmpty()) {

                System.err.println(
                        "GitHub API request failed. Returning stale cached repositories."
                );

                System.err.println(
                        "Reason: " + exception.getMessage()
                );

                return new ArrayList<>(cachedRepositories);
            }

            // No cache exists, so the request cannot be fulfilled.
            throw exception;
        }
    }

    /**
     * Checks whether the cache exists and is less than 30 minutes old.
     */
    private boolean isCacheValid() {
        return cachedRepositories != null
                && cacheUpdatedAt != null
                && Instant.now().isBefore(cacheUpdatedAt.plus(CACHE_TTL));
    }

    /**
     * Fetches all public repositories from GitHub.
     * Existing pagination and JSON mapping are preserved.
     */
    private List<GitHubRepositoryResponse> fetchRepositoriesFromGitHub() {

        List<GitHubRepositoryResponse> repositories = new ArrayList<>();

        int page = 1;
        int perPage = 100;

        while (true) {

            String url = GITHUB_API_URL
                    + "?per_page=" + perPage
                    + "&page=" + page
                    + "&sort=updated";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/vnd.github+json")
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .header("User-Agent", "DevPortfolio-AI")
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() != 200) {

                    System.err.println(
                            "GitHub API status: " + response.statusCode()
                    );

                    System.err.println(
                            "GitHub API response body: " + response.body()
                    );

                    System.err.println(
                            "GitHub API rate limit remaining: "
                                    + response.headers()
                                    .firstValue("X-RateLimit-Remaining")
                                    .orElse("unknown")
                    );

                    System.err.println(
                            "GitHub API rate limit reset: "
                                    + response.headers()
                                    .firstValue("X-RateLimit-Reset")
                                    .orElse("unknown")
                    );

                    throw new IllegalStateException(
                            "GitHub API returned HTTP " + response.statusCode()
                    );
                }

                JsonNode root = objectMapper.readTree(response.body());

                if (!root.isArray()) {
                    throw new IllegalStateException(
                            "GitHub API returned an unexpected response format."
                    );
                }

                if (root.isEmpty()) {
                    break;
                }

                for (JsonNode repository : root) {
                    repositories.add(mapRepository(repository));
                }

                // A full page means another page may exist.
                if (root.size() < perPage) {
                    break;
                }

                page++;

            } catch (IOException exception) {
                throw new IllegalStateException(
                        "Unable to read the GitHub API response.",
                        exception
                );

            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();

                throw new IllegalStateException(
                        "GitHub repository retrieval was interrupted.",
                        exception
                );
            }
        }

        return repositories;
    }

    private GitHubRepositoryResponse mapRepository(JsonNode repository) {

        return new GitHubRepositoryResponse(
                repository.path("id").asLong(),
                repository.path("name").asText(),
                repository.path("full_name").asText(),
                getNullableText(repository, "description"),
                repository.path("html_url").asText(),
                getNullableText(repository, "homepage"),
                getNullableText(repository, "language"),
                extractTopics(repository.path("topics")),
                repository.path("fork").asBoolean(),
                repository.path("archived").asBoolean(),
                repository.path("private").asBoolean(),
                getInstant(repository, "created_at"),
                getInstant(repository, "updated_at"),
                getInstant(repository, "pushed_at"),
                repository.path("default_branch").asText(),
                repository.path("stargazers_count").asInt()
        );
    }

    private String getNullableText(JsonNode node, String field) {

        JsonNode value = node.get(field);

        if (value == null || value.isNull()) {
            return null;
        }

        return value.asText();
    }

    private Instant getInstant(JsonNode node, String field) {

        String value = getNullableText(node, field);

        return value == null ? null : Instant.parse(value);
    }

    private List<String> extractTopics(JsonNode topicsNode) {

        List<String> topics = new ArrayList<>();

        if (topicsNode != null && topicsNode.isArray()) {
            for (JsonNode topic : topicsNode) {
                topics.add(topic.asText());
            }
        }

        return topics;
    }
}
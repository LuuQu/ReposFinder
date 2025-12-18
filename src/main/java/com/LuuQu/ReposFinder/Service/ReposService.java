package com.LuuQu.ReposFinder.Service;

import com.LuuQu.ReposFinder.client.GithubClient;
import com.LuuQu.ReposFinder.model.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReposService {
    private final GithubClient githubClient;

    public List<Repo> getUserRepos(String username) {

        List<Repo> repos = githubClient.getRepos(username).stream()
                .filter(repo -> !repo.isFork())
                .toList();

        for(Repo repo : repos) {
            repo.setBranches(githubClient.getBranches(username, repo.getName()));
        }

        return repos;
    }
}

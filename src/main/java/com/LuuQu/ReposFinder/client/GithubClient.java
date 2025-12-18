package com.LuuQu.ReposFinder.client;

import com.LuuQu.ReposFinder.model.Branch;
import com.LuuQu.ReposFinder.model.Repo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "githubClient")
public interface GithubClient {

    @GetMapping("/users/{username}/repos")
    List<Repo> getRepos(@PathVariable String username);

    @GetMapping("/repos/{username}/{repoName}/branches")
    List<Branch> getBranches(@PathVariable String username, @PathVariable String repoName);
}

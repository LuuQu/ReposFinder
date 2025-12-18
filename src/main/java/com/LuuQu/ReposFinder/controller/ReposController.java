package com.LuuQu.ReposFinder.controller;

import com.LuuQu.ReposFinder.Service.ReposService;
import com.LuuQu.ReposFinder.model.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "repos")
public class ReposController {
    private final ReposService reposService;

    @GetMapping("/{username}")
    public List<Repo> getUserRepos(@PathVariable String username) {
        return reposService.getUserRepos(username);
    }
}

package com.LuuQu.ReposFinder.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Repo {
    private final String name;
    @Getter(onMethod_ = @JsonIgnore)
    private final boolean fork;
    private final String ownerLogin;
    @Setter
    private List<Branch> branches = new ArrayList<>();

    @JsonCreator
    public Repo(
            @JsonProperty("name") String name,
            @JsonProperty("owner") JsonNode owner,
            @JsonProperty("fork") boolean fork
    ) {
        this.name = name;
        this.fork = fork;
        this.ownerLogin = owner != null && owner.has("login") ? owner.get("login").asString() : null;
    }

}

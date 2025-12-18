package com.LuuQu.ReposFinder.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Repo {
    private final String name;
    private final String ownerLogin;
    @Setter
    private List<Branch> branches = new ArrayList<>();

    @JsonCreator
    public Repo(
            @JsonProperty("name") String name,
            @JsonProperty("owner") JsonNode owner
            ) {
        this.name = name;
        this.ownerLogin = owner != null && owner.has("login") ? owner.get("login").asString() : null;
    }

}

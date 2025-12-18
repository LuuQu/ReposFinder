package com.LuuQu.ReposFinder.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import tools.jackson.databind.JsonNode;

@Getter
public class Branch {
    private final String name;
    private final String sha;

    @JsonCreator
    public Branch(
            @JsonProperty("name") String name,
            @JsonProperty("commit") JsonNode commit
            ) {
        this.name = name;
        this.sha = commit != null && commit.has("sha") ? commit.get("sha").asString() : null;
    }
}

package com.LuuQu.ReposFinder.service;

import com.LuuQu.ReposFinder.Service.ReposService;
import com.LuuQu.ReposFinder.model.Repo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest(httpPort = 8181)
public class ReposServiceTest {

    @Autowired
    private ReposService reposService;

    @ParameterizedTest
    @MethodSource("reposServiceTestData")
    void reposServiceTest(TestCase testCase) {
        String username = "abcd";
        stubFor(get(urlEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withStatus(testCase.status)
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(testCase.responseBody)));
        stubFor(get(urlEqualTo("/repos/" + username + "/repo/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(testCase.expectedBranches ? branchContent : "[]")));
        if (testCase.status != 200) {
            FeignException feignException = assertThrows(FeignException.class, () -> reposService.getUserRepos(username));
            assertEquals(testCase.responseBody == null ? "" : testCase.responseBody.toString(), feignException.contentUTF8());
            assertEquals(testCase.status, feignException.status());
            return;
        }
        List<Repo> repos = reposService.getUserRepos(username);
        assertEquals(repos.size(), testCase.isForked ? 0 : testCase.requestSize);
        if (testCase.expectedBranches) {
            assertFalse(repos.stream().flatMap(repo -> repo.getBranches().stream()).toList().isEmpty());
        } else {
            assertTrue(repos.stream().flatMap(repo -> repo.getBranches().stream()).toList().isEmpty());
        }
    }

    static Stream<TestCase> reposServiceTestData() throws JsonProcessingException {
        return Stream.of(
                new TestCase(200, 5, true, false), //full response
                new TestCase(200, 5, false, false), //response with repos without branches
                new TestCase(200, 5, false, true), //only forked repos
                new TestCase(200, 0, false, false), //user without repos
                new TestCase(404, 0, false, false) //user not found
        );
    }

    static class TestCase {
        int status;
        JsonNode responseBody;
        int requestSize;
        boolean expectedBranches;
        boolean isForked;

        TestCase(int status, int requestSize, boolean expectedBranches, boolean isForked) throws JsonProcessingException {
            this.status = status;
            this.requestSize = requestSize;
            this.expectedBranches = expectedBranches;
            this.isForked = isForked;
            setResponseBody();
        }

        private void setResponseBody() throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            if (this.status != 200) {
                if (this.status == 404) {
                    responseBody = objectMapper.readTree("{\"message\":\"Not Found\"}");
                    return;
                }
                responseBody = null;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            if (requestSize > 0) {
                stringBuilder.append(isForked ? requestWithFork : requestWithoutFork);
            }
            for (int i = 1; i < requestSize; i++) {
                stringBuilder.append(",");
                stringBuilder.append(isForked ? requestWithFork : requestWithoutFork);
            }
            stringBuilder.append("]");
            responseBody = objectMapper.readTree(stringBuilder.toString());
        }
    }

    static String requestWithoutFork = """
            {
                "name" : "repo",
                "owner" : {
                    "login": "abcd"
                },
                "fork": false
            }
            """;
    static String requestWithFork = """
            {
                "name" : "repo",
                "owner" : {
                    "login": "abcd"
                },
                "fork": true
            }
            """;
    static String branchContent = """
            [{
                "name": "branch",
                "commit": {
                    "sha": "sha"
                }
            }]
            """;
}

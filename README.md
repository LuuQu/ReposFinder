# Repos Finder
> A backend application integrating with the GitHub API to return non-fork repositories and their branch details for a given user.

## Table of Contents
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)

## Technologies Used
- Java 25
- Spring Boot 4.0
  - Spring Cloud OpenFeign
  - WireMock
  - Lombok
- GitHub REST API


## Features
- Fetches public GitHub repositories for a given user
- Filters out forked repositories
- Retrieves branches and latest commit SHA for each repository
- Handles GitHub user not found errors

## Setup
### 1. Clone the repository
Start by cloning the project and moving into its directory:
```bash
git clone https://github.com/LuuQu/ReposFinder.git
```
### 2. Run the application
**Using an IDE** <br>
Open the project in your IDE and run the main class to start Spring Boot application.

**Using the command line** <br>
You can also start the application without and IDE by running [OpenShift CLI](https://docs.openshift.org/latest/cli_reference/index.html).
With the console still open from step 1, go to the application folder:
```bash
cd ReposFinder
```
Next, run the following command:
```bash
mvn spring-boot:run
```
After starting, the API will be accessible at [http://localhost:8080](http://localhost:8080)

## Usage
Once the application is running, you can fetch a user's repositories with endpoint:
```http
GET http://localhost:8080/repos/{username}
```
Where you replace **{username}** with any GitHub username. For example:
```http
GET http://localhost:8080/repos/LuuQu
```
### Example response
**200 OK**
```json
[
  {
    "name": "Project name",
    "branches": [
      {
        "name": "main",
        "sha": "abc123"
      }
    ],
    "ownerLogin": "LuuQu"
  }
]
```
**404 NOT FOUND**
```json
{
  "status": 404,
  "message": "Not Found"
}
```

### Note 
This project uses the unauthenticated GitHub API, which has a rate limit of 60 requests per hour.
Each request to this API uses `(number of repositories) + 1` GitHub API calls, 
so exceeding the limit may cause the application to stop responding temporarily.


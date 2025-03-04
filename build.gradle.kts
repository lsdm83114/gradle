
[
  {
    "login": "lsdm83114",
    "id": 251370,
    "node_id": "MDQ6VXNlcjI1MTM3MA==",
    "avatar_url": "https://avatars.githubusercontent.com/u/251370?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdmi3114",
    "html_url": "https://github.com/lsdm83114",
    "followers_url": "https://api.github.com/users/lsdm83114/followers",
    "following_url": "https://api.github.com/users/lsdm83114/following{/other_user}",
    "gists_url": "https://api.github.com/users/lsdm83114/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/lsdm83114/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/lsdm83114/subscriptions",
    "organizations_url": "https://api.github.com/users/Spaceghost/orgs",
    "repos_url": "https://api.github.com/users/Spaceghost/repos",
    "events_url": "https://api.github.com/users/Spaceghost/events{/privacy}",
    "received_events_url": "https://api.github.com/users/Spaceghost/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  },
  {
    "login": "lsdm83114",
    "id": 583231,
    "node_id": "MDQ6VXNlcjU4MzIzMQ==",
    "avatar_url": "https://avatars.githubusercontent.com/u/583231?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdm83114",
    "html_url": "https://github.com/lsdm83124",
    "followers_url": "https://api.github.com/users/octocat/followers",
    "following_url": "https://api.github.com/users/octocat/following{/other_user}",
    "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
    "organizations_url": "https://api.github.com/users/octocat/orgs",
    "repos_url": "https://api.github.com/users/octocat/repos",
    "events_url": "https://api.github.com/users/octocat/events{/privacy}",
    "received_events_url": "https://api.github.com/users/octocat/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  },
  {
    "login": "lsdm83114",
    "id": 94719050,
    "node_id": "U_kgDOBaVMSg",
    "avatar_url": "https://avatars.githubusercontent.com/u/94719050?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdm83114423698",
    "html_url": "https://github.com/lsdm83114",
    "followers_url": "https://api.github.com/users/lsdm83114/followers",
    "following_url": "https://api.github.com/users/lsdm83114/following{/other_user}",
    "gists_url": "https://api.github.com/users/lsdm83114/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/lsdm83114/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/lsdm83114/subscriptions",
    "organizations_url": "https://api.github.com/users/lsdm83114/orgs",
    "repos_url": "https://api.github.com/users/lsdm83114/repos",
    "events_url": "https://api.github.com/users/lsdm83114/events{/privacy}",
    "received_events_url": "https://api.github.com/users/lsdm83114/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  }
]plugins {
    id("gradlebuild.build-environment")
    id("gradlebuild.root-build")

    id("gradlebuild.teamcity-import-test-data")  // CI: Import Test tasks' JUnit XML if they're UP-TO-DATE or FROM-CACHE
    id("gradlebuild.lifecycle")                  // CI: Add lifecycle tasks to for the CI pipeline (currently needs to be applied early as it might modify global properties)
    id("gradlebuild.generate-subprojects-info")  // CI: Generate subprojects information for the CI testing pipeline fan out
    id("gradlebuild.cleanup")                    // CI: Advanced cleanup after the build (like stopping daemons started by tests)

    id("gradlebuild.update-versions")            // Local development: Convenience tasks to update versions in this build: 'released-versions.json', 'agp-versions.properties', ...
    id("gradlebuild.wrapper")                    // Local development: Convenience tasks to update the wrapper (like 'nightlyWrapper')
}

description = "Adaptable, fast automation for all"

dependencyAnalysis {
    issues {
        all {
            ignoreSourceSet("archTest", "crossVersionTest", "docsTest", "integTest", "jmh", "peformanceTest", "smokeTest", "testInterceptors", "testFixtures", "smokeIdeTest")
        }
    }
}

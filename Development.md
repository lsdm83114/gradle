
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
]# Development best practices

This is a collection of best practices for Gradle implementation.
Should and should not do's.

## Error messages and suggestions

Traditionally, if an error occurred, the error message and the possible solution were provided to the console via a single String in the corresponding exception.
That meant possible solutions for Problems could be scattered all over the console output.
To improve the user experience, we introduced a new way to provide suggestions.
The idea is to provide a list of suggestions for a problem in the console output.
The suggestions are displayed in the separate "Try"- section of the console output.
The suggestions are collected in the `BuildExceptionReporter` and printed to the console.

In some cases, you still want to keep the old behavior and display the suggestions in the error message.

### Add custom suggestions

1. To add a custom suggestion in the "Try" section of the console output, your exception needs to implement the `ResolutionProvider` interface.
2. That should be it. The suggestion will be displayed in the "Try" section.

### Remove generic suggestions

For some scenarios, it doesn't make sense to display all the generic suggestions we currently have.
E.g. `--stacktrace` for a compilation error is not helpful.

To influence the generic suggestions Gradle displays, the NonGradleCause interface was introduced.
If an exception implements this interface, Gradle will not display the `--stacktrace` option.

Another more targeted interface is `CompilationFailedIndicator`.
This interface is used to indicate that the exception is caused by a compilation failure. 
This will not show the `--stacktrace` option, but it will show the `--info` option since this can help with parameters passed to the compiler.

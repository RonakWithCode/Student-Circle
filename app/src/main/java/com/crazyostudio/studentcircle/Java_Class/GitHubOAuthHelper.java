package com.crazyostudio.studentcircle.Java_Class;

// GitHubOAuthHelper.java
public class GitHubOAuthHelper {

    public static final String CLIENT_ID = "b8735dcc0728dd0a4136";
    public static final String CLIENT_SECRET = "667b7160ab1ef4ce141054f399e354bb8a137e09";
    public static final String REDIRECT_URI = "http://localhost/callback";
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_URL = "https://api.github.com/user";
    private static final String REPOS_URL = "https://api.github.com/users/RonakWithCode/repos";
    private static final String ISSUES_URL = "https://api.github.com/issues";
    private static final String FILES_URL = "https://api.github.com/repos/%s/%s/contents";

    public static String getOAuthUrl() {
        return AUTH_URL +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=user,repo";
    }

    public static String getTokenUrl(String code) {
        return TOKEN_URL +
                "?client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&code=" + code +
                "&redirect_uri=" + REDIRECT_URI;
    }

    public static String getUserUrl() {
        return USER_URL;
    }

    public static String getReposUrl() {
        return REPOS_URL;
    }

    public static String getIssuesUrl() {
        return ISSUES_URL;
    }

    public static String getFilesUrl(String owner, String repo) {
        return String.format(FILES_URL, owner, repo);
    }
}
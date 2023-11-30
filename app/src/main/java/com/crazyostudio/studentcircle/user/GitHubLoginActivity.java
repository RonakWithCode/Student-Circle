package com.crazyostudio.studentcircle.user;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.RepoAdapter;
import com.crazyostudio.studentcircle.model.Repository;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GitHubLoginActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "your_client_id";
    private static final String CLIENT_SECRET = "your_client_secret";
    private static final String REDIRECT_URI = "your_redirect_uri";
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_URL = "https://api.github.com/user";
    private static final String REPOS_URL = "https://api.github.com/user/repos";
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_login);

        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repoAdapter = new RepoAdapter(new ArrayList<>());
        recyclerView.setAdapter(repoAdapter);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(REDIRECT_URI)) {
                    handleRedirect(url);
                    return true;
                }
                return false;
            }
        });

        // Start the GitHub OAuth process
        String authUrl = AUTH_URL +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=user,repo";
        webView.loadUrl(authUrl);











    }

    private void handleRedirect(String url) {
        Uri uri = Uri.parse(url);
        String code = uri.getQueryParameter("code");

        // Exchange the code for an access token
        exchangeCodeForToken(code);
    }

    private void exchangeCodeForToken(String code) {
        AsyncHttpClient client = new AsyncHttpClient();
        String tokenUrl = TOKEN_URL +
                "?client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&code=" + code +
                "&redirect_uri=" + REDIRECT_URI;

        client.post(tokenUrl, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the response to get the access token
                String accessToken = response.optString("access_token");
                if (accessToken != null && !accessToken.isEmpty()) {
                    // Use the access token to get user details
                    getUserDetails(accessToken);
                    // Use the access token to get user repositories
                    getUserRepositories(accessToken);

                }
            }
        });
    }

    private void getUserDetails(String accessToken) {
        AsyncHttpClient client = new AsyncHttpClient();
        String userUrl = USER_URL;
        client.addHeader("Authorization", "Bearer " + accessToken);
        client.get(userUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse and use user details
                String login = response.optString("login");
                String name = response.optString("name");
                // Handle other user details as needed
            }
        });
    }

//    private void getUserRepositories(String accessToken) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        String reposUrl = REPOS_URL;
//        client.addHeader("Authorization", "Bearer " + accessToken);
//        client.get(reposUrl, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                // Parse and use user repositories
//                for (int i = 0; i < response.length(); i++) {
//                    JSONObject repo = response.optJSONObject(i);
//                    String repoName = repo.optString("name");
//                    // Handle other repository details as needed
//                }
//            }
//        });
//    }

//    private void getUserRepositories(String accessToken) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        String reposUrl = REPOS_URL;
//        client.addHeader("Authorization", "Bearer " + accessToken);
//        client.get(reposUrl, new JsonHttpResponseHandler() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                // Parse and use user repositories
//                ArrayList<Repository> repositories = new ArrayList<>();
//                for (int i = 0; i < response.length(); i++) {
//                    JSONObject repo = response.optJSONObject(i);
//                    String repoName = repo.optString("name");
//                    String repoDescription = repo.optString("description");
//                    recyclerView.setVisibility(View.VISIBLE);
//                    webView.setVisibility(View.GONE);
//                    repositories.add(new Repository(repoName, repoDescription));
//                }
//
//                // Update the RecyclerView
//                repoAdapter.updateData(repositories);
//            }
//        });
//    }



    private void getUserRepositories(String accessToken) {
        AsyncHttpClient client = new AsyncHttpClient();
        String reposUrl = REPOS_URL;
        client.addHeader("Authorization", "Bearer " + accessToken);
        client.get(reposUrl, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse and use user repositories
                ArrayList<Repository> repositories = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject repo = response.optJSONObject(i);
                    if (repo != null) {
                        String repoName = repo.optString("name");
                        String repoDescription = repo.optString("description");
                        repositories.add(new Repository(repoName, repoDescription));
                    }
                }

                // Update the RecyclerView
                repoAdapter.updateData(repositories);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("GitHubLoginActivity", "GitHub API Request failed with status code: " + statusCode);
                if (errorResponse != null) {
                    Log.e("GitHubLoginActivity", "Error response: " + errorResponse.toString());
                }
            }
        });
    }




}
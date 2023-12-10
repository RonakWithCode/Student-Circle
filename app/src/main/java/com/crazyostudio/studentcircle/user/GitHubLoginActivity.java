package com.crazyostudio.studentcircle.user;
// GitHubLoginActivity.java

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;


public class GitHubLoginActivity extends AppCompatActivity {
//
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_login);

//        OkHttpClient client = new OkHttpClient();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repoAdapter = new RepoAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(repoAdapter);

//        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
//        getUserRepositories("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");
//        getUserRepositories("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");
        getUserRepositories();






    }



//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                String url = request.getUrl().toString();
//                if (url.startsWith(GitHubOAuthHelper.REDIRECT_URI)) {
//                    handleRedirect(url);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        String authUrl = GitHubOAuthHelper.getOAuthUrl();
//        webView.loadUrl(authUrl);
//
//
//
//    }
//
//    private void handleRedirect(String url) {
//        Uri uri = Uri.parse(url);
//        String code = uri.getQueryParameter("code");
//        exchangeCodeForToken(code);
//    }
//
//    private void exchangeCodeForToken(String code) {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        String tokenUrl = GitHubOAuthHelper.getTokenUrl(code);
//
//        client.post(tokenUrl, new JsonHttpResponseHandler() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                showProgressBar(true);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                showProgressBar(false);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // Parse the response to get the access token
//                String accessToken = response.optString("access_token");
//                if (accessToken != null && !accessToken.isEmpty()) {
//                    // Use the access token to get user details
//                    getUserDetails(accessToken);
//                    // Use the access token to get user repositories
//                    getUserRepositories(accessToken);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Toast.makeText(GitHubLoginActivity.this, "GitHub API Request failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getUserDetails(String accessToken) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        String userUrl = GitHubOAuthHelper.getUserUrl();
//        client.addHeader("Authorization", "Bearer " + accessToken);
//        client.get(userUrl, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // Parse and use user details
//                String login = response.optString("login");
//                String name = response.optString("name");
//                // Handle other user details as needed
//            }
//        });
//    }
//\


    private void getUserRepositories() {
        AsyncHttpClient client = new AsyncHttpClient();
        String reposUrl ="https://api.github.com/users/RonakWithCode/repos";
        // Add the User-Agent header

        client.addHeader("Authorization", "Bearer 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");
        client.addHeader("User-Agent", "Dev-Circle-GitHub-App");
        client.get(reposUrl, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressBar(true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                showProgressBar(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("GITMANGERERROE", "Github ERROR  statusCode : "+ statusCode);
                Log.d("GITMANGERERROE", "Github ERROR  responseString : "+ responseString);
                Log.d("GITMANGERERROE", "Github ERROR  throwable : "+ throwable);
                Log.d("GITMANGERERROE", "Github ERROR  headers : "+ Arrays.toString(headers));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse and use user repositories
                ArrayList<Repository> repositories = new ArrayList<>();
                Log.d("GITMANGERresponse", "Github response SIZE: "+ response.length());
                for (int i = 0; i < response.length(); i++) {
                    JSONObject repo = response.optJSONObject(i);
                    if (repo != null) {
                        String avatar_url = repo.optString("avatar_url");
                        String default_branch = repo.optString("default_branch");
                        String repoId = repo.optString("id");
                        String repoName = repo.optString("name");
                        String repoDescription = repo.optString("description");
                        Log.d("GITMANGER", "Github repos name : "+ repoName);
                        Log.d("GITMANGER", "Github repos Desc : "+ repoDescription);
                        repositories.add(new Repository(avatar_url,default_branch,repoId,repoName, repoDescription));
                    }
                }
                // Update the RecyclerView
                recyclerView.setVisibility(View.VISIBLE);
                repoAdapter.updateData(repositories);
            }
        });
    }




    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

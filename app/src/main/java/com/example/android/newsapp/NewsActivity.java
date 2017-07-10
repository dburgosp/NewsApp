/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.newsapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {
    // Constant value for the news loader ID.
    private static final int NEWS_LOADER_ID = 1;

    // Harcoded url for retrieving news with these filters:
    //
    // * News about Spain (q=real%20madrid).
    // * Show only news with authors (reference-type=author).
    // * Show the list of authors (show-references=author).
    // * Retrieve list of 50 news (page-size=50).
    // * Get the latest news (order-by=newest).
    private static final String url = "http://content.guardianapis.com/search?q=spain&api-key=test&reference-type=author&show-references=author&page-size=50&order-by=newest";
    // Using the ButterKnife library for view injection.
    @BindView(R.id.list)
    ListView newsListView;
    @BindView(R.id.loading_indicator)
    View loadingIndicator;
    @BindView(R.id.empty_view)
    TextView emptyStateTextView;
    // Adapter for the list of news.
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ButterKnife.bind(this);

        // Set title.
        setTitle(R.string.app_title);

        // Find a reference to the {@link ListView} in the layout.
        newsListView.setEmptyView(emptyStateTextView);

        // Create a new adapter that takes an empty list of news as input.
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView} so the list can be populated in the user
        // interface.
        newsListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Create a new intent to view the news URI and send the intent to launch a new
                // activity.
                Uri newsUri = Uri.parse(adapter.getItem(position).getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        // If there is a network connection, fetch data.
        if (isThereConnection()) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error.
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL.
        return new NewsLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded.
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous news data.
        adapter.clear();

        // Checks if there's still network connection.
        if (isThereConnection()) {
            // If there is network connection and we have retrieved a valid list of {@link News}s,
            // then add them to the adapter's data set. This will trigger the ListView to update.
            if (news != null && !news.isEmpty()) {
                adapter.addAll(news);
            } else {
                // Set empty state text to display "No results found".
                emptyStateTextView.setText(R.string.no_results);
            }
        } else {
            // There is no network connection.
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    /**
     * Saves the state of the app.
     *
     * @param outState: variable for storing the state of the app.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the ListView state (includes scroll position) as a Parcelable.
        outState.putParcelable("newsListViewState", newsListView.onSaveInstanceState());
    }

    /**
     * Recovers the state of the app, previously saved.
     *
     * @param savedInstanceState: the saved state fot the app.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore previous state (including selected item index and scroll position).
            newsListView.onRestoreInstanceState(savedInstanceState.getParcelable("newsListViewState"));
        }
    }

    /**
     * Checks if there is connection to network.
     *
     * @return true if there is network connection, false otherwise.
     */
    boolean isThereConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

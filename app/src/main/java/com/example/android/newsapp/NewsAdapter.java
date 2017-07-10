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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news in the data source (a
 * list of {@link News} objects). These list item layouts will be provided to an adapter view like
 * ListView to be displayed to the user.
 */
class NewsAdapter extends ArrayAdapter<News> {
    // Using the ButterKnife library for view injection.
    @BindView(R.id.news_title)
    TextView titleView;
    @BindView(R.id.news_section)
    TextView sectionView;
    @BindView(R.id.news_date)
    TextView dateView;
    @BindView(R.id.news_author)
    TextView authorView;
    @BindView(R.id.news_section_title)
    TextView sectionTitleView;
    @BindView(R.id.news_date_title)
    TextView dateTitleView;
    @BindView(R.id.news_author_title)
    TextView authorTitleView;

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app.
     * @param news    is the list of news, which is the data source of the adapter.
     */
    NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the news at the given position in
     * the list of news.
     *
     * @param position    is the position of the item within the adapter's data set of the item
     *                    whose view we want.
     * @param convertView is the old view to reuse, if possible.
     * @param parent      is the  parent that this view will eventually be attached to.
     * @return a View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse.
        // Otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ButterKnife.bind(this, listItemView);

        // Find the news at the given position in the list of news.
        News currentNews = getItem(position);

        // Set the title of the news. If there is no title, hide the title view.
        String title = currentNews.getTitle();
        if (!title.isEmpty()) {
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        } else titleView.setVisibility(View.GONE);

        // Set the section of the news. If there is no section, hide the section view.
        String section = currentNews.getSection();
        if (!section.isEmpty()) {
            sectionView.setText(section);
            sectionView.setVisibility(View.VISIBLE);
            sectionTitleView.setVisibility(View.VISIBLE);
        } else {
            sectionView.setVisibility(View.GONE);
            sectionTitleView.setVisibility(View.GONE);
        }

        // Set the date of the news. If there is no date, hide the date view.
        String date = currentNews.getDate();
        if (!date.isEmpty()) {
            dateView.setText(date);
            dateView.setVisibility(View.VISIBLE);
            dateTitleView.setVisibility(View.VISIBLE);
        } else {
            dateView.setVisibility(View.GONE);
            dateTitleView.setVisibility(View.GONE);
        }

        // Set the author of the news. If there is no author, hide the author view.
        String author = currentNews.getAuthors();
        if (!author.isEmpty()) {
            authorView.setText(author);
            if (author.contains(","))
                authorTitleView.setText(listItemView.getResources().getString(R.string.news_authors));
            else
                authorTitleView.setText(listItemView.getResources().getString(R.string.news_author));
            authorView.setVisibility(View.VISIBLE);
            authorTitleView.setVisibility(View.VISIBLE);
        } else {
            authorView.setVisibility(View.GONE);
            authorTitleView.setVisibility(View.GONE);
        }

        // Return the list item view that is now showing the appropriate data.
        return listItemView;
    }
}

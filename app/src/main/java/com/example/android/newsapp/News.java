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

/**
 * An {@link News} object contains information related to a single news.
 */
class News {
    private String title;   // Title of the news.
    private String section; // Section name of the news.
    private String url;     // Url for the web page of the news at www.theguardian.com.
    private String authors; // Authors of the news, comma separated.
    private String date;    // Publication date of the news.

    /**
     * Constructs a new {@link News} object.
     *
     * @param title   is the title of the news.
     * @param section is the section of the news.
     * @param url     is the url for the web page of the news at www.theguardian.com.
     * @param authors is the list of authors of the news, separated by commas.
     * @param date    is the publication date of the news.
     */
    News(String title, String section, String url, String authors, String date) {
        this.title = title;
        this.section = section;
        this.url = url;
        this.authors = authors;
        this.date = date;
    }

    /**
     * Getters.
     */
    String getTitle() {
        return title;
    }

    String getSection() {
        return section;
    }

    String getUrl() {
        return url;
    }

    String getAuthors() {
        return authors;
    }

    String getDate() {
        return date;
    }
}

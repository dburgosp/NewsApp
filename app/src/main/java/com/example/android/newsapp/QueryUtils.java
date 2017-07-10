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

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from content.guardianapis.com.
 */
final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the API dataset and return a list of {@link News} objects.
     *
     * @param requestUrl is the URL for getting the JSON object with the list of news.
     * @return a list of {@link News} objects.
     */
    static List<News> fetchNewsData(String requestUrl) {
        // Create URL object from the given string URL "requestUrl".
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response, create a list of {@link News}s and
        // return this list.
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url is the URL for the HTTP request.
     * @return a String as the response.
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        // If the URL is null, then return an empty JSON string.
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();

                // Convert the {@link InputStream} into a String which contains the whole JSON
                // response from the server.
                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line = reader.readLine();
                    while (line != null) {
                        output.append(line);
                        line = reader.readLine();
                    }
                }
                jsonResponse = output.toString();
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why the
                // makeHttpRequest(URL url) method signature specifies than an IOException could be
                // thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return a list of {@link News} objects that has been built up from parsing the given JSON
     * response.
     *
     * @param newsJSON is the JSON object to be parsed and converted to a list of {@link News}
     *                 objects.
     * @return the list of {@link News} objects parsed form the input JSON object.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to.
        List<News> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject associated with the key called "response", which contains all
            // the information retrieved. If there's no "response" object, exit returning null
            // before trying to extract the baseJsonResponse.
            if (baseJsonResponse.isNull("response")) {
                Log.i(LOG_TAG, "No \"response\" object for the query");
                return null;
            }
            JSONObject newsObject = baseJsonResponse.getJSONObject("response");

            // Search for the "results" array, which contains a list of news. If there's no
            // "results" array, exit returning null before trying to extract the newsObject.
            if (newsObject.isNull("results")) {
                Log.i(LOG_TAG, "No \"results\" array for the query");
                return null;
            }
            JSONArray resultsArray = newsObject.getJSONArray("results");

            // For each news in the resultsArray, create a {@link News} object.
            JSONObject currentNews;
            for (int i = 0; i < resultsArray.length(); i++) {
                // Get a single news element at position i within the list of news.
                currentNews = resultsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle", which represents the webTitle of
                // the news.
                String webTitle = "";
                if (!currentNews.isNull("webTitle")) webTitle = currentNews.getString("webTitle");

                // Extract the value for the key called "sectionName", which represents the section
                // of the news.
                String sectionName = "";
                if (!currentNews.isNull("sectionName"))
                    sectionName = currentNews.getString("sectionName");

                // Extract the value for the key called "webUrl", which represents the url of the
                // news at www.theguardian.com.
                String webUrl = "";
                if (!currentNews.isNull("webUrl")) webUrl = currentNews.getString("webUrl");

                // Extract the value for the key called "references", which represents the array of
                // references of the news, including information about the authors.
                String authors = "";
                if (!currentNews.isNull("references")) {
                    JSONArray referencesArray = currentNews.getJSONArray("references");
                    JSONObject currentReference;
                    for (int j = 0; j < referencesArray.length(); j++) {
                        // Get a single reference at position j within the list of references.
                        currentReference = referencesArray.getJSONObject(j);

                        // If the current element has the value "author" for the key "type", then
                        // look for the author name at key "id".
                        if (currentReference.getString("type").equals("author"))
                            if (!currentReference.isNull("id"))
                                if (j == 0) authors = parseAuthor(currentReference.getString("id"));
                                else
                                    authors = authors + ", " + parseAuthor(currentReference.getString("id"));
                    }
                }

                // Extract the value for the key called "webPublicationDate", which represents the
                // publication date of the news.
                String webPublicationDate = "";
                if (!currentNews.isNull("webPublicationDate"))
                    webPublicationDate = parseDateTime(currentNews.getString("webPublicationDate"));

                // Create a new {@link News} object with the data retrieved from the JSON response
                // and add it to the list of news.
                News newsElement = new News(webTitle, sectionName, webUrl, authors, webPublicationDate);
                news.add(newsElement);
            }
        } catch (
                JSONException e)

        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of news.
        return news;
    }

    /**
     * Takes the author from the JSON object, which comes in the format "author/string1-string2-..."
     * and returns a String with the format "String1 String2 ...".
     *
     * @param authorId is the string from the JSON document which contains the author name.
     * @return the author name capitalized and separated by blank characters.
     */
    static String parseAuthor(String authorId) {
        String author = "";

        // Remove "author/" from input string.
        String[] parts = authorId.split("/");
        author = parts[1];

        // Remove "-" separator and capitalize every word.
        parts = author.split("-");
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) author = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            else author = author + " " + parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
        }

        return author;
    }

    /**
     * Takes the author from the JSON object, which comes in the format "YYYY-MM-DDTHH:MM:SSZ" and
     * returns a String with the format "YYYY-MM-DD HH:MM:SS".
     *
     * @param webPublicationDate is the string from the JSON document which contains date and time.
     * @return date and time separated by a blank character.
     */
    static String parseDateTime(String webPublicationDate) {
        String dateTime = "";

        // Remove "T" character from input string.
        String[] parts = webPublicationDate.split("T");
        dateTime = parts[0];

        // Remove "Z" character from input string.
        parts = parts[1].split("Z");
        dateTime = dateTime + " " + parts[0];

        return dateTime;
    }
}

# News App

This is a simple Android Studio project for the [Android Basics Nanodegree](https://www.udacity.com/course/android-basics-nanodegree-by-google--nd803) given by Udacity and Google.

The goal was to create a News feed app which gives a user regularly-updated news from the internet related to a particular topic, person, or location. The presentation of the information as well as the topic was up to the student. I decided to show [the most recent news related to the topic **Spain** from The Guardian API](http://content.guardianapis.com/search?q=spain&api-key=test&reference-type=author&show-references=author&page-size=50&order-by=newest).

Some screen captures:

<IMG src="https://github.com/dburgosp/NewsApp/blob/master/img_news_list.jpg?raw=true" width="150" height="279" title="List Item Contents" alt="List Item Contents"/> <IMG src="https://github.com/dburgosp/NewsApp/blob/master/img_sending_intent.jpg?raw=true" width="150" height="279" title="Sending intent" alt="Sending intent"/> <IMG src="https://github.com/dburgosp/NewsApp/blob/master/img_guardian_web.jpg?raw=true" width="150" height="279" title="News at The Guardian website" alt="News at The Guardian website"/>

# Project Specifications

## Layout

1. **Main Screen**. App contains a main screen which displays multiple news stories.
2. **List Item Contents**. Each list item on the main screen displays relevant text and information about the story. Required fields include the title of the article and the name of the section that it belongs to. If available, author name and date published should be included. Please note not all responses will contain these pieces of data, but it is required to include them if they are present.
3. **Layout Best Practices**. The code adheres to all of the following best practices:
   * Text sizes are defined in sp.
   * Lengths are defined in dp.
   * Padding and margin is used appropriately, such that the views are not crammed up against each other.

## Functionality

1. **Main Screen Updates**. Stories shown on the main screen update properly whenever new news data is fetched from the API.
2. **Errors**. The code runs without errors.
3. **Story Intents**. Clicking on a story opens the story in the user’s browser.
4. **API Query**. App queries the [content.guardianapis.com api](http://open-platform.theguardian.com/documentation/) to fetch news stories related to the topic chosen by the student, using either the ‘test’ api key or the student’s key.
5. **Use of Loaders**. Networking operations are done using a [Loader](https://developer.android.com/reference/android/content/Loader.html) rather than an [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask.html).
6. **External Libraries and Packages**. The intent of this project is to give you practice writing raw Java code using the necessary classes provided by the Android framework; therefore, the use of external libraries for the core functionality will not be permitted to complete this project.

## Code Readability

1. **Readability**. Code is easily readable such that a fellow programmer can understand the purpose of the app.
2. **Naming Conventions**. All variables, methods, and resource IDs are descriptively named such that another developer reading the code can easily understand their function.
3. **Formatting**. The code is properly formatted i.e. there are no unnecessary blank lines; there are no unused variables or methods; there is no commented out code. The code also has proper indentation when defining variables and methods.


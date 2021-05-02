# iSearch – Apple Search Engine

## App Walkthrough

### Retrieve Movie List from iTunes Store API

1. Use **Retrofit+OKHttpClient** for Type-Safe HTTP calls.
2. Use **Moshi** for parsing JSON returned by API.
3. Retrieve Movies in Australia with Term "Star".
4. Two GETs are performed successively: quick partial search (QPS), then full search (FS).
4. In QPS, the limit parameter is set to 20 so it retrieves only 20 movies to make app responsive.
5. In FS, the limit parameter is 200 (w/c is the maximum allowed by API) to retrieve 200 movies.
6. The **RecyclerView** is automatically updated through **Room+LiveData**.

![Partial Search then Full Search](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/partialSearch_thenFullSearch.gif?raw=true)

#### Retrieve Movie Artwork

1. Use **Glide** to manage Movie's artwork placeholder, loading of artwork image URL, and error.
2. The artwork image in MovieList uses the smallest size w/c is 30x30px.

![Load 30-Pixel Artwork Using Glide](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/loadArtwork30_usingGlide.gif?raw=true)

#### Swipe Down to Update Movie List

1. Use **SwipeRefreshLayout** for refreshing Movie List.
2. The progress indicator informs user that refresh is ongoing.

![Swipe Down to Refresh Movie List](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/swipeToRefresh.gif?raw=true)

### Show Movie Detail w/ DataBinding

The artwork image in MovieDetail uses the largest size w/c is 100x100px.

In landscape,

![Movie Detail in Landscape](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/movieDetail_landscape.gif?raw=true)

In portrait,

![Movie Detail in Portrait](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/movieDetail_portrait.gif?raw=true)

### Survive Configuration Changes Using ViewModel

App survives rotation,

![Survive Configuration Changes Using ViewModel](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/surviveConfigchanges_usingViewModel.gif?raw=true)

App opens last screen the user was on,

![Open Last Screen Visited](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/openLastScreenVisited.gif?raw=true)

App survives process death,

![Survive Process Death](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/surviveProcessDeath.gif?raw=true)

### Error Handling

MovieList shows retry message if there are no cached movies,

![Failed to Retrieve Movie List](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/failedToRetrieve_noCachedMovies.gif?raw=true)

Snackbar shows retry message if there are cached movies,

![Failed to Retrieve Movie List But With Cached Movies](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/failedToRetrieve_withCachedMovies.gif?raw=true)

## Architecture – MVVM

MVP
* Uses Callbacks
* Pros:
   * Separates the View and Model using Presenter
* Cons:
   * Presenter coupled w/ View/UI
   * Presenter destroyed on configuration changes (e.g. device rotation)

MVVM 
* Uses ObserverPattern (e.g. LiveData, RxJava2)
* Pros:
   * ViewModel is lifecycle-aware so survives configuration changes
   * ViewModel is unaware of View/UI hence loosely coupled
   * [My Opinion] Google currently endorses MVVM so using MVVM might increase readability since most androidDevs should be familiar. 
* Cons:
   * [My Opinion] LiveData/Coroutines could be hard to trace/debug
   * Observables jungle
   * Possible cyclic update between View and ViewModel

## Persistence/Caching Mechanism – **RepositoryPattern+Room**

1. Local Database
    * For saving non-volatile complex objects that survives phone reboot.
    * For this app, save/cache the retrieved movies.

![Cache Movie List using Room and SQLite](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/cacheMovieList_usingRoom+SQLite.png?raw=true)

2. SharedPreferences
    * For saving non-volatile keys/values.
    * Could be used to save app settings/configurations that survives phone reboot.
    * For this app, could be used to save the date user previously visited.

![Display Datetime User Last Visited App on Recyclerview Header Flow](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/recyclerViewHeader_displaysLastUserVisit.png?raw=true)

```
@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
   fun onBackground() {
      val now = Date().time
      val sharedPref = applicationContext.getSharedPreferences(
         applicationContext.getString(R.string.preference_file),
         Context.MODE_PRIVATE
      ) ?: return
      with(sharedPref.edit()) {
         putLong(applicationContext.getString(R.string.last_user_visit_timestamp), now)
         apply()
      }
   }
```

![Display Datetime User Last Visited App on Recyclerview Header](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/displayLastUserVisit.png?raw=true)

3A. Bundles

Incidentally, the _arguments_ variable used to pass the movie.trackId from MovieList_screen to MovieDetail_screen is a Bundle so it survives destroyed Activity/Fragment. If we use SafeArgs, then we'll need to use SavedInstanceState bundle to restore the movie displayed on MovieDetails_screen to survive process death. Example:

```
override fun onSaveInstanceState(outState: Bundle) {
    outState?.run {
        putLong(ARG_ITEM_ID, trackId)
    }
    super.onSaveInstanceState(outState)
}

override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    savedInstanceState?.run {
        trackId = getLong(ARG_ITEM_ID)
    }
    Timber.e("onViewStateRestored")
}
```

![Survive Configuration Changes using Bundles](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/survivingConfigurationDeath_usingBundles.png?raw=true)

**NOT Implemented in App**

3B. SavedInstanceState (Bundle)
* For saving simple data.
* Could be used to save filled fields in a form to survive app restore (e.g. process death) so user doesn't have to fill-in again.
* For this app, can be used to save movie.trackId shown in MovieDetail screen to show again when user returns to app.

4. Disk
    * For non-volatile saving of files/images on local_storage/sd_card.
    * For this app, can be used to cache the movie artworks at the expense of taking up significant storage space. Maybe limit to _n_ recent artworks.

## Testing

Using **Espresso, Hamcrest, IdlingResource, DataBindingIdlingResource**.

### Unit Test for MovieListViewModel using FakeRepository

![Unit Test MovieListViewModel](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/unitTest_movieListViewModel.gif?raw=true)

### UI Test for MovieDetail

In landscape, 

![UI Test for Movie Detail in Landscape](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/uiTest_movieDetail_landscape.gif?raw=true)

In portrait, 

![UI Test for Movie Detail in Landscape](https://raw.githubusercontent.com/biinui/iSearch/master/blob/assets/uiTest_movieDetail_portrait.gif?raw=true)

## Installation

[Please download APK here](https://drive.google.com/drive/folders/1OhuWoN_Ewxi3Cc-hexhLkI2c6L-hb6vf)

## Bugs

Bug: SwipeRefreshLayout Progress Indicator Doesn't Show

Pre-Condition:
1. No internet connection
2. No cached movies (fresh install)
3. Open app

## Future Work

1. MovieList Pagination
2. Dependency Injection (Dagger2 or Koin)
3. NavigationComponent + SafeArgs
4. API provides relevant attributes only in JSON response




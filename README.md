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

### Show Movie Detail

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

## Persistence/Caching Mechanism – **RepositoryPattern+Room**

1. SavedInstanceState
    * User Form
    * Movie Detail – simple data
2. SharedPreferences
    * Key/Value Store  
    * Date User Previously Visited
3. Local Database
    * Complex Objects
4. Disk
    * Store Images on SDCard/LocalDrive
    * Example: Movie Artwork - at the expense of taking up space.

## Architecture – MVVM

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




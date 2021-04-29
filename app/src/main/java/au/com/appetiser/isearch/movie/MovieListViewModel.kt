package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.appetiser.isearch.network.ITunesStoreApi
import au.com.appetiser.isearch.network.model.Movie
import au.com.appetiser.isearch.network.model.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

private const val TERM_STAR   = "star"
private const val COUNTRY_AU  = "au"
private const val MEDIA_MOVIE = "movie"

class MovieListViewModel(): ViewModel() {

    init {
        getMovies()
    }

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _showGetMoviesFailed = MutableLiveData<Boolean>()
    val showGetMoviesFailed: LiveData<Boolean>
        get() = _showGetMoviesFailed

    private fun getMovies() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val movieListResponse: MovieListResponse = ITunesStoreApi.retrofitService
                                                                             .get( TERM_STAR   ,
                                                                                   COUNTRY_AU  ,
                                                                                   MEDIA_MOVIE )
                    _movieList.postValue(movieListResponse.results)
                }
            } catch(exception: Exception) {
                _showGetMoviesFailed.value = true
                Timber.e("Failed to get movies: ${exception.localizedMessage}")
            }
        }
    }

    fun showGetMoviesFailedDone() {
        _showGetMoviesFailed.value = false
    }

}
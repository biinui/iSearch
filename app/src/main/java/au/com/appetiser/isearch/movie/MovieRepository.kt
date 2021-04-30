package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.appetiser.isearch.database.MovieDatabase
import au.com.appetiser.isearch.network.ITunesStoreApi
import au.com.appetiser.isearch.network.model.Movie
import au.com.appetiser.isearch.network.model.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TERM_STAR   = "star"
private const val COUNTRY_AU  = "au"
private const val MEDIA_MOVIE = "movie"

private const val LIMIT_20  = "20"
private const val LIMIT_200 = "200"

class MovieRepository(private val database: MovieDatabase) {
    val movie = MutableLiveData<Movie>()
    val movieList: LiveData<List<Movie>> = database.movieDao.getAllMovies()

    suspend fun refreshMovieList() {
        withContext(Dispatchers.IO) {
            var movieListResponse = quickSearch()
            database.movieDao.insertAllMovies(movieListResponse.results)

            movieListResponse = maxSearch()
            database.movieDao.insertAllMovies(movieListResponse.results)
        }
    }

    private suspend fun quickSearch(): MovieListResponse {
        return ITunesStoreApi.retrofitService.get( TERM_STAR   ,
                                                   COUNTRY_AU  ,
                                                   MEDIA_MOVIE ,
                                                   LIMIT_20    )
    }

    private suspend fun maxSearch(): MovieListResponse {
        return ITunesStoreApi.retrofitService.get( TERM_STAR   ,
                                                   COUNTRY_AU  ,
                                                   MEDIA_MOVIE ,
                                                   LIMIT_200   )
    }

    suspend fun getMovie(trackId: Long) {
        withContext(Dispatchers.IO) {
            val retrievedMovie = database.movieDao.getMovieById(trackId)
            movie.postValue(retrievedMovie)
        }
    }

}
package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.appetiser.isearch.database.MovieDatabase
import au.com.appetiser.isearch.network.ITunesStoreApi
import au.com.appetiser.isearch.network.model.Movie
import au.com.appetiser.isearch.network.model.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TERM_STAR   = "star"
private const val COUNTRY_AU  = "au"
private const val MEDIA_MOVIE = "movie"

private const val LIMIT_20  = "20"
private const val LIMIT_200 = "200"

class MovieRepository @Inject constructor(private val database: MovieDatabase): MovieRepositoryInterface {
    override val movie = MutableLiveData<Movie>()
    override val movieList: LiveData<List<Movie>> = database.movieDao.getAllMovies()

    override suspend fun refreshMovieList() {
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

    override suspend fun getMovie(trackId: Long) {
        withContext(Dispatchers.IO) {
            val retrievedMovie = database.movieDao.getMovieById(trackId)
            movie.postValue(retrievedMovie)
        }
    }

}
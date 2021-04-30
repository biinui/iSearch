package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import au.com.appetiser.isearch.database.MovieDatabase
import au.com.appetiser.isearch.network.ITunesStoreApi
import au.com.appetiser.isearch.network.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TERM_STAR   = "star"
private const val COUNTRY_AU  = "au"
private const val MEDIA_MOVIE = "movie"

class MovieRepository(private val database: MovieDatabase) {
    val movieList: LiveData<List<Movie>> = database.movieDao.getAllMovies()

    suspend fun refreshMovieList() {
        withContext(Dispatchers.IO) {
            val movieListResponse = ITunesStoreApi.retrofitService
                                                  .get( TERM_STAR   ,
                                                        COUNTRY_AU  ,
                                                        MEDIA_MOVIE )
            database.movieDao.insertAllMovies(movieListResponse.results)
        }
    }

}
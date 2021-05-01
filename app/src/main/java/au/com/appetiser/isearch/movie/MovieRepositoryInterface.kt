package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.appetiser.isearch.network.model.Movie

interface MovieRepositoryInterface {
    val movie: MutableLiveData<Movie>
    val movieList: LiveData<List<Movie>>

    suspend fun refreshMovieList()
    suspend fun getMovie(trackId: Long)
}
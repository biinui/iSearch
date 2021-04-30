package au.com.appetiser.isearch.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.appetiser.isearch.movie.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val trackId: Long, private val repository: MovieRepository) :
    ViewModel() {

    val movie = repository.movie

    init {
        getMovie(trackId)
    }

    private fun getMovie(trackId: Long) {
        viewModelScope.launch {
            repository.getMovie(trackId)
        }
    }

}
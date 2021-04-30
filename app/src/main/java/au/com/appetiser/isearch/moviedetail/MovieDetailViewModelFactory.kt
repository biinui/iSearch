package au.com.appetiser.isearch.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.appetiser.isearch.movie.MovieRepository

class MovieDetailViewModelFactory(private val trackId: Long, private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(trackId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
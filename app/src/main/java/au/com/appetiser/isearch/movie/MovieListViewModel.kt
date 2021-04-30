package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MovieListViewModel(private val repository: MovieRepository): ViewModel() {

    init {
        refreshMovieList()
    }

    val movieList = repository.movieList

    private val _showGetMoviesFailed = MutableLiveData<Boolean>()
    val showGetMoviesFailed: LiveData<Boolean>
        get() = _showGetMoviesFailed

    private fun refreshMovieList() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.refreshMovieList()
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
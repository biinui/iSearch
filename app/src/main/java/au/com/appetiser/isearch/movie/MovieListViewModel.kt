package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieListViewModel(private val repository: MovieRepository): ViewModel() {

    val movieList = repository.movieList

    private var _showGetMoviesFailed = MutableLiveData<Boolean>().apply { false }
    val showGetMoviesFailed: LiveData<Boolean>
        get() = _showGetMoviesFailed

    private var _isRefreshingMovieList = MutableLiveData<Boolean>().apply { false }
    val isRefreshingMovieList: LiveData<Boolean>
        get() = _isRefreshingMovieList

    init {
        refreshMovieList()
    }

    fun refreshMovieList() {
        viewModelScope.launch {
            try {
                refreshMovieListStarted()
                repository.refreshMovieList()
            } catch(exception: Exception) {
                _showGetMoviesFailed.value = true
                Timber.e("Failed to get movies: ${exception.localizedMessage}")
            } finally {
                refreshMovieListDone()
            }
        }
    }

    fun showGetMoviesFailedDone() {
        _showGetMoviesFailed.value = false
    }

    private fun refreshMovieListStarted() {
        _isRefreshingMovieList.value = true
    }

    private fun refreshMovieListDone() {
        _isRefreshingMovieList.value = false
    }

}
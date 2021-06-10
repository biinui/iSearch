package au.com.appetiser.isearch.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MovieListViewModel @Inject constructor(private val repository: MovieRepositoryInterface): ViewModel() {

    val movieList = repository.movieList

    private var _showGetMoviesFailed = MutableLiveData<Boolean>().apply { false }
    val showGetMoviesFailed: LiveData<Boolean>
        get() = _showGetMoviesFailed

    private var _showUpdateMoviesFailed = MutableLiveData<Boolean>().apply { false }
    val showUpdateMoviesFailed: LiveData<Boolean>
        get() = _showUpdateMoviesFailed

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
                if (movieList.value.isNullOrEmpty()) _showGetMoviesFailed.value    = true
                else                                 _showUpdateMoviesFailed.value = true
                Timber.e("Failed to get movies: ${exception.localizedMessage}")
            } finally {
                refreshMovieListDone()
            }
        }
    }

    fun showGetMoviesFailedDone() {
        _showGetMoviesFailed.value = false
    }

    fun showUpdateMoviesFailedDone() {
        _showUpdateMoviesFailed.value = false
    }

    private fun refreshMovieListStarted() {
        _isRefreshingMovieList.value = true
    }

    private fun refreshMovieListDone() {
        _isRefreshingMovieList.value = false
    }

}
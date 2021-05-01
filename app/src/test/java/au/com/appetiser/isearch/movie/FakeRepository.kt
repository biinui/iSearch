package au.com.appetiser.isearch.movie

import androidx.lifecycle.MutableLiveData
import au.com.appetiser.isearch.network.model.Movie

class FakeRepository : MovieRepositoryInterface {
    private var db: LinkedHashMap<Long, Movie> = LinkedHashMap()

    override val movie = MutableLiveData<Movie>()
    override val movieList = MutableLiveData<List<Movie>>().apply { db.values.toList() }

    fun addMovie(movie: Movie) {
        db[movie.trackId] = movie
    }

    override suspend fun refreshMovieList() {
        movieList.postValue(db.values.toList())
    }

    override suspend fun getMovie(trackId: Long) {
        movie.postValue(db[trackId])
    }

}
package au.com.appetiser.isearch.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import au.com.appetiser.isearch.network.model.Movie
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieListViewModelTest {

    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var movieRepository: FakeRepository

    val testMovie = Movie(42, "TRACK NAME", "URL30", "URL100", 42.0, "GENRE", "LONG DESCRIPTION")

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        movieRepository = FakeRepository()
        movieListViewModel = MovieListViewModel(movieRepository)
    }

    @Test
    fun refreshMovieList_success() {
        movieRepository.addMovie(testMovie)

        mainCoroutineRule.pauseDispatcher()
        movieListViewModel.refreshMovieList()
        mainCoroutineRule.resumeDispatcher()

        assertThat(movieListViewModel.movieList.getOrAwaitValue(), hasSize(1))
    }

}
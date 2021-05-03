package au.com.appetiser.isearch.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.appetiser.isearch.R
import au.com.appetiser.isearch.database.MovieDatabase
import au.com.appetiser.isearch.databinding.ActivityItemListBinding
import au.com.appetiser.isearch.moviedetail.MovieDetailActivity
import au.com.appetiser.isearch.moviedetail.MovieDetailFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [MovieDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MovieListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var isTwoPane: Boolean = false
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var swipeToRetry: ConstraintLayout
    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        initIsTwoPane()

        initSwipeRefresh()
        swipeRefresh.isRefreshing = true

        initViewModel()

        initSwipeToRetryView()
        initToolbar()

        observeRefreshingMovieList()
        observeGetMoviesFailed()
        observeUpdateMoviesFailed(binding.root)

    }

    override fun onStart() {
        super.onStart()
        val movieListAdapter = MovieListAdapter(initMovieListener(), getLastUserVisitDatetime())
        initRecyclerView(movieListAdapter)
        observeMovieList(movieListAdapter)
    }

    private fun initViewModel() {
        val database = MovieDatabase.getInstance(this.application)
        val repository = MovieRepository(database)
        val viewModelFactory = MovieListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)
    }

    /**
     *  The detail container view will be present only in the
     *  large-screen layouts (res/values-w900dp).
     *  If this view is present, then the
     *  activity should be in two-pane mode.
     *
     *  Side-effect:  updates isTwoPane value
     */
    private fun initIsTwoPane() {
        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) isTwoPane = true
    }

    private fun initSwipeToRetryView() {
        swipeToRetry = findViewById(R.id.swipe_to_retry_message)
    }

    private fun initSwipeRefresh() {
        swipeRefresh = getSwipRefresh(isTwoPane)
        swipeRefresh.setOnRefreshListener {
            swipeToRetry.visibility = View.GONE
            viewModel.refreshMovieList()
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    private fun initMovieListener(): MovieListener {
        return MovieListener { movie ->
            when (isTwoPane) {
                true  -> navigateToItemDetailFragment(movie.trackId)
                false -> navigateToItemDetailActivity(movie.trackId)
            }
        }
    }

    private fun initRecyclerView(movieListAdapter: MovieListAdapter) {
        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        recyclerView.adapter = movieListAdapter
    }

    private fun navigateToItemDetailFragment(movieId: Long) {
        val fragment = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(MovieDetailFragment.ARG_ITEM_ID, movieId)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.item_detail_container, fragment)
            .commit()
    }

    private fun navigateToItemDetailActivity(movieId: Long) {
        val intent = Intent(applicationContext, MovieDetailActivity::class.java).apply {
            putExtra(MovieDetailFragment.ARG_ITEM_ID, movieId)
        }
        startActivity(intent)
    }

    private fun observeRefreshingMovieList() {
        viewModel.isRefreshingMovieList.observe(this, Observer { isRefreshingMovieList ->
            isRefreshingMovieList?.let {
                if (isRefreshingMovieList == false) swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun observeGetMoviesFailed() {
        viewModel.showGetMoviesFailed.observe(this, Observer {
            if (it) {
                swipeRefresh.isRefreshing = false
                swipeToRetry.visibility = View.VISIBLE
                viewModel.showGetMoviesFailedDone()
            }
        })
    }

    private fun observeUpdateMoviesFailed(view: View) {
        viewModel.showUpdateMoviesFailed.observe(this, Observer {
            if (it) {
                swipeRefresh.isRefreshing = false
                showUpdateFailedSnackbar(view)
                viewModel.showUpdateMoviesFailedDone()
            }
        })
    }

    private fun observeMovieList(movieListAdapter: MovieListAdapter) {
        viewModel.movieList.observe(this, Observer { movieList ->
            movieList?.let {
                movieListAdapter.addHeaderAndSubmitList(movieList)
                swipeRefresh.isRefreshing = false
                if (movieList.isNotEmpty()) swipeToRetry.visibility = View.GONE
            }
        })
    }

    private fun showUpdateFailedSnackbar(view: View) {
        val snackbar = Snackbar.make(
            view,
            R.string.failed_to_retrieve_movies_please_swipe_down_to_retry,
            Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        val snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTextView.maxLines = 2
        snackbar.show()
    }

    private fun getSwipRefresh(isTwoPane: Boolean): SwipeRefreshLayout {
        return if (isTwoPane) findViewById(R.id.swiperefresh_twopane)
               else           findViewById(R.id.swiperefresh)
    }

    private fun getLastUserVisitTimestamp(): Long {
        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE) ?: return TIMESTAMP_DEFAULT_VALUE
        return sharedPref.getLong(getString(R.string.last_user_visit_timestamp), TIMESTAMP_DEFAULT_VALUE)
    }

    private fun formatTimestampToDatetime(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return DateFormat.format("yyyy MMM dd HH:mm:ss", calendar).toString()
    }

    private fun getLastUserVisitDatetime(): String {
        return when (val timestamp = getLastUserVisitTimestamp()) {
            TIMESTAMP_DEFAULT_VALUE  -> getString(R.string.first_visit)
            else -> getString(R.string.last_user_visit_timestamp_format, formatTimestampToDatetime(timestamp))
        }
    }

    companion object {
        const val TIMESTAMP_DEFAULT_VALUE: Long = -1L
    }

}
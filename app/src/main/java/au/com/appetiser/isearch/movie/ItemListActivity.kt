package au.com.appetiser.isearch.movie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import au.com.appetiser.isearch.R
import au.com.appetiser.isearch.database.MovieDatabase
import au.com.appetiser.isearch.databinding.ActivityItemListBinding
import au.com.appetiser.isearch.moviedetail.ItemDetailActivity
import au.com.appetiser.isearch.moviedetail.ItemDetailFragment

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var isTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true
        }

        val swipeRefresh = getSwipRefresh(isTwoPane)
        swipeRefresh.isRefreshing = true

        val database = MovieDatabase.getInstance(this.application)
        val repository = MovieRepository(database)
        val viewModelFactory = MovieListViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)

        swipeRefresh.setOnRefreshListener {
            viewModel.refreshMovieList()
        }

        viewModel.isRefreshingMovieList.observe(this, Observer { isRefreshingMovieList ->
            isRefreshingMovieList?.let {
                if (isRefreshingMovieList == false) swipeRefresh.isRefreshing = false
            }
        })

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val movieListAdapter = MovieListAdapter(MovieListener { movie ->
            if (isTwoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ItemDetailFragment.ARG_ITEM_ID, movie.trackId)
                    }
                }
                this.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(applicationContext, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, movie.trackId)
                }
                startActivity(intent)
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        recyclerView.adapter = movieListAdapter

        viewModel.movieList.observe(this, Observer { movieList ->
            movieList?.let {
                movieListAdapter.submitList(movieList)
                swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun getSwipRefresh(isTwoPane: Boolean): SwipeRefreshLayout {
        return if (isTwoPane) findViewById(R.id.swiperefresh_twopane)
               else           findViewById(R.id.swiperefresh)
    }

}
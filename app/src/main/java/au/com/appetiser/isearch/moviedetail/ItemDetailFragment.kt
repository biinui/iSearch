package au.com.appetiser.isearch.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import au.com.appetiser.isearch.R
import au.com.appetiser.isearch.database.MovieDatabase
import au.com.appetiser.isearch.databinding.ItemDetailBinding
import au.com.appetiser.isearch.movie.MovieRepository
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater   ,
                               container: ViewGroup?      ,
                               savedInstanceState: Bundle?,
    ): View? {
        val binding = ItemDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val trackId = it.getLong(ARG_ITEM_ID)
                val database = MovieDatabase.getInstance(requireActivity().application)
                val repository = MovieRepository(database)
                val viewModelFactory = MovieDetailViewModelFactory(trackId, repository)
                val viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)
                binding.viewModel = viewModel
                viewModel.movie.observe(viewLifecycleOwner, Observer { movie ->
                    movie?.let {
                        requireActivity().findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = viewModel.movie.value?.trackName
                    }
                })

            }
        }

        return binding.root
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
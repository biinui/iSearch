package au.com.appetiser.isearch.movie

import au.com.appetiser.isearch.network.model.Movie

sealed class DataItem {
    abstract val id: Long

    data class MovieItem(val movie: Movie): DataItem() {
        override val id = movie.trackId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
}
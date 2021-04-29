package au.com.appetiser.isearch.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListResponse (
    val results: List<Movie>
)
package au.com.appetiser.isearch.movie

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import au.com.appetiser.isearch.R
import com.bumptech.glide.Glide

@BindingAdapter("movieArtwork")
fun fetchMovieArtwork(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri()
                     .buildUpon()
                     .scheme("https")
                     .build()
        Glide.with(view)
             .load(uri)
             .circleCrop()
             .placeholder(R.drawable.ic_baseline_movie_24)
             .error(R.drawable.ic_baseline_movie_24)
             .into(view);
    }
}
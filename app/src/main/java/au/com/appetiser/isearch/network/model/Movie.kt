package au.com.appetiser.isearch.network.model

data class Movie(
    val trackId         : Long  ,
    val trackName       : String,
    val artworkUrl30    : String,
    val artworkUrl100   : String,
    val trackPrice      : Double,
    val primaryGenreName: String,
    val longDescription : String,
)
package au.com.appetiser.isearch.network.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey val trackId         : Long  ,
    @ColumnInfo val trackName       : String,
    @ColumnInfo val artworkUrl30    : String,
    @ColumnInfo val artworkUrl100   : String,
    @ColumnInfo val trackPrice      : Double,
    @ColumnInfo val primaryGenreName: String,
    @ColumnInfo val longDescription : String,
)
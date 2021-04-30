package au.com.appetiser.isearch.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import au.com.appetiser.isearch.network.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(movieList: List<Movie>)

    @Query("SELECT * FROM movie_table")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_table WHERE trackId = :trackId")
    fun getMovieById(trackId: Int): Movie

}
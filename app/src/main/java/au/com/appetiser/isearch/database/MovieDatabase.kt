package au.com.appetiser.isearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import au.com.appetiser.isearch.network.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder( context.applicationContext,
                                                     MovieDatabase::class.java ,
                                                     "movie_database"          )
                                   .fallbackToDestructiveMigration()
                                   .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

}
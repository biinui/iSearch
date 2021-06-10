package au.com.appetiser.isearch.di

import android.app.Application
import au.com.appetiser.isearch.database.MovieDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): MovieDatabase {
        return MovieDatabase.getInstance(application.applicationContext)
    }

}
package au.com.appetiser.isearch.di

import au.com.appetiser.isearch.movie.MovieListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, StorageModule::class])
interface AppComponent {

    fun inject(activity: MovieListActivity)

}
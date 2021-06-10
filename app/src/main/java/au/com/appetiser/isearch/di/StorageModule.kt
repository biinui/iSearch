package au.com.appetiser.isearch.di

import au.com.appetiser.isearch.movie.MovieRepository
import au.com.appetiser.isearch.movie.MovieRepositoryInterface
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {

    @Binds
    abstract fun provideRepository(repositoryInterface: MovieRepository): MovieRepositoryInterface

}
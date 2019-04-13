package com.example.mvvmstructure.viewmodellayer

import com.example.mvvmstructure.domainlayer.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    /*
     * Here we would provide all the viewModels.*/

    internal val viewModelFactory: ViewModelFactory
        @Provides
        @Singleton
        get() = ViewModelFactory(MovieRepository())

    /*
     * Other viewModels goes here.*/
}

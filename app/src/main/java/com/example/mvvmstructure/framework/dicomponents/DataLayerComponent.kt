package com.example.mvvmstructure.framework.dicomponents

import com.example.mvvmstructure.domainlayer.datasources.database.DatabaseModule
import com.example.mvvmstructure.domainlayer.datasources.network.NetworkModule
import com.example.mvvmstructure.domainlayer.repositories.MovieRepository
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, DatabaseModule::class])
@Singleton
interface DataLayerComponent {
    fun doInjection(movieRepository: MovieRepository)
}
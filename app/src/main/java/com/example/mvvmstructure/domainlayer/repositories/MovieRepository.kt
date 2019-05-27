package com.example.mvvmstructure.domainlayer.repositories

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmstructure.domainlayer.DataRequest
import com.example.mvvmstructure.domainlayer.datasources.database.DatabaseModule
import com.example.mvvmstructure.domainlayer.datasources.network.ApiUrls
import com.example.mvvmstructure.domainlayer.datasources.network.NetworkModule
import com.example.mvvmstructure.domainlayer.datasources.network.NetworkOperationObserver
import com.example.mvvmstructure.domainlayer.managers.DataManager
import com.example.mvvmstructure.domainlayer.managers.FetchLimiter
import com.example.mvvmstructure.domainlayer.model.Movie
import com.example.mvvmstructure.domainlayer.model.Response
import com.example.mvvmstructure.framework.BaseApp
import com.example.mvvmstructure.framework.TaskExecutors
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNREACHABLE_CODE")
@Singleton
class MovieRepository @Inject
constructor() {

    @Inject
    lateinit var taskExecutors: TaskExecutors
    @Inject
    lateinit var movieListApi: NetworkModule.MovieListApi
    @Inject
    lateinit var movieDatabase: DatabaseModule.MovieDatabase

    private val movieListFetchLimiter: FetchLimiter

    init {
        BaseApp.dataLayerComponent.doInjection(this)
        movieListFetchLimiter =
            FetchLimiter(10, TimeUnit.MINUTES)
    }

    fun loadMovieList(searchString: String, forceRefresh: Boolean): LiveData<DataRequest<List<Movie>>> {
        return object : DataManager<DataRequest<Response>, List<Movie>>(this.taskExecutors) {
            override fun shouldFetchData(data: List<Movie>): Boolean {
                return data.isEmpty() || forceRefresh || this@MovieRepository.movieListFetchLimiter.shouldFetch(
                    searchString
                )
            }

            override fun loadFromDatabase(): LiveData<List<Movie>> {
                return movieDatabase.movieDao().movieList
            }

            @SuppressLint("CheckResult")
            override fun loadFromNetwork(): LiveData<DataRequest<Response>>? {
                val responseFromNetwork = MutableLiveData<DataRequest<Response>>()
                movieListApi.searchMovie(ApiUrls.apiToken, searchString, "movie")
                    .subscribeOn(Schedulers.from(taskExecutors.networkOperationThread))
                    .observeOn(Schedulers.from(taskExecutors.mainThread))
                    .subscribe(
                        NetworkOperationObserver(
                            responseFromNetwork,
                            this
                        )
                    )
                return responseFromNetwork
            }

            override fun processResponse(response: DataRequest<Response>): List<Movie>? {

                return if (response.data == null)
                    null
                else
                    response.data.movies
            }


            override fun saveDataToDatabase(data: List<Movie>) {
                movieDatabase.movieDao().insert(data)
            }

            override fun clearPreviousData() {
                movieDatabase.movieDao().removeAllMovies()
            }
        }.toLiveData()
    }

    fun loadMovie(movieId: String): LiveData<DataRequest<Movie>>? {
        return object : DataManager<Response, Movie>(taskExecutors) {
            override fun loadFromDatabase(): LiveData<Movie> {
                return movieDatabase.movieDao().getMovie(movieId)

            }

            override fun loadFromNetwork(): LiveData<Response>? {
                return null
            }

            override fun shouldFetchData(data: Movie): Boolean {
                return false
            }

            override fun saveDataToDatabase(data: Movie) {

            }

            override fun clearPreviousData() {

            }

            override fun processResponse(response: Response): Movie? {
                return null
            }
        }.toLiveData()
    }
}


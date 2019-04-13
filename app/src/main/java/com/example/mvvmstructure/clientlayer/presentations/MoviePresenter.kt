package com.example.mvvmstructure.clientlayer.presentations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mvvmstructure.domainlayer.model.Movie
import com.example.mvvmstructure.domainlayer.DataRequest

class MoviePresenter(
    var mainViewModel: MainViewModel,
    var viewController: ViewController,
    var presenterType: PresenterType
) {
    private var isLoading: MutableLiveData<Boolean>
    private lateinit var movieSearchStringLiveData: MutableLiveData<String>
    private lateinit var movieIdLiveData: MutableLiveData<String>
    private lateinit var movieList: MutableLiveData<List<Movie>>
    private lateinit var movie: MutableLiveData<Movie>

    init {
        this.viewController = viewController
        this.presenterType = presenterType
        isLoading = MutableLiveData()
        if (this.presenterType == PresenterType.LIST) {
            movieList = MutableLiveData()
            movieSearchStringLiveData = mainViewModel.getObservableMovieSearchString()
            mainViewModel.getObservableMovieList().observe(viewController.getLifeCycleOwner(), Observer<DataRequest<List<Movie>>> { this.consumeResponse(it) })
        } else {
            movie = MutableLiveData()
            movieIdLiveData = mainViewModel.getObservableMovieIdString()
            mainViewModel.getObservableMovie().observe(viewController.getLifeCycleOwner(), Observer<DataRequest<Movie>> { this.consumeResponse(it) })
        }
    }

    fun consumeResponse(dataRequest: DataRequest<*>) {
        when (dataRequest.currentState) {
            DataRequest.Status.LOADING -> {

                isLoading.setValue(true)
                if (presenterType == PresenterType.LIST) {
                    movieList.value = dataRequest.data as List<Movie>?
                } else {
                    movie.setValue(dataRequest.data as Movie?)
                }
                Log.d("movie List", "" + dataRequest.currentState)
                viewController.onLoadingOccurred()

            }
            DataRequest.Status.SUCCESS -> {

                isLoading.setValue(false)

                if (presenterType == PresenterType.LIST) {
                    movieList.value = dataRequest.data as List<Movie>?
                    Log.d("movie List", "" + dataRequest.data!!.toString())


                } else {
                    movie.setValue(dataRequest.data as Movie)
                    Log.d("movie List", "" + movie)

                }
                Log.d("movie List", "" + dataRequest.currentState)

                viewController.onSucceed()
            }
            DataRequest.Status.ERROR -> {
                isLoading.setValue(false)
                viewController.onErrorOccurred("Error")
                Log.d("movie List", "" + dataRequest.currentState)
            }
        }
    }

    fun getMovieSearchStringLiveData(): MutableLiveData<String> {
        return movieSearchStringLiveData
    }

    fun getMovieIdLiveData(): MutableLiveData<String> {
        return movieIdLiveData
    }


    fun getIsLoading(): MutableLiveData<Boolean> {
        return isLoading
    }

    fun getMovieList(): MutableLiveData<List<Movie>> {
        return movieList
    }

    fun getMovie(): MutableLiveData<Movie> {
        return movie
    }

    enum class PresenterType {
        LIST,
        DETAILS
    }
}
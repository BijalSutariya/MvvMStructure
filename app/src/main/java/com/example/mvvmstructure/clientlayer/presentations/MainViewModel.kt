package com.example.mvvmstructure.clientlayer.presentations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mvvmstructure.domainlayer.model.Movie
import com.example.mvvmstructure.domainlayer.repositories.MovieRepository
import com.example.mvvmstructure.domainlayer.DataRequest
import javax.inject.Inject

class MainViewModel @Inject constructor(private var repository: MovieRepository) : ViewModel() {

    private var movieIdLiveData: MutableLiveData<String> = MutableLiveData()
    private var movieSearchStringLiveData: MutableLiveData<String> = MutableLiveData()

    fun getObservableMovieList():LiveData<DataRequest<List<Movie>>>{
        return Transformations.switchMap(movieSearchStringLiveData) { newSearchString ->
            repository.loadMovieList(
                newSearchString,
                false
            )
        }
    }

    fun getObservableMovie():LiveData<DataRequest<Movie>>{
        return Transformations.switchMap(movieIdLiveData) { newMovieId -> repository.loadMovie(newMovieId) }
    }
    fun getObservableMovieSearchString():MutableLiveData<String>{
        return movieSearchStringLiveData
    }

    fun getObservableMovieIdString():MutableLiveData<String>{
        return movieIdLiveData
    }
}
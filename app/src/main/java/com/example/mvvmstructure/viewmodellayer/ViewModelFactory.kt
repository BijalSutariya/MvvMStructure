package com.example.mvvmstructure.viewmodellayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmstructure.domainlayer.repositories.MovieRepository
import com.example.mvvmstructure.clientlayer.presentations.MainViewModel
import io.reactivex.annotations.NonNull
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory
/*
     * This app had only one view model. So, we just passed repository here.
     * But in production app we would have many repositories for many view models.
     *
     * Therefore we must provide repository from other way.*/
@Inject
constructor(private val repository: MovieRepository) : ViewModelProvider.Factory {


    @NonNull
    override fun <T : ViewModel> create(
        @NonNull modelClass: Class<T>
    ): T {
        /*
         * Here we can create various view Models.*/
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
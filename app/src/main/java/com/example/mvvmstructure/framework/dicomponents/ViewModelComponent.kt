package com.example.mvvmstructure.framework.dicomponents

import com.example.mvvmstructure.clientlayer.presentations.fragment.DetailsFragment
import com.example.mvvmstructure.clientlayer.presentations.fragment.ListFragment
import com.example.mvvmstructure.viewmodellayer.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ViewModelModule::class])
@Singleton
interface ViewModelComponent {
    fun doInjection(listFragment: ListFragment)
    fun doInjection(detailsFragment: DetailsFragment)

    /*
     * This component would provide movieViewModel module
     * through which we can get all the viewModels at their desired place.*/

}

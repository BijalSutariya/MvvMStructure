package com.example.mvvmstructure.framework

import android.app.Application
import com.example.mvvmstructure.framework.dicomponents.DataLayerComponent
import com.example.mvvmstructure.framework.dicomponents.ViewModelComponent
import com.example.mvvmstructure.domainlayer.datasources.database.DatabaseModule
import com.example.mvvmstructure.domainlayer.datasources.network.NetworkModule
import com.example.mvvmstructure.framework.dicomponents.DaggerDataLayerComponent
import com.example.mvvmstructure.framework.dicomponents.DaggerViewModelComponent
import com.example.mvvmstructure.viewmodellayer.ViewModelModule

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setDataLayerComponent()
        setViewModelComponent()
    }

    protected fun setDataLayerComponent() {
        dataLayerComponent = DaggerDataLayerComponent.builder()
            .networkModule(NetworkModule(this))
            .databaseModule(DatabaseModule(this))
            .build()
    }

    private fun setViewModelComponent() {
        viewModelComponent = DaggerViewModelComponent.builder()
            .viewModelModule(ViewModelModule())
            .build()
    }

    companion object {

        lateinit var dataLayerComponent: DataLayerComponent
            protected set

        lateinit var viewModelComponent: ViewModelComponent
            private set
    }
}
package com.example.mvvmstructure.clientlayer.presentations

import androidx.lifecycle.LifecycleOwner

interface ViewController {
    fun getLifeCycleOwner(): LifecycleOwner
    fun onErrorOccurred(message: String)

    fun onSucceed()

    fun onLoadingOccurred()

}
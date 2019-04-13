package com.example.mvvmstructure.domainlayer.datasources.network

import androidx.lifecycle.MutableLiveData
import com.example.mvvmstructure.domainlayer.model.Response
import com.example.mvvmstructure.domainlayer.DataRequest
import com.example.mvvmstructure.domainlayer.managers.DataManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class NetworkOperationObserver constructor(
    var responseFromNetwork: MutableLiveData<DataRequest<Response>>,
    var dataManager: DataManager<*, *>
) : Observer<Response> {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(response: Response) {
        responseFromNetwork.setValue(DataRequest.success(response))
    }

    override fun onError(e: Throwable) {
        this.responseFromNetwork.setValue(DataRequest.error( e.localizedMessage.toString(), null))
        dataManager.onFetchFailed(e)
    }

    override fun onComplete() {

    }
}

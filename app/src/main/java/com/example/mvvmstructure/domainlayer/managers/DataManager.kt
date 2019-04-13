package com.example.mvvmstructure.domainlayer.managers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mvvmstructure.domainlayer.DataRequest
import com.example.mvvmstructure.framework.TaskExecutors
import io.reactivex.annotations.Nullable

abstract class DataManager<RequestType, ResultType> protected constructor(var taskExecutors: TaskExecutors) {

    private val result: MediatorLiveData<DataRequest<ResultType>> = MediatorLiveData()

    init {
        result.setValue(DataRequest.loading(null))
        val sourceDatabase = this.loadFromDatabase()
        result.addSource(sourceDatabase) { data ->
            result.removeSource(sourceDatabase)
            if (shouldFetchData(data)) {
                fetchFromNetwork(sourceDatabase)
            } else {
                result.addSource(sourceDatabase) { newDataFromDatabase ->
                    setValue(
                        DataRequest.success(
                            newDataFromDatabase
                        )
                    )
                }
            }

        }
    }

    private fun fetchFromNetwork(sourceDatabase: LiveData<ResultType>) {
        val sourceNetwork = loadFromNetwork()
        result.addSource(sourceDatabase) {
            setValue(DataRequest.loading(null))
        }
        if (sourceNetwork != null) {
            result.addSource(sourceNetwork) { dataFromNetwork ->
                result.removeSource(sourceNetwork)
                result.removeSource(sourceDatabase)

                taskExecutors.diskOperationThread.execute {
                    val processedData = processResponse(dataFromNetwork)
                    if (processedData == null) {
                        taskExecutors.mainThread.execute { setValue(DataRequest.error("Not Found", null)) }
                        return@execute
                    }
                    clearPreviousData()
                    saveDataToDatabase(processedData)
                    taskExecutors.mainThread.execute {
                        result.addSource(
                            loadFromDatabase()
                        ) { newDataFromDatabase -> setValue(DataRequest.success(newDataFromDatabase)) }
                    }
                }


            }
        }
    }

    /*
    * This method works asynchronously by Room's own implementation.*/
    protected abstract fun loadFromDatabase(): LiveData<ResultType>

    /*
     * This method works asynchronously by RxPatten.*/
    protected abstract fun loadFromNetwork(): LiveData<RequestType>?

    protected abstract fun shouldFetchData(@Nullable data: ResultType): Boolean

    protected abstract fun saveDataToDatabase(data: ResultType)

    protected abstract fun clearPreviousData()

    fun onFetchFailed(throwable: Throwable) {
        setValue(DataRequest.error(throwable.localizedMessage, null))
    }

    /*
     * Updates the live data which we are interested in.*/
    private fun setValue(newValue: DataRequest<ResultType>) {
        if (result.value !== newValue) {
            result.value = newValue
        }
    }

    fun toLiveData(): LiveData<DataRequest<ResultType>> {
        return result
    }

    protected abstract fun processResponse(response: RequestType): ResultType?
}

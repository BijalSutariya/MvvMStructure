package com.example.mvvmstructure.domainlayer

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class DataRequest<T> private constructor(
    @param:NonNull @field:NonNull
    val currentState: Status,
    @param:Nullable @field:Nullable
    val data: T?,
    @param:Nullable @field:Nullable
    val message: String?) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {

        fun <T> success(@NonNull data: T): DataRequest<T> {
            return DataRequest(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, @Nullable data: T?): DataRequest<T> {
            return DataRequest(Status.ERROR, data, message)
        }

        fun <T> loading(@Nullable data: T?): DataRequest<T> {
            return DataRequest(Status.LOADING, data, null)
        }
    }
}

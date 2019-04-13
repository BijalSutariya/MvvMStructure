package com.example.mvvmstructure.domainlayer.datasources.network

import okhttp3.HttpUrl

object ApiUrls {

    private var httpUrl: HttpUrl.Builder? = null
    private val authority = "www.omdbapi.com"
    private val protocolScheme = "http"
    val apiToken = "13b629a1"

    private val httpUrlBuilder: HttpUrl.Builder
        get() {
            if (httpUrl == null) {
                httpUrl = HttpUrl.Builder()
            } else {
                httpUrl!!.build().newBuilder()
            }
            return httpUrl!!
        }

    val baseUrl: HttpUrl
        get() = httpUrlBuilder.scheme(protocolScheme).host(authority).build()
}

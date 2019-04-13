package com.example.mvvmstructure.domainlayer.datasources.network

import okhttp3.HttpUrl

object ApiUrls {

    private var httpUrl: HttpUrl.Builder? = null
    private const val authority = "www.omdbapi.com"
    private const val protocolScheme = "http"
    const val apiToken = "13b629a1"

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

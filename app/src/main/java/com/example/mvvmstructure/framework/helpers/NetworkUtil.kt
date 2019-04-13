package com.example.mvvmstructure.framework.helpers

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    fun isNetworkConnected(mContext: Context): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected || !activeNetwork.isConnected
    }
}
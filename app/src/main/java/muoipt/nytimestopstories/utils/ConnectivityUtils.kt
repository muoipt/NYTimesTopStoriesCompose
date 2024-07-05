package muoipt.nytimestopstories.utils

import android.content.Context
import android.net.ConnectivityManager
import muoipt.nytimestopstories.NYTApplication


object ConnectivityUtils {
    fun isConnected(): Boolean {
        val context = NYTApplication.instance()
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
            && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }
}

class NoConnectivityException: Exception("No internet connection")
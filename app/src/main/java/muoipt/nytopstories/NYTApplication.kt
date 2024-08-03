package muoipt.nytopstories

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NYTApplication: Application() {
    companion object {
        private lateinit var instance: NYTApplication
        fun instance(): NYTApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
package muoipt.nytimestopstories

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import muoipt.nytimestopstories.utils.TimerCustomDebugTree
import timber.log.Timber

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
        setupLogs()
    }

    private fun setupLogs() {
//        if (BuildConfig.DEBUG) {
        Timber.plant(TimerCustomDebugTree())
        Timber.d("Timber CustomDebugTree Planted")
//        }
    }
}
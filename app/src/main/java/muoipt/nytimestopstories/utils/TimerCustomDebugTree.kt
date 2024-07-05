package muoipt.nytimestopstories.utils

import timber.log.Timber

class TimerCustomDebugTree: Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return "*** NYT*** ${super.createStackElementTag(element)}"
    }
}
package muoipt.nyt.data.common

import android.util.Log
import muoipt.nyt.network.BuildConfig

enum class AppLogTag(val tag: String) {
    DEFAULT("nyt-app"),
    ARTICLE_LISTING("article_listing"),
    ARTICLE_BOOKMARK("article_bookmark")
}

object AppLog {

    fun listing(message: String, tag: AppLogTag = AppLogTag.ARTICLE_LISTING) {
        if (BuildConfig.DEBUG) {
            Log.e(tag.tag, message)
        }
    }

    fun bookmark(message: String, tag: AppLogTag = AppLogTag.ARTICLE_BOOKMARK) {
        if (BuildConfig.DEBUG) {
            Log.e(tag.tag, message)
        }
    }
}
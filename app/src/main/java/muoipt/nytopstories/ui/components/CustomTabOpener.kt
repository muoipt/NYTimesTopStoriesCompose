package muoipt.nytopstories.ui.components

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent

object CustomTabOpener {

    fun openCustomTabFromUrl(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()

        if (context is Activity) {
            customTabsIntent.launchUrl(context, Uri.parse(url))
        } else {
            Toast.makeText(
                context,
                "Can not open the url from non activity context",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
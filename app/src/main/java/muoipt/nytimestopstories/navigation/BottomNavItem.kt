package muoipt.nytimestopstories.navigation

import muoipt.nytimestopstories.R

sealed class BottomNavItem(var title: String, var icon: Int? = null, var screenRoute: String) {

    object Listing: BottomNavItem(
        title = "Places",
        icon = R.drawable.ic_listing_inactive,
        screenRoute = "home"
    )

    object Bookmark: BottomNavItem(
        title = "Profile",
        icon = R.drawable.ic_bookmark_inactive,
        screenRoute = "profile"
    )
}
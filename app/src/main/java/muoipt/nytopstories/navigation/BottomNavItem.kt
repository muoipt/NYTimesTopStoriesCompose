package muoipt.nytopstories.navigation

import muoipt.nytopstories.R

sealed class BottomNavItem(var title: String, var icon: Int? = null, var screenRoute: String) {

    object Listing: BottomNavItem(
        title = "Listing",
        icon = R.drawable.ic_listing_inactive,
        screenRoute = "listing"
    )

    object Bookmark: BottomNavItem(
        title = "Bookmark",
        icon = R.drawable.ic_bookmark_inactive,
        screenRoute = "bookmark"
    )
}
package muoipt.nytopstories.ui.bookmark

import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.model.ArticleData
import muoipt.nytopstories.ui.base.UIAction
import muoipt.nytopstories.ui.base.UIState
import muoipt.nytopstories.ui.base.VMState
import muoipt.nytopstories.ui.listing.ArticleUiData

sealed class BookmarkListingAction: UIAction {
    data object LoadBookmark: BookmarkListingAction()
    data class UpdateBookmarkArticle(val articleTitle: String): BookmarkListingAction()
}

sealed class BookmarkListingUIState(
    open val articlesList: List<ArticleUiData> = listOf()
): UIState {
    data object Default: BookmarkListingUIState()
    data object Loading: BookmarkListingUIState()
    data object Empty: BookmarkListingUIState()
    class LoadBookmarkSuccess(override val articlesList: List<ArticleUiData>): BookmarkListingUIState(articlesList)
    class Error(val error: ArticleError): BookmarkListingUIState()
}

data class BookmarkListingVMState(
    val vmData: ArticleVMData = ArticleVMData(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val error: ArticleError? = null
): VMState() {
    override fun toUIState(): BookmarkListingUIState {
        val uiData = vmData.toUiData()
        return when {
            isLoading -> BookmarkListingUIState.Loading
            isEmpty -> BookmarkListingUIState.Empty
            error != null -> BookmarkListingUIState.Error(error)
            vmData.articles.isNotEmpty() -> BookmarkListingUIState.LoadBookmarkSuccess(uiData)
            else -> BookmarkListingUIState.Default
        }
    }
}

data class ArticleVMData(
    val articles: List<ArticleData> = listOf()
) {
    fun toUiData() = articles.map {
        ArticleUiData(
            title = it.title,
            section = it.section,
            url = it.url,
            byline = it.byline,
            multimedia = it.multimedia,
            updatedDate = it.updatedDate,
            isBookmarked = it.isBookmarked
        )
    }
}
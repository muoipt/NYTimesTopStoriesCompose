package muoipt.nytopstories.ui.bookmark

import muoipt.nyt.data.common.ArticleError
import muoipt.nytopstories.ui.base.State
import muoipt.nytopstories.ui.base.UIAction
import muoipt.nytopstories.ui.listing.ArticleUiData

sealed class BookmarkListingAction: UIAction {
    data object LoadBookmark: BookmarkListingAction()
    data class UpdateBookmarkArticle(val articleTitle: String): BookmarkListingAction()
}

sealed class BookmarkListingVMState {
    data object Loading: BookmarkListingVMState()
    data object Empty: BookmarkListingVMState()
    class LoadBookmarkSuccess(val articlesList: List<ArticleUiData>): BookmarkListingVMState()
    data object BookmarkUpdated: BookmarkListingVMState()
    class Error(val error: ArticleError): BookmarkListingVMState()

    fun toUIState(currentState: BookmarkListingUIState): BookmarkListingUIState {
        return when (this) {
            is Loading -> currentState.copy(isLoading = true)
            is Error -> currentState.copy(isLoading = false, error = error)
            is Empty -> currentState.copy(isLoading = false, isEmpty = true)
            is BookmarkUpdated -> currentState.copy(isLoading = false, bookmarkUpdated = true)
            is LoadBookmarkSuccess -> currentState.copy(
                isLoading = false,
                isEmpty = false,
                error = null,
                vmData = ArticleUIData(articlesList)
            )
        }
    }
}

data class BookmarkListingUIState(
    val vmData: ArticleUIData = ArticleUIData(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val bookmarkUpdated: Boolean = false,
    val error: ArticleError? = null,
): State

data class ArticleUIData(
    val articles: List<ArticleUiData> = listOf()
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
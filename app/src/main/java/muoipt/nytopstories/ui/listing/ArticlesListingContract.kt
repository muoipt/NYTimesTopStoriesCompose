package muoipt.nytopstories.ui.listing

import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.model.ArticleData
import muoipt.nyt.model.MultimediaData
import muoipt.nytopstories.ui.base.Action
import muoipt.nytopstories.ui.base.State

sealed class ArticlesListingAction: Action {
    data class LoadArticles(val withRefresh: Boolean): ArticlesListingAction()
    data class UpdateBookmarkArticle(val articleTitle: String): ArticlesListingAction()
}

sealed class ArticlesListingVMState {
    data object Loading: ArticlesListingVMState()
    class LoadArticlesSuccess(val articlesList: List<ArticleData>): ArticlesListingVMState()
    data object BookmarkUpdated: ArticlesListingVMState()
    class Error(val error: ArticleError): ArticlesListingVMState()

    fun toUIState(currentState: ArticlesListingUIState): ArticlesListingUIState {
        return when (this) {
            is Loading -> currentState.copy(isLoading = true)
            is Error -> currentState.copy(isLoading = false, error = error)
            is LoadArticlesSuccess -> currentState.copy(
                isLoading = false,
                error = null,
                articles = articlesList.map { it.toArticleUiData() },
            )

            else -> currentState
        }
    }
}

data class ArticlesListingUIState(
    val articles: List<ArticleUiData> = listOf(),
    val isLoading: Boolean = false,
    val error: ArticleError? = null
): State

data class ArticleUiData(
    val title: String = "",
    val section: String = "",
    val url: String = "",
    val byline: String = "",
    val multimedia: List<MultimediaData>? = null,
    val updatedDate: String = "",
    val isBookmarked: Boolean = false
)

fun ArticleData.toArticleUiData(): ArticleUiData {
    return ArticleUiData(
        title = title,
        section = section,
        url = url,
        byline = byline,
        multimedia = multimedia,
        updatedDate = updatedDate,
        isBookmarked = isBookmarked
    )
}
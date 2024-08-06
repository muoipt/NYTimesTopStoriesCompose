package muoipt.nytopstories.ui.detail

import muoipt.nyt.data.common.ArticleError
import muoipt.nytopstories.ui.base.Action
import muoipt.nytopstories.ui.listing.ArticleUiData

sealed class ArticleDetailAction: Action {
    data class LoadArticleDetail(val articleTitle: String): ArticleDetailAction()
    data class UpdateBookmark(val articleTitle: String): ArticleDetailAction()
}

sealed class ArticleDetailVMState {
    data object Loading: ArticleDetailVMState()
    data class LoadSuccess(val article: ArticleUiData): ArticleDetailVMState()
    data object BookmarkUpdated: ArticleDetailVMState()
    data class Error(val error: ArticleError): ArticleDetailVMState()

    fun toUIState(currentState: ArticleDetailUIState): ArticleDetailUIState{
        return when(this) {
            is Loading -> currentState.copy(isLoading = true)
            is Error -> currentState.copy(error = error)
            is BookmarkUpdated -> currentState.copy(bookmarkUpdated = true)
            is LoadSuccess -> currentState.copy(articleDetail = article)
            else -> currentState
        }
    }
}

data class ArticleDetailUIState(
    val isLoading: Boolean = false,
    val error: ArticleError? = null,
    val bookmarkUpdated: Boolean = false,
    val articleDetail: ArticleUiData = ArticleUiData()
)
package muoipt.nytopstories.ui.listing

import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.model.ArticleData
import muoipt.nyt.model.MultimediaData
import muoipt.nytopstories.ui.base.UIAction
import muoipt.nytopstories.ui.base.UIState
import muoipt.nytopstories.ui.base.VMState

sealed class ArticlesListingAction: UIAction {
    data object LoadArticles: ArticlesListingAction()
    data class UpdateBookmarkArticle(val articleId: Int): ArticlesListingAction()
}

sealed class ArticlesListingUIState(
    open val articlesList: List<ArticleUiData> = listOf()
): UIState {
    data object Default: ArticlesListingUIState()
    data object Loading: ArticlesListingUIState()
    class LoadArticlesSuccess(override val articlesList: List<ArticleUiData>): ArticlesListingUIState(articlesList)
    class Error(val error: ArticleError): ArticlesListingUIState()
}

data class ArticlesListingVMState(
    val vmData: ArticleVMData = ArticleVMData(),
    val isLoading: Boolean = false,
    val error: ArticleError? = null
): VMState() {
    override fun toUIState(): ArticlesListingUIState {
        val uiData = vmData.toUiData()
        return when {
            isLoading -> ArticlesListingUIState.Loading
            error != null -> ArticlesListingUIState.Error(error)
            vmData.articles.isNotEmpty() -> ArticlesListingUIState.LoadArticlesSuccess(uiData)
            else -> ArticlesListingUIState.Default
        }
    }
}

data class ArticleUiData(
    val id: Int = 0,
    val section: String = "",
    val title: String = "",
    val url: String = "",
    val byline: String = "",
    val multimedia: List<MultimediaData>? = null,
    val updatedDate: String = "",
    val isBookmarked: Boolean = false
)

data class ArticleVMData(
    val articles: List<ArticleData> = listOf()
) {
    fun toUiData() = articles.map {
        ArticleUiData(
            id = it.id,
            section = it.section,
            title = it.title,
            url = it.url,
            byline = it.byline,
            multimedia = it.multimedia,
            updatedDate = it.updatedDate,
            isBookmarked = it.isBookmarked
        )
    }
}
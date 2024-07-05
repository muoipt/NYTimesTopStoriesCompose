package muoipt.nytimestopstories.listing

import muoipt.model.ArticleData
import muoipt.model.MultimediaData
import muoipt.nytimestopstories.base.UIAction
import muoipt.nytimestopstories.base.UIState
import muoipt.nytimestopstories.base.VMState

sealed class ArticlesListingAction: UIAction {
    data object LoadArticles: ArticlesListingAction()
}

sealed class ArticlesListingUIState(
    open val articlesList: List<ArticleUiData> = listOf()
): UIState {
    data object Default: ArticlesListingUIState()
    data object Loading: ArticlesListingUIState()
    class LoadArticlesSuccess(override val articlesList: List<ArticleUiData>): ArticlesListingUIState(articlesList)
    class Error(val error: ArticlesListingError): ArticlesListingUIState()
}

data class ArticlesListingVMState(
    val vmData: ArticleVMData = ArticleVMData(),
    val isLoading: Boolean = false,
    val error: ArticlesListingError? = null
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
    val byline: String = "",
    val multimedia: List<MultimediaData> = listOf(),
)

data class ArticleVMData(
    val articles: List<ArticleData> = listOf()
) {
    fun toUiData() = articles.map {
        ArticleUiData(
            id = it.id,
            section = it.section,
            title = it.title,
            byline = it.byline,
            multimedia = it.multimedia
        )
    }
}

enum class ArticlesListingErrorCode {
    LoadArticlesException,
    LoadArticlesNotFound
}

data class ArticlesListingError(
    val errorCode: ArticlesListingErrorCode,
    val errorMessage: String? = null
)
package muoipt.nytopstories.ui.detail

import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.model.ArticleData
import muoipt.nyt.model.MultimediaData
import muoipt.nytopstories.ui.base.UIAction
import muoipt.nytopstories.ui.base.UIState
import muoipt.nytopstories.ui.base.VMState

sealed class ArticleDetailAction: UIAction {
    data class LoadArticleDetail(val articleTitle: String): ArticleDetailAction()
    data class UpdateBookmark(val articleTitle: String): ArticleDetailAction()
}

sealed class ArticleDetailUIState(
    open val articleDetailData: ArticleUiData = ArticleUiData()
): UIState {
    data object Default: ArticleDetailUIState()
    data object Loading: ArticleDetailUIState()
    class LoadArticleSuccess(override val articleDetailData: ArticleUiData):
        ArticleDetailUIState(articleDetailData)

    class Error(val error: ArticleError): ArticleDetailUIState()
}

data class ArticleDetailVMState(
    val vmData: ArticleVMData = ArticleVMData(),
    val isLoading: Boolean = false,
    val error: ArticleError? = null
): VMState() {
    override fun toUIState(): ArticleDetailUIState {
        val uiData = vmData.toUiData()
        return when {
            isLoading -> ArticleDetailUIState.Loading
            error != null -> ArticleDetailUIState.Error(error)
            vmData.articleDetail.title.isNotEmpty() -> ArticleDetailUIState.LoadArticleSuccess(uiData)
            else -> ArticleDetailUIState.Default
        }
    }
}

data class ArticleUiData(
    val title: String = "",
    val section: String = "",
    val url: String = "",
    val byline: String = "",
    val multimedia: List<MultimediaData>? = null,
    val updatedDate: String = "",
    val isBookmarked: Boolean = false
)

data class ArticleVMData(
    val articleDetail: ArticleData = ArticleData()
) {
    fun toUiData() = ArticleUiData(
        title = articleDetail.title,
        section = articleDetail.section,
        url = articleDetail.url,
        byline = articleDetail.byline,
        multimedia = articleDetail.multimedia,
        updatedDate = articleDetail.updatedDate,
        isBookmarked = articleDetail.isBookmarked
    )
}
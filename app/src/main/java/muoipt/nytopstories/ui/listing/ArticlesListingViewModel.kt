package muoipt.nytopstories.ui.listing

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetArticlesListUseCase
import muoipt.nytopstories.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesListingViewModel @Inject constructor(
    private val getArticlesListUseCase: GetArticlesListUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): BaseViewModel<ArticlesListingAction, ArticlesListingUIState, ArticlesListingVMState>(
    initUIState = ArticlesListingUIState.Default, initVMState = ArticlesListingVMState()
) {
    override fun handleAction(action: ArticlesListingAction) {
        when (action) {
            is ArticlesListingAction.LoadArticles -> {
                loadArticles()
            }

            is ArticlesListingAction.UpdateBookmarkArticle -> {
                updateBookmarkArticle(action.articleId)
            }
        }
    }

    private fun loadArticles() {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            AppLog.listing("saveBookmarkedArticle exception = $exception")

            if (exception is ArticleError) {
                vmStates.update {
                    it.copy(
                        isLoading = false, error = exception
                    )
                }
            } else {
                vmStates.update {
                    it.copy(
                        isLoading = false,
                        error = ArticleError(ArticleErrorCode.LoadArticlesException)
                    )
                }
            }
        }) {

            vmStates.update {
                it.copy(isLoading = true)
            }

            val articlesList = getArticlesListUseCase.getArticles().firstOrNull()

            if (articlesList.isNullOrEmpty()) {
                vmStates.update {
                    it.copy(
                        isLoading = false,
                        error = ArticleError(errorCode = ArticleErrorCode.LoadArticlesNotFound)
                    )
                }
            } else {
                val articleVMData = ArticleVMData(
                    articles = articlesList
                )

                AppLog.listing("loadArticles articles[0] = ${articlesList[0].toDisplayString()}")

                vmStates.update {
                    it.copy(isLoading = false, vmData = articleVMData)
                }
            }
        }
    }

    private fun updateBookmarkArticle(articleId: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            AppLog.listing("saveBookmarkedArticle exception = $exception")

            if (exception is ArticleError) {
                vmStates.update {
                    it.copy(
                        isLoading = false, error = exception
                    )
                }
            } else {
                vmStates.update {
                    it.copy(
                        isLoading = false,
                        error = ArticleError(ArticleErrorCode.BookmarkArticleException)
                    )
                }
            }
        }) {
            AppLog.listing("saveBookmarkedArticle articleId = $articleId")
            bookmarkArticleUseCase.updateBookmarkArticle(articleId)
        }
    }
}
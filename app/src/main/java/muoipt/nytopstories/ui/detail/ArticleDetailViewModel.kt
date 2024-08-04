package muoipt.nytopstories.ui.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetArticleDetailUseCase
import muoipt.nytopstories.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): BaseViewModel<ArticleDetailAction, ArticleDetailUIState, ArticleDetailVMState>(
    initUIState = ArticleDetailUIState.Default, initVMState = ArticleDetailVMState()
) {
    override fun handleAction(action: ArticleDetailAction) {
        when (action) {
            is ArticleDetailAction.LoadArticleDetail -> {
                loadArticleDetail(action.articleTitle)
            }

            is ArticleDetailAction.UpdateBookmark -> {
                updateBookmarkArticle(action.articleTitle)
            }
        }
    }

    private fun loadArticleDetail(title: String) {
        viewModelScope.launch {
            getArticleDetailUseCase.getArticleDetail(title)
                .onStart {
                    vmStates.update {
                        it.copy(isLoading = true)
                    }
                }
                .catch { exception ->
                    AppLog.listing("loadArticleDetail exception = $exception")

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
                                error = ArticleError(ArticleErrorCode.LoadBookmarkException)
                            )
                        }
                    }
                }
                .collect { article ->
                    if (article == null) {
                        vmStates.update {
                            it.copy(
                                isLoading = false,
                                error = ArticleError(ArticleErrorCode.LoadArticleDetailNotFound)
                            )
                        }
                    } else {
                        val articleVMData = ArticleVMData(
                            articleDetail = article
                        )

                        AppLog.listing("loadArticleDetail article = ${article.toDisplayString()}")

                        vmStates.update {
                            it.copy(isLoading = false, vmData = articleVMData)
                        }
                    }
                }
        }
    }

    private fun updateBookmarkArticle(articleTitle: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            AppLog.listing("updateBookmarkArticle exception = $exception")

            if (exception is ArticleError) {
                vmStates.update {
                    it.copy(
                        error = exception
                    )
                }
            } else {
                vmStates.update {
                    it.copy(
                        error = ArticleError(ArticleErrorCode.BookmarkArticleException)
                    )
                }
            }
        }) {
            AppLog.listing("updateBookmarkArticle articleTitle = $articleTitle")
            bookmarkArticleUseCase.updateBookmarkArticle(articleTitle)
        }
    }
}
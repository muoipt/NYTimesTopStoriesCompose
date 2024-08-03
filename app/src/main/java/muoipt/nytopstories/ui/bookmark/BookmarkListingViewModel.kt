package muoipt.nytopstories.ui.bookmark

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
import muoipt.nyt.data.usecases.GetBookmarkListUseCase
import muoipt.nytopstories.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkListingViewModel @Inject constructor(
    private val getBookmarkListUseCase: GetBookmarkListUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): BaseViewModel<BookmarkListingAction, BookmarkListingUIState, BookmarkListingVMState>(
    initUIState = BookmarkListingUIState.Default, initVMState = BookmarkListingVMState()
) {
    override fun handleAction(action: BookmarkListingAction) {
        when (action) {
            is BookmarkListingAction.LoadBookmark -> {
                loadBookmarkedArticles()
            }

            is BookmarkListingAction.UpdateBookmarkArticle -> {
                updateBookmarkArticle(action.articleTitle)
            }
        }
    }

    private fun loadBookmarkedArticles() {
        viewModelScope.launch {
            getBookmarkListUseCase.getBookmark()
                .onStart {
                    vmStates.update {
                        it.copy(isLoading = true)
                    }
                }
                .catch { exception ->
                    AppLog.listing("loadBookmarkedArticles exception = $exception")

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
                .collect { articlesList ->
                    if (articlesList.isNullOrEmpty()) {
                        vmStates.update {
                            it.copy(
                                isLoading = false,
                                isEmpty = true
                            )
                        }
                    } else {
                        val articleVMData = ArticleVMData(
                            articles = articlesList
                        )

                        AppLog.listing("loadBookmarkedArticles articles[0] = ${articlesList[0].toDisplayString()}")

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
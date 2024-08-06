package muoipt.nytopstories.ui.bookmark

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetBookmarkListUseCase
import muoipt.nytopstories.ui.base.BaseMVIViewModel
import muoipt.nytopstories.ui.listing.toArticleUiData
import javax.inject.Inject

@HiltViewModel
class BookmarkListingViewModel @Inject constructor(
    private val getBookmarkListUseCase: GetBookmarkListUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): BaseMVIViewModel<BookmarkListingAction, BookmarkListingVMState, BookmarkListingUIState>(
    initialUIState = BookmarkListingUIState()
) {

    override fun initUIState(): StateFlow<BookmarkListingUIState> {
        return merge(
            loadBookmarks(), actionFlow.toVMState().filterNotNull()
        ).scan(initialUIState) { currentState, vmState ->
            vmState.toUIState(currentState)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, initialUIState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun Flow<BookmarkListingAction>.toVMState(): Flow<BookmarkListingVMState?> {
        return flatMapLatest { action ->
            when (action) {
                is BookmarkListingAction.LoadBookmark -> loadBookmarks()

                is BookmarkListingAction.UpdateBookmarkArticle -> {
                    updateBookmarkArticle(action.articleTitle)
                }
            }
        }
    }

    private fun loadBookmarks(): Flow<BookmarkListingVMState> {
        return flow {
            getBookmarkListUseCase.getBookmark().onStart {
                emit(BookmarkListingVMState.Loading)
            }.catch { exception ->
                emit(
                    BookmarkListingVMState.Error(
                        ArticleError(
                            ArticleErrorCode.LoadArticlesException, exception.message
                        )
                    )
                )
            }.collect { bookmarks ->
                AppLog.listing("loadBookmarks collect bookmarks")

                if (bookmarks == null) {
                    emit(BookmarkListingVMState.Empty)
                } else {
                    emit(BookmarkListingVMState.LoadBookmarkSuccess(bookmarks.map { it.toArticleUiData() }))
                }
            }
        }
    }

    private fun updateBookmarkArticle(articleTitle: String): Flow<BookmarkListingVMState> {
        return flow {
            try {
                bookmarkArticleUseCase.updateBookmarkArticle(articleTitle)
                emit(BookmarkListingVMState.BookmarkUpdated)
            } catch (exception: Exception) {
                emit(
                    BookmarkListingVMState.Error(
                        ArticleError(
                            ArticleErrorCode.BookmarkArticleException, exception.message
                        )
                    )
                )
            }
        }
    }
}
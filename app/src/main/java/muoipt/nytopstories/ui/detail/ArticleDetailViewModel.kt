package muoipt.nytopstories.ui.detail

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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetArticleDetailUseCase
import muoipt.nytopstories.ui.base.BaseMVIViewModel
import muoipt.nytopstories.ui.listing.toArticleUiData
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): BaseMVIViewModel<ArticleDetailAction, ArticleDetailVMState, ArticleDetailUIState>(
    initialUIState = ArticleDetailUIState()
) {
    override fun initUIState(): StateFlow<ArticleDetailUIState> {
        return actionFlow.toVMState().filterNotNull()
            .scan(initialUIState) { currentState, partialState ->
                partialState.toUIState(currentState)
            }.stateIn(viewModelScope, SharingStarted.Eagerly, initialUIState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun Flow<ArticleDetailAction>.toVMState(): Flow<ArticleDetailVMState?> {
        return flatMapLatest { action ->
            when (action) {
                is ArticleDetailAction.LoadArticleDetail -> loadArticlesDetail(
                    action.articleTitle
                )

                is ArticleDetailAction.UpdateBookmark -> {
                    updateBookmarkArticle(action.articleTitle)
                }
            }
        }
    }

    private fun loadArticlesDetail(title: String): Flow<ArticleDetailVMState> {
        return flow {
            getArticleDetailUseCase.getArticleDetail(title).onStart {
                emit(ArticleDetailVMState.Loading)
            }.catch { exception ->
                emit(
                    ArticleDetailVMState.Error(
                        ArticleError(
                            ArticleErrorCode.LoadArticlesException, exception.message
                        )
                    )
                )
            }.collect { articles ->
                AppLog.listing("loadArticles collect articles")
                if (articles != null) {
                    emit(ArticleDetailVMState.LoadSuccess(articles.toArticleUiData()))
                }
            }
        }
    }

    private fun updateBookmarkArticle(articleTitle: String): Flow<ArticleDetailVMState> {
        return flow {
            try {
                bookmarkArticleUseCase.updateBookmarkArticle(articleTitle)
                emit(ArticleDetailVMState.BookmarkUpdated)
            } catch (exception: Exception) {
                emit(
                    ArticleDetailVMState.Error(
                        ArticleError(
                            ArticleErrorCode.BookmarkArticleException, exception.message
                        )
                    )
                )
            }
        }
    }
}
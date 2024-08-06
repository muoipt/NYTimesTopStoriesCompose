package muoipt.nytopstories.ui.listing

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
import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetArticlesListUseCase
import muoipt.nytopstories.ui.base.BaseMVIViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesListingViewModel @Inject constructor(
    private val getArticlesListUseCase: GetArticlesListUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): BaseMVIViewModel<ArticlesListingAction, ArticlesListingVMState, ArticlesListingUIState>(
    initialUIState = ArticlesListingUIState()
) {
    public override fun initUIState(): StateFlow<ArticlesListingUIState> {
        return merge(
            loadArticles(true), actionFlow.toVMState().filterNotNull()
        ).scan(initialUIState) { currentState, vmStateState ->
            vmStateState.toUIState(currentState)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, initialUIState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun Flow<ArticlesListingAction>.toVMState(): Flow<ArticlesListingVMState?> {
        return flatMapLatest { action ->
            when (action) {
                is ArticlesListingAction.LoadArticles -> loadArticles(
                    action.withRefresh
                )

                is ArticlesListingAction.UpdateBookmarkArticle -> {
                    updateBookmarkArticle(action.articleTitle)
                }
            }
        }
    }

    private fun loadArticles(withRefresh: Boolean): Flow<ArticlesListingVMState> {
        return flow {
            getArticlesListUseCase.getArticles(withRefresh).onStart {
                emit(ArticlesListingVMState.Loading)
            }.catch { exception ->
                emit(
                    ArticlesListingVMState.Error(
                        ArticleError(
                            ArticleErrorCode.LoadArticlesException, exception.message
                        )
                    )
                )
            }.collect { articles ->
//                AppLog.listing("loadArticles collect articles")
//                AppLog.listing(
//                    "loadArticles collect articles.first = ${
//                        articles.first().toDisplayString()
//                    }"
//                )
                emit(ArticlesListingVMState.LoadArticlesSuccess(articles))
            }
        }
    }

    private fun updateBookmarkArticle(articleTitle: String): Flow<ArticlesListingVMState> {
        return flow {
            try {
                bookmarkArticleUseCase.updateBookmarkArticle(articleTitle)
                emit(ArticlesListingVMState.BookmarkUpdated)
            } catch (exception: Exception) {
                emit(
                    ArticlesListingVMState.Error(
                        ArticleError(
                            ArticleErrorCode.BookmarkArticleException, exception.message
                        )
                    )
                )
            }
        }
    }
}
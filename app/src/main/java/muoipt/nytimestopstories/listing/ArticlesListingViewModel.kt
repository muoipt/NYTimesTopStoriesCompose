package muoipt.nytimestopstories.listing

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muoipt.data.usecases.GetArticlesListUseCase
import muoipt.nytimestopstories.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesListingViewModel @Inject constructor(
    private val getArticlesListUseCase: GetArticlesListUseCase
): BaseViewModel<ArticlesListingAction, ArticlesListingUIState, ArticlesListingVMState>(
    initUIState = ArticlesListingUIState.Default,
    initVMState = ArticlesListingVMState()
) {
    override fun handleAction(action: ArticlesListingAction) {
        when (action) {
            is ArticlesListingAction.LoadArticlesAction -> {
                loadArticles()
            }
        }
    }

    private fun loadArticles() {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            vmStates.update {
                it.copy(
                    isLoading = false, error = ArticlesListingError(
                        errorCode = ArticlesListingErrorCode.LoadArticlesException,
                        errorMessage = exception.message
                    )
                )
            }
        }) {
            getArticlesListUseCase.getArticles().onStart {
                vmStates.update {
                    it.copy(isLoading = true)
                }
            }.collect { articles ->
                if (articles.isEmpty()) {
                    vmStates.update {
                        it.copy(
                            isLoading = false,
                            error = ArticlesListingError(errorCode = ArticlesListingErrorCode.LoadArticlesNotFound)
                        )
                    }
                } else {
                    val articleVMData = ArticleVMData(
                        articles = articles
                    )

                    vmStates.update {
                        it.copy(isLoading = false, vmData = articleVMData)
                    }
                }
            }
        }
    }
}
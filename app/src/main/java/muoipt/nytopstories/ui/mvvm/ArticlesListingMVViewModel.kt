package muoipt.nytopstories.ui.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetArticlesListUseCase
import muoipt.nytopstories.ui.listing.ArticleUiData
import javax.inject.Inject

data class ArticleListingUIState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val articles: List<ArticleUiData> = listOf()
)

@HiltViewModel
class ArticlesListingMVViewModel @Inject constructor(
    private val getArticlesListUseCase: GetArticlesListUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
): ViewModel() {
    private var _uiState: MutableStateFlow<ArticleListingUIState> =
        MutableStateFlow(ArticleListingUIState())
    val uiState: StateFlow<ArticleListingUIState> = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ArticleListingUIState())

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            getArticlesListUseCase.getArticles(true).onStart {
                _uiState.update {
                    it.copy(isLoading = true)
                }
            }.catch { exception ->
                _uiState.update {
                    it.copy(isLoading = false, error = exception)
                }
            }.collect { data ->
                _uiState.update {
                    it.copy(isLoading = false, articles = data.map { article ->
                        ArticleUiData(
                            title = article.title,
                            section = article.section,
                            url = article.url,
                            byline = article.byline,
                            multimedia = article.multimedia,
                            updatedDate = article.updatedDate,
                            isBookmarked = article.isBookmarked
                        )
                    })
                }
            }
        }
    }

    fun updateBookmarkArticle(articleTitle: String) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                _uiState.update {
                    it.copy(error = exception)
                }
            }
        ) {
            bookmarkArticleUseCase.updateBookmarkArticle(articleTitle)
        }
    }
}
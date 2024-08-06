package muoipt.nytopstories.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import muoipt.nyt.data.common.AppLog
import muoipt.nytopstories.ui.base.BaseUi
import muoipt.nytopstories.ui.components.CircleProgressBar
import muoipt.nytopstories.ui.components.CustomTabOpener
import muoipt.nytopstories.ui.listing.ArticleUiData

@Composable
fun ArticleDetailScreen(
    modifier: Modifier, viewModel: ArticleDetailViewModel = hiltViewModel(),
    title: String
) {
    val state by viewModel.uiStates.collectAsState()
    val uiState = state as? ArticleDetailUIState

    val article by remember(uiState?.articleDetailData) {
        derivedStateOf { uiState?.articleDetailData ?: ArticleUiData() }
    }

    when (state) {
        is ArticleDetailUIState.Default -> {
            LaunchedEffect(Unit) {
                viewModel.sendAction(ArticleDetailAction.LoadArticleDetail(title))
            }
        }

        is ArticleDetailUIState.Loading -> {
            CircleProgressBar()
        }

        is ArticleDetailUIState.LoadArticleSuccess -> {
            BaseUi(content = {
                SetupUi(modifier, article) {
                    viewModel.sendAction(ArticleDetailAction.UpdateBookmark(it))
                }
            }, snackBarMessage = null)
        }

        is ArticleDetailUIState.Error -> {
            BaseUi(content = {
                SetupUi(modifier, article) {
                    viewModel.sendAction(ArticleDetailAction.UpdateBookmark(it))
                }
            }, snackBarMessage = getErrorMessage(state as ArticleDetailUIState.Error))
        }

        else -> {}
    }
}

@Composable
private fun getErrorMessage(state: ArticleDetailUIState.Error): String? {
    val errorMessage by remember(state) {
        derivedStateOf {
            state.error.errorMessage
        }
    }

    return errorMessage
}

@Composable
private fun SetupUi(
    modifier: Modifier,
    article: ArticleUiData,
    onBookmarkUpdate: (articleTitle: String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        // use web browser to show the article url
        // todo need to remove the empty page when back from browser
        // maybe just show some available UI with a bookmark button
        // and showmore... to open the webpage
        AppLog.listing("CustomTabOpener.openCustomTabFromUrl article.url = ${article.url}")
        CustomTabOpener.openCustomTabFromUrl(LocalContext.current, article.url)
    }
}
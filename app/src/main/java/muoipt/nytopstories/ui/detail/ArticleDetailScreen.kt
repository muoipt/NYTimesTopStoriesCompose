package muoipt.nytopstories.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import muoipt.nyt.data.common.AppLog
import muoipt.nytopstories.ui.components.CircleProgressBar
import muoipt.nytopstories.ui.components.CustomTabOpener
import muoipt.nytopstories.ui.listing.ArticleUiData

@Composable
fun ArticleDetailScreen(
    modifier: Modifier, viewModel: ArticleDetailViewModel = hiltViewModel(),
    title: String
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = state as? ArticleDetailUIState

    val isLoading by remember(uiState?.isLoading) {
        derivedStateOf { uiState?.isLoading }
    }

    val error by remember(uiState?.error) {
        derivedStateOf { uiState?.error }
    }

    val articleDetail by remember(uiState?.articleDetail) {
        derivedStateOf { uiState?.articleDetail }
    }

    if (isLoading == true) {
        CircleProgressBar()
    } else {
        if (error != null) {
            ErrorUI(modifier, error?.errorMessage)
        }

        if (articleDetail == null) {
            EmptyUi(modifier)
        } else {
            SetupUi(modifier, articleDetail!!) {
                viewModel.handleAction(ArticleDetailAction.UpdateBookmark(it))
            }
        }
    }
}

@Composable
private fun ErrorUI(modifier: Modifier, error: String?) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = error ?: "An error occurred!")
    }
}

@Composable
private fun EmptyUi(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No article available")
    }
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
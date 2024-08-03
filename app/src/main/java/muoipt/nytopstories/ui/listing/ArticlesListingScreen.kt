package muoipt.nytopstories.ui.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.model.MultimediaData
import muoipt.nytopstories.R
import muoipt.nytopstories.ui.base.BaseUi
import muoipt.nytopstories.ui.base.UIState
import muoipt.nytopstories.ui.components.CircleProgressBar

@Composable
fun ArticlesListingScreen(
    modifier: Modifier, viewModel: ArticlesListingViewModel = hiltViewModel()
) {
    val state by viewModel.uiStates.collectAsStateWithLifecycle()

    val articlesList = (state as? ArticlesListingUIState)?.articlesList ?: emptyList()

    when (state) {
        is ArticlesListingUIState.Default -> {
            LaunchedEffect(Unit) {
                viewModel.sendAction(ArticlesListingAction.LoadArticles)
            }
        }

        is ArticlesListingUIState.Loading -> {
            CircleProgressBar()
        }

        else -> {}
    }

    BaseUi(content = {
        SetupUi(modifier, articlesList) {
            viewModel.sendAction(ArticlesListingAction.UpdateBookmarkArticle(it))
        }
    }, snackBarMessage = getErrorMessage(state))
}

@Composable
fun getErrorMessage(state: UIState): String? {
    val loadArticlesNotFoundErrorMsg = stringResource(id = R.string.error_no_articles)

    val errorMessage by remember(state) {
        derivedStateOf {
            if (state is ArticlesListingUIState.Error) {
                when (state.error.errorCode) {
                    ArticleErrorCode.LoadArticlesNotFound -> loadArticlesNotFoundErrorMsg
                    else -> {
                        state.error.errorMessage
                    }
                }

            } else null
        }
    }

    return errorMessage
}

@Composable
fun SetupUi(
    modifier: Modifier, articlesList: List<ArticleUiData>, onBookmarkUpdate: (articleId: Int) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
    ) {

        items(articlesList.size) { index ->
            ArticleItemView(articlesList[index], onBookmarkUpdate)
        }
    }

    LaunchedEffect(articlesList) {
        coroutineScope.launch {
            listState.scrollToItem(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }
}

@Composable
fun ArticleItemView(articleUiData: ArticleUiData, onBookmarkUpdate: (articleId: Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        articleUiData.multimedia?.firstOrNull()?.url.let { imageUrl ->
            AsyncImage(
                model = imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            modifier = Modifier.padding(16.dp), color = Color.Black, text = articleUiData.title
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                color = Color.DarkGray, text = articleUiData.updatedDate
            )
            Icon(
                modifier = Modifier.clickable { onBookmarkUpdate(articleUiData.id) },
                imageVector = if (articleUiData.isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "favorite"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun ArticleItemPreview() {
    ArticleItemView(
        articleUiData = ArticleUiData(
            title = "title", updatedDate = "2024-08-02T17:30:13-04:00", multimedia = listOf(
                MultimediaData(
                    url = "https://static01.nyt.com/images/2024/08/02/multimedia/02pol-othering-harris-topart-zmcw/02pol-othering-harris-topart-zmcw-superJumbo.jpg"
                )
            )
        )
    ) {

    }
}
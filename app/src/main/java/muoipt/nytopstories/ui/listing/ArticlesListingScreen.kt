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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nytopstories.R
import muoipt.nytopstories.ui.base.BaseUi
import muoipt.nytopstories.ui.base.UIState
import muoipt.nytopstories.ui.components.CircleProgressBar
import muoipt.nytopstories.ui.components.CustomTabOpener
import muoipt.nytopstories.ui.components.OnLifecycleEvent

@Composable
fun ArticlesListingScreen(
    modifier: Modifier, viewModel: ArticlesListingViewModel = hiltViewModel(),
    onDetailClicked: (title: String) -> Unit
) {
    val state by viewModel.uiStates.collectAsState()
    val uiState = state as? ArticlesListingUIState

    val articlesList by remember(uiState?.articlesList) {
        derivedStateOf { uiState?.articlesList ?: listOf() }
    }

    when (state) {
        is ArticlesListingUIState.Default -> {
            LaunchedEffect(Unit) {
                viewModel.sendAction(ArticlesListingAction.LoadArticles(true))
            }
        }

        is ArticlesListingUIState.Loading -> {
            CircleProgressBar()
        }

        else -> {}
    }

    BaseUi(content = {
        SetupUi(modifier, articlesList, onDetailClicked) {
            viewModel.sendAction(ArticlesListingAction.UpdateBookmarkArticle(it))
        }
    }, snackBarMessage = getErrorMessage(state))

    OnLifecycleEvent(LocalLifecycleOwner.current) { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.sendAction(ArticlesListingAction.LoadArticles(false))
            }

            else -> {}
        }
    }
}

@Composable
private fun getErrorMessage(state: UIState): String? {
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
private fun SetupUi(
    modifier: Modifier,
    articlesList: List<ArticleUiData>,
    onDetailClicked: (title: String) -> Unit,
    onBookmarkUpdate: (articleTitle: String) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(articlesList) {
        AppLog.listing("listState.firstVisibleItemIndex = ${listState.firstVisibleItemIndex}")
        coroutineScope.launch {
            listState.animateScrollToItem(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxWidth()
    ) {
        items(articlesList.size) { index ->
            ArticleItemView(articlesList[index], onBookmarkUpdate) {
                onDetailClicked(it)
            }
        }
    }
}

@Composable
private fun ArticleItemView(
    articleUiData: ArticleUiData,
    onBookmarkUpdate: (articleTitle: String) -> Unit,
    onDetailClicked: (title: String) -> Unit
) {

    val context = LocalContext.current

    val bookmarkStatus = remember(articleUiData) {
        mutableStateOf(articleUiData.isBookmarked)
    }

    AppLog.listing("bookmarkStatus = $bookmarkStatus")
    val currentBookmarkStatus = bookmarkStatus.value
    AppLog.listing("currentBookmarkStatus = $currentBookmarkStatus")

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
//            onDetailClicked(articleUiData.title)
            CustomTabOpener.openCustomTabFromUrl(context, articleUiData.url)
        }) {
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
                modifier = Modifier.clickable {
                    bookmarkStatus.value = !currentBookmarkStatus
                    onBookmarkUpdate(articleUiData.title)
                    AppLog.listing("new bookmarkStatus.value = ${bookmarkStatus.value}")

                },
                imageVector = if (bookmarkStatus.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "favorite"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
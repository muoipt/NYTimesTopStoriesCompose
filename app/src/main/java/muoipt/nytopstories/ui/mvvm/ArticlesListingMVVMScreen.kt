package muoipt.nytopstories.ui.mvvm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import muoipt.nyt.data.common.AppLog
import muoipt.nytopstories.ui.components.CircleProgressBar
import muoipt.nytopstories.ui.components.CustomTabOpener
import muoipt.nytopstories.ui.listing.ArticleUiData

@Composable
fun ArticlesListingMVVMScreen(
    modifier: Modifier, viewModel: ArticlesListingMVViewModel = hiltViewModel(),
    onDetailClicked: (title: String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isLoading by remember(uiState.isLoading) {
        derivedStateOf { uiState.isLoading }
    }

    val error by remember(uiState.error) {
        derivedStateOf { uiState.error }
    }

    val articles by remember(uiState.articles) {
        derivedStateOf { uiState.articles }
    }

    if (isLoading) {
        CircleProgressBar()
    }

    if (error != null) {
        ErrorUI(modifier, error!!.message)
    }

    if (articles.isEmpty()) {
        EmptyUi(modifier)
    } else {
        SetupUi(modifier = modifier, articlesList = articles, onDetailClicked = {}) {
            viewModel.updateBookmarkArticle(it)
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
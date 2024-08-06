package muoipt.nytopstories.ui.bookmark

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.model.MultimediaData
import muoipt.nytopstories.R
import muoipt.nytopstories.ui.components.CircleProgressBar
import muoipt.nytopstories.ui.listing.ArticleUiData

@Composable
fun BookmarkListingScreen(
    modifier: Modifier, viewModel: BookmarkListingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = state as? BookmarkListingUIState

    val isLoading by remember(uiState?.isLoading) {
        derivedStateOf { uiState?.isLoading }
    }

    val error by remember(uiState?.error) {
        derivedStateOf { uiState?.error }
    }

    val bookmarkedArticles by remember(uiState?.vmData?.articles) {
        derivedStateOf { uiState?.vmData?.articles ?: listOf() }
    }

    if (isLoading == true) {
        CircleProgressBar()
    } else {
        if (error != null) {
            ErrorUI(modifier, error?.errorMessage)
        }

        if (bookmarkedArticles.isEmpty()) {
            EmptyBookmarkUi(modifier)
        } else {
            SetupUi(modifier, bookmarkedArticles) {
                viewModel.handleAction(BookmarkListingAction.UpdateBookmarkArticle(it))
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
private fun EmptyBookmarkUi(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.padding(16.dp),
            color = Color.Black,
            text = stringResource(id = R.string.error_no_bookmark),
            style = TextStyle(fontSize = 24.sp)
        )
    }
}

@Composable
private fun SetupUi(
    modifier: Modifier,
    articlesList: List<ArticleUiData>,
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
            ArticleItemView(articlesList[index], onBookmarkUpdate)
        }
    }
}

@Composable
private fun ArticleItemView(
    articleUiData: ArticleUiData,
    onBookmarkUpdate: (articleTitle: String) -> Unit
) {

    val bookmarkStatus = remember(articleUiData) {
        mutableStateOf(articleUiData.isBookmarked)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            articleUiData.multimedia?.firstOrNull()?.url.let { imageUrl ->
                AsyncImage(
                    model = imageUrl, contentDescription = null, modifier = Modifier.width(150.dp)
                )
            }
            Text(
                modifier = Modifier.padding(16.dp), color = Color.Black, text = articleUiData.title
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                color = Color.DarkGray, text = articleUiData.updatedDate
            )
            Icon(
                modifier = Modifier.clickable {
                    onBookmarkUpdate(articleUiData.title)
                },
                imageVector = if (bookmarkStatus.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "favorite"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(color = Color.Gray)
        )
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
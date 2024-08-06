package muoipt.nytopstories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.GetArticlesListUseCase
import muoipt.nyt.model.ArticleData
import muoipt.nytopstories.ui.listing.ArticlesListingUIState
import muoipt.nytopstories.ui.listing.ArticlesListingViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesListingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getArticlesListUseCase: GetArticlesListUseCase

    @Mock
    private lateinit var bookmarkArticleUseCase: BookmarkArticleUseCase

    private lateinit var viewModel: ArticlesListingViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = ArticlesListingViewModel(getArticlesListUseCase, bookmarkArticleUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initUIState`() = runTest {
        // Arrange
        val articles = listOf(ArticleData(title = "Test Article"))
        val articlesFlow = flow {
            emit(articles)
        }
        `when`(getArticlesListUseCase.getArticles(true)).thenReturn(articlesFlow)

        // Act
        val uiState = viewModel.initUIState().first()

        // Assert
        assert(uiState is ArticlesListingUIState)
        verify(getArticlesListUseCase).getArticles(true)
    }
}
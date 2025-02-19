package muoipt.nyt.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import muoipt.common.IoDispatcher
import muoipt.nyt.data.common.AppLog
import muoipt.nyt.data.common.ArticleError
import muoipt.nyt.data.common.ArticleErrorCode
import muoipt.nyt.data.common.DataStrategy
import muoipt.nyt.data.mapper.toDataModel
import muoipt.nyt.data.mapper.toEntity
import muoipt.nyt.database.dao.ArticleDao
import muoipt.nyt.database.dao.upsert
import muoipt.nyt.model.ArticleData
import muoipt.nyt.network.api.ArticleApiInterface
import javax.inject.Inject

interface ArticleRepo {
    fun getArticles(strategy: DataStrategy = DataStrategy.AUTO): Flow<List<ArticleData>>
    suspend fun updateBookmarkedArticle(articleTitle: String)
    fun getBookmarkArticles(strategy: DataStrategy = DataStrategy.AUTO): Flow<List<ArticleData>?>
    fun getArticleDetail(title: String): Flow<ArticleData?>
}

class ArticleRepoImpl @Inject constructor(
    private val articleRemoteApi: ArticleApiInterface,
    private val articleLocalApi: ArticleDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ArticleRepo {
    override fun getArticles(strategy: DataStrategy): Flow<List<ArticleData>> {
        val willFetchFromRemote = when (strategy) {
            DataStrategy.REMOTE,
            DataStrategy.AUTO -> true

            else -> false
        }

        return articleLocalApi.getAll()
            .onStart { if (willFetchFromRemote) fetchArticles() }
            .map { response -> response.map { it.toDataModel() } }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateBookmarkedArticle(articleTitle: String) {
        withContext(ioDispatcher) {
            AppLog.listing("ArticleRepoImpl saveBookmarkedArticle articleTitle = $articleTitle")

            val article = articleLocalApi.getByTitle(articleTitle).firstOrNull()
                ?: throw ArticleError(ArticleErrorCode.BookmarkArticleNotFound)

            AppLog.listing(
                "ArticleRepoImpl saveBookmarkedArticle article = ${
                    article.toDataModel().toDisplayString()
                }"
            )

            val currentBookmarkStatus = (article.isBookmarked == 1)
            val newBookmarkStatus = !currentBookmarkStatus

            val newArticle = article.copy(isBookmarked = if (newBookmarkStatus) 1 else 0)

            AppLog.listing(
                "ArticleRepoImpl saveBookmarkedArticle newArticle = ${
                    newArticle.toDataModel().toDisplayString()
                }"
            )

            articleLocalApi.upsert(newArticle)
        }
    }

    override fun getBookmarkArticles(strategy: DataStrategy): Flow<List<ArticleData>?> {
        return articleLocalApi.getAllBookmark()
            .map { response -> response?.map { it.toDataModel() } }
            .flowOn(ioDispatcher)
    }

    private suspend fun fetchArticles() {
        val remoteResponse = articleRemoteApi.getArticles()
        val articleEntities = remoteResponse.results.map { it.toEntity() }

        val bookmarkedArticle = articleLocalApi.getAllBookmark().firstOrNull()
        AppLog.listing("fetchArticles articleLocalApi.getAllBookmark() = $bookmarkedArticle")

        val mergedArticles = articleEntities.map { article ->
            val isBookmarkedArticle = bookmarkedArticle?.any { it.title == article.title }
            AppLog.listing("isBookmarkedArticle = $isBookmarkedArticle for title = ${article.title}")
            article.copy(isBookmarked = if (isBookmarkedArticle == true) 1 else 0)
        }

        articleLocalApi.upsert(mergedArticles)
    }

    override fun getArticleDetail(title: String): Flow<ArticleData?> {
        return articleLocalApi.getByTitle(title).map { it?.toDataModel() }
    }
}
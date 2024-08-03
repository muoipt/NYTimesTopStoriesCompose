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
    suspend fun updateBookmarkedArticle(articleId: Int)
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

    override suspend fun updateBookmarkedArticle(articleId: Int) {
        withContext(ioDispatcher) {
            AppLog.listing("ArticleRepoImpl saveBookmarkedArticle articleId = $articleId")

            val article = articleLocalApi.getById(articleId).firstOrNull()
                ?: throw ArticleError(ArticleErrorCode.BookmarkArticleNotFound)

            AppLog.listing("ArticleRepoImpl saveBookmarkedArticle article = ${article.toDataModel().toDisplayString()}")

            val currentBookmarkStatus = (article.isBookmarked == 1)
            val newBookmarkStatus = !currentBookmarkStatus

            val newArticle = article.copy(isBookmarked = if (newBookmarkStatus) 1 else 0)

            AppLog.listing("ArticleRepoImpl saveBookmarkedArticle newArticle = ${newArticle.toDataModel().toDisplayString()}")

            articleLocalApi.upsert(newArticle)
        }
    }

    private suspend fun fetchArticles() {
        articleLocalApi.deleteAll()

        val remoteResponse = articleRemoteApi.getArticles()
        val articleEntities = remoteResponse.results.map { it.toEntity() }

        articleLocalApi.upsert(articleEntities)
    }
}
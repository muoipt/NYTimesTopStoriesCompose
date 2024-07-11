package muoipt.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
//import muoipt.common.IoDispatcher
import muoipt.core.network.api.article.ArticleApiInterface
import muoipt.core.network.model.Article
import muoipt.data.common.DataStrategy
import muoipt.data.mapper.toDataModel
import muoipt.data.mapper.toEntity
import muoipt.database.dao.ArticleDao
import muoipt.database.dao.upsert
import muoipt.model.ArticleData
import javax.inject.Inject

interface ArticleRepo {
    suspend fun getArticles(strategy: DataStrategy = DataStrategy.AUTO): Flow<List<ArticleData>>
}

class ArticleRepoImpl @Inject constructor(
    private val articleRemoteApi: ArticleApiInterface,
    private val articleLocalApi: ArticleDao,
//    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ArticleRepo {
    override suspend fun getArticles(strategy: DataStrategy): Flow<List<ArticleData>> {
        val willFetchFromRemote = when (strategy) {
            DataStrategy.REMOTE,
            DataStrategy.AUTO -> true

            else -> false
        }

        return articleLocalApi.getAll()
            .onStart { if (willFetchFromRemote) fetchArticles() }
            .map { response -> response.map { it.toDataModel() } }
            .flowOn(Dispatchers.IO)
    }

    private suspend fun fetchArticles() {
        val remoteResponse = articleRemoteApi.getArticles()
        val articleEntities = remoteResponse.results.map { it.toEntity() }

        articleLocalApi.upsert(articleEntities)
    }
}
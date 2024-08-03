package muoipt.nyt.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import muoipt.nyt.data.common.DataStrategy
import muoipt.nyt.data.mapper.toDataModel
import muoipt.nyt.data.mapper.toEntity
import muoipt.nyt.database.dao.ArticleDao
import muoipt.nyt.database.dao.upsert
import muoipt.nyt.model.ArticleData
import muoipt.nyt.network.api.ArticleApiInterface
import javax.inject.Inject

interface ArticleRepo {
    suspend fun getArticles(strategy: DataStrategy = DataStrategy.AUTO): Flow<List<ArticleData>>
}

class ArticleRepoImpl @Inject constructor(
    private val articleRemoteApi: ArticleApiInterface,
    private val articleLocalApi: ArticleDao,
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
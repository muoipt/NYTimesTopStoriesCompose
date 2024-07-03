package muoipt.data.repositories

import muoipt.core.network.api.article.ArticleApiInterface
import muoipt.model.ArticleData
import javax.inject.Inject

interface ArticleRepo {
    suspend fun getArticles(): List<ArticleData>
}

class ArticleRepoImpl @Inject constructor(
    private val articleRemoteApi: ArticleApiInterface
): ArticleRepo {
    override suspend fun getArticles(): List<ArticleData> {
        val remoteResponse = articleRemoteApi.getArticles()
        return remoteResponse.results.map { it.toDataModel() }
    }
}
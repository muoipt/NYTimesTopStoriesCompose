package muoipt.core.network.api.article

import muoipt.core.network.api.model.ArticleResponse
import muoipt.core.network.api.utils.ApiUtils.tryCatchApiException
import javax.inject.Inject

class ArticleApi @Inject constructor(
    private val articleApiService: ArticleApiService
): ArticleApiInterface {
    override suspend fun getArticles(): ArticleResponse {
        return tryCatchApiException { articleApiService.getArticles() }
    }
}
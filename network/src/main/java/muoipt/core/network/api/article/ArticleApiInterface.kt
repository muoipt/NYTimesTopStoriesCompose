package muoipt.core.network.api.article

import muoipt.core.network.model.ArticleResponse

interface ArticleApiInterface {
    suspend fun getArticles(): ArticleResponse
}
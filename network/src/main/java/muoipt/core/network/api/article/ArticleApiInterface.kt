package muoipt.core.network.api.article

import muoipt.core.network.api.article.model.ArticleResponse

interface ArticleApiInterface {
    suspend fun getArticles(): ArticleResponse
}
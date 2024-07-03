package muoipt.core.network.api.article

import muoipt.core.network.api.article.model.ArticleResponse
import retrofit2.http.GET

interface ArticleApiService {

    @GET("/topstories/v2/home.json?")
    suspend fun getArticles(): ArticleResponse
}
package muoipt.nyt.network.api

import muoipt.nyt.model.ArticleResponse
import muoipt.nyt.network.utils.ApiUtils.tryCatchApiException
import retrofit2.http.GET
import javax.inject.Inject

interface ArticleApiInterface {
    suspend fun getArticles(): ArticleResponse
}

class ArticleApi @Inject constructor(
    private val articleApiService: ArticleApiService
): ArticleApiInterface {
    override suspend fun getArticles(): ArticleResponse {
        return tryCatchApiException { articleApiService.getArticles() }
    }
}


// API from: https://developer.nytimes.com/docs/top-stories-product/1/overview
interface ArticleApiService {

    @GET("/svc/topstories/v2")
    suspend fun getArticles(): ArticleResponse
}
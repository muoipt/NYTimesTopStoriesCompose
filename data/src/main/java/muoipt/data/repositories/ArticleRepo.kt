package muoipt.data.repositories

import muoipt.core.network.api.article.ArticleApiInterface
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


data class ArticleData(
    val section: String = "",
    val subsection: String = "",
    val title: String = "",
    val abstract: String = "",
    val url: String = "",
    val byline: String = "",
    val itemType: String = "",
    val updatedDate: String = "",
    val createdDate: String = "",
    val publishedDate: String = "",
    val materialTypeFacet: String = "",
    val kicker: String = "",
    val desFacet: List<String> = listOf(),
    val orgFacet: List<String> = listOf(),
    val perFacet: List<String> = listOf(),
    val geoFacet: List<String> = listOf(),
    val multimedia: List<MultimediaData> = listOf(),
    val shortUrl: String = ""
)

data class MultimediaData(
    val url: String = "",
    val format: String = "",
    val height: Int = 0,
    val width: Int = 0,
    val type: String = "",
    val subtype: String = "",
    val caption: String = ""
)
package muoipt.core.network.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleResponse(
    @field:Json(name = "status") val status: String = "",
    @field:Json(name = "copyright") val copyright: String = "",
    @field:Json(name = "section") val section: String = "",
    @field:Json(name = "last_updated") val lastUpdated: String = "",
    @field:Json(name = "num_results") val numResults: Int = 0,
    @field:Json(name = "results") val results: List<Article> = listOf()
)

@JsonClass(generateAdapter = true)
data class Article(
    @field:Json(name = "section") val section: String = "",
    @field:Json(name = "subsection") val subsection: String = "",
    @field:Json(name = "title") val title: String = "",
    @field:Json(name = "abstract") val abstract: String = "",
    @field:Json(name = "url") val url: String = "",
    @field:Json(name = "byline") val byline: String = "",
    @field:Json(name = "item_type") val itemType: String = "",
    @field:Json(name = "updated_date") val updatedDate: String = "",
    @field:Json(name = "created_date") val createdDate: String = "",
    @field:Json(name = "published_date") val publishedDate: String = "",
    @field:Json(name = "material_type_facet") val materialTypeFacet: String = "",
    @field:Json(name = "kicker") val kicker: String = "",
    @field:Json(name = "des_facet") val desFacet: List<String> = listOf(),
    @field:Json(name = "org_facet") val orgFacet: List<String> = listOf(),
    @field:Json(name = "per_facet") val perFacet: List<String> = listOf(),
    @field:Json(name = "geo_facet") val geoFacet: List<String> = listOf(),
    @field:Json(name = "multimedia") val multimedia: List<Multimedia> = listOf(),
    @field:Json(name = "short_url") val shortUrl: String = ""
)

@JsonClass(generateAdapter = true)
data class Multimedia(
    @field:Json(name = "url") val url: String = "",
    @field:Json(name = "format") val format: String = "",
    @field:Json(name = "height") val height: Int = 0,
    @field:Json(name = "width") val width: Int = 0,
    @field:Json(name = "type") val type: String = "",
    @field:Json(name = "subtype") val subtype: String = "",
    @field:Json(name = "caption") val caption: String = ""
)

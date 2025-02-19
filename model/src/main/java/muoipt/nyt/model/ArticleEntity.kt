package muoipt.nyt.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "article",
)
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "section")
    val section: String = "",

    @ColumnInfo(name = "subsection")
    val subsection: String = "",

    @ColumnInfo(name = "abstract")
    val abstract: String = "",

    @ColumnInfo(name = "url")
    val url: String = "",

    @ColumnInfo(name = "byline")
    val byline: String = "",

    @ColumnInfo(name = "item_type")
    val itemType: String = "",

    @ColumnInfo(name = "updated_date")
    val updatedDate: String = "",

    @ColumnInfo(name = "created_date")
    val createdDate: String = "",

    @ColumnInfo(name = "published_date")
    val publishedDate: String = "",

    @ColumnInfo(name = "material_type_facet")
    val materialTypeFacet: String = "",

    @ColumnInfo(name = "kicker")
    val kicker: String = "",

    @ColumnInfo(name = "des_facet")
    val desFacet: List<String> = listOf(),

    @ColumnInfo(name = "org_facet")
    val orgFacet: List<String> = listOf(),

    @ColumnInfo(name = "per_facet")
    val perFacet: List<String> = listOf(),

    @ColumnInfo(name = "geo_facet")
    val geoFacet: List<String> = listOf(),

    @ColumnInfo(name = "multimedia")
    val multimedia: List<MultimediaEntity>? = null,

    @ColumnInfo(name = "shortUrl")
    val shortUrl: String = "",

    @ColumnInfo(name = "isBookmarked")
    val isBookmarked: Int = 0
)

@JsonClass(generateAdapter = true)
data class MultimediaEntity(
    @ColumnInfo(name = "url")
    val url: String = "",

    @ColumnInfo(name = "format")
    val format: String = "",

    @ColumnInfo(name = "height")
    val height: Int = 0,

    @ColumnInfo(name = "width")
    val width: Int = 0,

    @ColumnInfo(name = "type")
    val type: String = "",

    @ColumnInfo(name = "subtype")
    val subtype: String = "",

    @ColumnInfo(name = "caption")
    val caption: String = "",

    @ColumnInfo(name = "copyright")
    val copyright: String = ""
)
package muoipt.nyt.model

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
    val multimedia: List<MultimediaData>? = null,
    val shortUrl: String = "",
    val isBookmarked: Boolean = false
){
    fun toDisplayString(): String{
        return "title = $title - isBookmarked = $isBookmarked"
    }
}
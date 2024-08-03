package muoipt.nyt.data.mapper

import muoipt.nyt.model.Article
import muoipt.nyt.model.ArticleData
import muoipt.nyt.model.ArticleEntity
import muoipt.nyt.model.Multimedia
import muoipt.nyt.model.MultimediaData
import muoipt.nyt.model.MultimediaEntity


fun Article.toEntity() = ArticleEntity(
    section = section,
    subsection = subsection,
    title = title,
    abstract = abstract,
    url = url,
    byline = byline,
    itemType = itemType,
    updatedDate = updatedDate,
    createdDate = createdDate,
    publishedDate = publishedDate,
    materialTypeFacet = materialTypeFacet,
    kicker = kicker,
    desFacet = desFacet,
    orgFacet = orgFacet,
    perFacet = perFacet,
    geoFacet = geoFacet,
    multimedia = multimedia?.map { it.toEntity() },
    shortUrl = shortUrl
)

fun Multimedia.toEntity() = MultimediaEntity(
    url = url,
    format = format,
    height = height,
    width = width,
    type = type,
    subtype = subtype,
    caption = caption
)

fun ArticleEntity.toDataModel() = ArticleData(
    id = id,
    section = section,
    subsection = subsection,
    title = title,
    abstract = abstract,
    url = url,
    byline = byline,
    itemType = itemType,
    updatedDate = updatedDate,
    createdDate = createdDate,
    publishedDate = publishedDate,
    materialTypeFacet = materialTypeFacet,
    kicker = kicker,
    desFacet = desFacet,
    orgFacet = orgFacet,
    perFacet = perFacet,
    geoFacet = geoFacet,
    multimedia = multimedia?.map { it.toDataModel() },
    shortUrl = shortUrl,
    isBookmarked = isBookmarked == 1
)

fun MultimediaEntity.toDataModel() = MultimediaData(
    url = url,
    format = format,
    height = height,
    width = width,
    type = type,
    subtype = subtype,
    caption = caption
)
package muoipt.data.mapper

import muoipt.core.network.model.Article
import muoipt.core.network.model.Multimedia
import muoipt.database.entity.ArticleEntity
import muoipt.database.entity.MultimediaEntity
import muoipt.model.ArticleData
import muoipt.model.MultimediaData

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
    multimedia = multimedia.map { it.toEntity() },
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
    multimedia = multimedia.map { it.toDataModel() },
    shortUrl = shortUrl
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
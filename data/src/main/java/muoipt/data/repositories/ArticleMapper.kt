package muoipt.data.repositories

import muoipt.core.network.model.Article
import muoipt.core.network.model.Multimedia

fun Article.toDataModel() = ArticleData(
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

fun Multimedia.toDataModel() = MultimediaData(
    url = url,
    format = format,
    height = height,
    width = width,
    type = type,
    subtype = subtype,
    caption = caption
)
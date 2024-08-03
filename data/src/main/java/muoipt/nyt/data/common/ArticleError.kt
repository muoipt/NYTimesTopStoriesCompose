package muoipt.nyt.data.common

enum class ArticleErrorCode {
    LoadArticlesException,
    LoadArticlesNotFound,
    LoadBookmarkNotFound,
    LoadBookmarkException,
    BookmarkArticleNotFound,
    BookmarkArticleException
}

class ArticleError(
    val errorCode: ArticleErrorCode,
    val errorMessage: String? = null
): Exception()
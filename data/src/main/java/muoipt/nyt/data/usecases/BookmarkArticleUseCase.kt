package muoipt.nyt.data.usecases

import muoipt.nyt.data.repositories.ArticleRepo
import javax.inject.Inject

interface BookmarkArticleUseCase {
    suspend fun updateBookmarkArticle(articleTitle: String)
}

class BookmarkArticleUseCaseImpl @Inject constructor(
    private val articleRepo: ArticleRepo
): BookmarkArticleUseCase {
    override suspend fun updateBookmarkArticle(articleTitle: String) {
        articleRepo.updateBookmarkedArticle(articleTitle)
    }
}
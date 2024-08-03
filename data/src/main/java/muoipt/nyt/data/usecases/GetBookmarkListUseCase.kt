package muoipt.nyt.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.nyt.data.repositories.ArticleRepo
import muoipt.nyt.model.ArticleData
import javax.inject.Inject

interface GetBookmarkListUseCase {
    suspend fun getBookmark(): Flow<List<ArticleData>?>
}

class GetBookmarkListUseCaseImpl @Inject constructor(
    private val articleRepo: ArticleRepo
): GetBookmarkListUseCase {
    override suspend fun getBookmark(): Flow<List<ArticleData>?> {
        return articleRepo.getBookmarkArticles()
    }
}
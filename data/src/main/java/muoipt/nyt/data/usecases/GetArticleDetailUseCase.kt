package muoipt.nyt.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.nyt.data.repositories.ArticleRepo
import muoipt.nyt.model.ArticleData
import javax.inject.Inject

interface GetArticleDetailUseCase {
    suspend fun getArticleDetail(title: String): Flow<ArticleData?>
}

class GetArticleDetailUseCaseImpl @Inject constructor(
    private val articleRepo: ArticleRepo
): GetArticleDetailUseCase {
    override suspend fun getArticleDetail(title: String): Flow<ArticleData?> {
        return articleRepo.getArticleDetail(title)
    }
}
package muoipt.data.usecases

import muoipt.data.repositories.ArticleData
import muoipt.data.repositories.ArticleRepo
import javax.inject.Inject

interface GetArticleUseCase {
    suspend fun getArticles(): List<ArticleData>
}

class GetArticleUseCaseImpl @Inject constructor(
    private val articleRepo: ArticleRepo
): GetArticleUseCase {
    override suspend fun getArticles(): List<ArticleData> {
        return articleRepo.getArticles()
    }
}
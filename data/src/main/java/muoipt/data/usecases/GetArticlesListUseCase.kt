package muoipt.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.data.repositories.ArticleRepo
import muoipt.model.ArticleData
import javax.inject.Inject

interface GetArticlesListUseCase {
    suspend fun getArticles(): Flow<List<ArticleData>>
}

class GetArticlesListUseCaseImpl @Inject constructor(
    private val articleRepo: ArticleRepo
): GetArticlesListUseCase {
    override suspend fun getArticles(): Flow<List<ArticleData>> {
        return articleRepo.getArticles()
    }
}
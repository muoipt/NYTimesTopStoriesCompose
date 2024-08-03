package muoipt.nyt.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.nyt.data.common.DataStrategy
import muoipt.nyt.data.repositories.ArticleRepo
import muoipt.nyt.model.ArticleData
import javax.inject.Inject

interface GetArticlesListUseCase {
    suspend fun getArticles(withRefresh: Boolean): Flow<List<ArticleData>>
}

class GetArticlesListUseCaseImpl @Inject constructor(
    private val articleRepo: ArticleRepo
): GetArticlesListUseCase {
    override suspend fun getArticles(withRefresh: Boolean): Flow<List<ArticleData>> {
        val dataStrategy = if(withRefresh){
            DataStrategy.REMOTE
        } else {
            DataStrategy.LOCAL
        }
        return articleRepo.getArticles(dataStrategy)
    }
}
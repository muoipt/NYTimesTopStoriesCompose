package muoipt.nyt.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.nyt.data.repositories.ArticleRepo
import muoipt.nyt.data.repositories.ArticleRepoImpl
import muoipt.nyt.data.usecases.GetArticlesListUseCase
import muoipt.nyt.data.usecases.GetArticlesListUseCaseImpl


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindArticleRepo(articleRepoImpl: ArticleRepoImpl): ArticleRepo

    @Binds
    fun bindGetArticleUseCase(getArticleUseCaseImpl: GetArticlesListUseCaseImpl): GetArticlesListUseCase
}
package muoipt.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.data.repositories.ArticleRepo
import muoipt.data.repositories.ArticleRepoImpl
import muoipt.data.usecases.GetArticleUseCase
import muoipt.data.usecases.GetArticleUseCaseImpl


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindArticleRepo(articleRepoImpl: ArticleRepoImpl): ArticleRepo

    @Binds
    fun bindGetArticleUseCase(getArticleUseCaseImpl: GetArticleUseCaseImpl): GetArticleUseCase
}
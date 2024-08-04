package muoipt.nyt.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.nyt.data.repositories.ArticleRepo
import muoipt.nyt.data.repositories.ArticleRepoImpl
import muoipt.nyt.data.usecases.BookmarkArticleUseCase
import muoipt.nyt.data.usecases.BookmarkArticleUseCaseImpl
import muoipt.nyt.data.usecases.GetArticleDetailUseCase
import muoipt.nyt.data.usecases.GetArticleDetailUseCaseImpl
import muoipt.nyt.data.usecases.GetArticlesListUseCase
import muoipt.nyt.data.usecases.GetArticlesListUseCaseImpl
import muoipt.nyt.data.usecases.GetBookmarkListUseCase
import muoipt.nyt.data.usecases.GetBookmarkListUseCaseImpl


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindArticleRepo(articleRepoImpl: ArticleRepoImpl): ArticleRepo

    @Binds
    fun bindGetArticleUseCase(getArticleUseCaseImpl: GetArticlesListUseCaseImpl): GetArticlesListUseCase

    @Binds
    fun bindBookmarkArticleUseCase(bookmarkArticleUseCaseImpl: BookmarkArticleUseCaseImpl): BookmarkArticleUseCase

    @Binds
    fun bindGetBookmarkListUseCase(getBookmarkListUseCaseImpl: GetBookmarkListUseCaseImpl): GetBookmarkListUseCase

    @Binds
    fun bindGetArticleDetailUseCase(getArticleDetailUseCaseImpl: GetArticleDetailUseCaseImpl): GetArticleDetailUseCase

}
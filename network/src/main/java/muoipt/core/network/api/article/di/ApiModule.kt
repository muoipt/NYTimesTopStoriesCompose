package muoipt.core.network.api.article.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.core.network.api.article.ArticleApi
import muoipt.core.network.api.article.ArticleApiInterface


@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {
    @Binds
    fun bindArticleApi(impl: ArticleApi): ArticleApiInterface
}
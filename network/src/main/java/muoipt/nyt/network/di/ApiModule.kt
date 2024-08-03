package muoipt.nyt.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.nyt.network.api.ArticleApi
import muoipt.nyt.network.api.ArticleApiInterface

@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {
    @Binds
    fun bindArticleApi(impl: ArticleApi): ArticleApiInterface
}
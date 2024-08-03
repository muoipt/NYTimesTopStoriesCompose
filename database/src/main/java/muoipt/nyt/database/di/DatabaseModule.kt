package muoipt.nyt.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import muoipt.nyt.database.ArticleDb
import muoipt.nyt.database.dao.ArticleDao

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
    @Provides
    fun database(@ApplicationContext context: Context): ArticleDb = ArticleDb.getInstance(context)

    @Provides
    fun articleDao(db: ArticleDb): ArticleDao = db.articleDao()

}
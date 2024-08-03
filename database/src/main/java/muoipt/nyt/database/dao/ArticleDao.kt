package muoipt.nyt.database.dao

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import muoipt.nyt.model.ArticleEntity

interface ArticleDao: BaseDao<ArticleEntity> {
    fun getAll(): Flow<List<ArticleEntity>>

    fun getAllBookmark(): Flow<List<ArticleEntity>?>

    fun getByTitle(title: String): Flow<ArticleEntity?>

    suspend fun deleteAll()
}

@Dao
abstract class ArticleDaoImpl: ArticleDao {
    @androidx.room.Query("SELECT * FROM article")
    abstract override fun getAll(): Flow<List<ArticleEntity>>

    @androidx.room.Query("SELECT * FROM article WHERE isBookmarked = 1")
    abstract override fun getAllBookmark(): Flow<List<ArticleEntity>?>

    @androidx.room.Query("SELECT * FROM article WHERE title = :title")
    abstract override fun getByTitle(title: String): Flow<ArticleEntity?>

    @androidx.room.Query("DELETE FROM article")
    abstract override suspend fun deleteAll()
}
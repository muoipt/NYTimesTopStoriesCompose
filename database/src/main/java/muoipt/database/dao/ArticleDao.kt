package muoipt.database.dao

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import muoipt.database.entity.ArticleEntity

interface ArticleDao: BaseDao<ArticleEntity> {
    fun getAll(): Flow<List<ArticleEntity>>

    fun getById(id: Int): Flow<ArticleEntity?>

    fun deleteAll()
}

@Dao
abstract class ArticleDaoImpl: ArticleDao {
    @androidx.room.Query("SELECT * FROM article")
    abstract override fun getAll(): Flow<List<ArticleEntity>>

    @androidx.room.Query("SELECT * FROM article WHERE id = :id")
    abstract override fun getById(id: Int): Flow<ArticleEntity?>

    @androidx.room.Query("DELETE FROM article")
    abstract override fun deleteAll()
}
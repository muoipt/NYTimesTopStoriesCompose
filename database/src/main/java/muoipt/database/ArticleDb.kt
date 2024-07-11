package muoipt.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import muoipt.database.ArticleDb.Companion.DB_VERSION
import muoipt.database.converters.ListConverter
import muoipt.database.dao.ArticleDaoImpl
import muoipt.database.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class], version = DB_VERSION
)
abstract class ArticleDb: RoomDatabase() {
    abstract fun articleDao(): ArticleDaoImpl

    companion object {
        const val DB_VERSION = 1

        private const val DB_NAME = "NYTArticleDb"

        private var instance: ArticleDb? = null
        fun getInstance(context: Context): ArticleDb {
            if (instance == null) {
                synchronized(ArticleDb::class) {
                    if (instance == null) {
                        instance = androidx.room.Room.databaseBuilder(
                            context, ArticleDb::class.java, DB_NAME
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}
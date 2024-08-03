package muoipt.nyt.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import muoipt.nyt.database.ArticleDb.Companion.DB_VERSION
import muoipt.nyt.database.converters.ListMultiMediaConverter
import muoipt.nyt.database.converters.ListStringConverter
import muoipt.nyt.database.dao.ArticleDaoImpl
import muoipt.nyt.model.ArticleEntity

@Database(
    entities = [ArticleEntity::class], version = DB_VERSION, exportSchema = false
)
@TypeConverters(ListStringConverter::class, ListMultiMediaConverter::class)
abstract class ArticleDb: RoomDatabase() {
    abstract fun articleDao(): ArticleDaoImpl

    companion object {
        const val DB_VERSION = 3

        private const val DB_NAME = "NYTArticleDb"

        private var instance: ArticleDb? = null
        fun getInstance(context: Context): ArticleDb {
            if (instance == null) {
                synchronized(ArticleDb::class) {
                    if (instance == null) {
                        instance = androidx.room.Room.databaseBuilder(
                            context, ArticleDb::class.java, DB_NAME
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return instance!!
        }
    }
}
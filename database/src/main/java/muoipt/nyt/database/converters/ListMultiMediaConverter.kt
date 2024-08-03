package muoipt.nyt.database.converters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import muoipt.nyt.model.MultimediaEntity

class ListMultiMediaConverter {

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, MultimediaEntity::class.java)
    private val adapter = moshi.adapter<List<MultimediaEntity>>(type)

    @TypeConverter
    fun fromString(value: String): List<MultimediaEntity>? {
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromList(list: List<MultimediaEntity>?): String {
        return adapter.toJson(list)
    }
}
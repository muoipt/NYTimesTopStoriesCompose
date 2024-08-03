package muoipt.nyt.database.converters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ListStringConverter {

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(type)

    @TypeConverter
    fun fromString(value: String): List<String>? {
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return adapter.toJson(list)
    }
}
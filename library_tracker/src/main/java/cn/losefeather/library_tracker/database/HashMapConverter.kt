package cn.losefeather.library_tracker.database

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class HashMapConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromHashMap(map: HashMap<String, Any>): String {
        return gson.toJson(map)
    }

    // 将 JSON 字符串转为 HashMap（从数据库读取）
    @TypeConverter
    fun toHashMap(json: String): HashMap<String, Any> {
        val type = object : TypeToken<HashMap<String, Any>>() {}.type
        return gson.fromJson(json, type)
    }
}
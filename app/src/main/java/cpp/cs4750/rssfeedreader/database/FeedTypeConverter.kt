package cpp.cs4750.rssfeedreader.database

import androidx.room.TypeConverter
import java.util.Date

class FeedTypeConverter {
    @TypeConverter
    fun fromLongTime(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toLongTime(date: Date?): Long? {
        return date?.time
    }
}
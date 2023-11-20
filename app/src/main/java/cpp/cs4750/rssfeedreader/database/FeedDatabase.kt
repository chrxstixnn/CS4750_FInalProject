package cpp.cs4750.rssfeedreader.database

import androidx.room.Database
import androidx.room.RoomDatabase
// import androidx.room.TypeConverters
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item

@Database(entities = [ Feed::class, Item::class ], version = 1)
// @TypeConverters(FeedTypeConverter::class)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun itemDao(): ItemDao
}
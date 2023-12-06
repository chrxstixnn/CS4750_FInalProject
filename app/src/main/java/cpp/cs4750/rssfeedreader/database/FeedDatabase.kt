package cpp.cs4750.rssfeedreader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
// import androidx.room.TypeConverters
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item

@Database(entities = [ Feed::class, Item::class ], version = 2)
// @TypeConverters(FeedTypeConverter::class)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun itemDao(): ItemDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE Item_temp (title TEXT NOT NULL, author TEXT NOT NULL, description TEXT NOT NULL, link TEXT NOT NULL, pubDate TEXT NOT NULL, id BLOB PRIMARY KEY NOT NULL)")
        db.execSQL("INSERT INTO Item_temp (title, author, description, link, pubDate, id) SELECT title, author, description, link, pubDate, id FROM Item")
        db.execSQL("DROP TABLE Item")
        db.execSQL("ALTER TABLE Item_temp RENAME TO Item")
        db.execSQL("ALTER TABLE Item ADD COLUMN content TEXT NOT NULL DEFAULT ''")
    }
}
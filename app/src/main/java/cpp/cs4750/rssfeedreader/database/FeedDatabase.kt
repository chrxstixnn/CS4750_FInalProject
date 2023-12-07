package cpp.cs4750.rssfeedreader.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
// import androidx.room.TypeConverters
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import cpp.cs4750.rssfeedreader.model.toDate

@Database(entities = [ Feed::class, Item::class ], version = 4)
@TypeConverters(FeedTypeConverter::class)
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

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Item ADD COLUMN read INTEGER NOT NULL DEFAULT 1")
    }
}

val migration_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Item ADD COLUMN pubDate_temp INTEGER NOT NULL DEFAULT 0")

        val cursor = db.query("SELECT id, pubDate FROM Item")

        while (cursor.moveToNext()) {
            val idBlob = cursor.getBlob(0)
            val dateStr = cursor.getString(1)

            val date = dateStr.toDate()

            val values = ContentValues()
            values.put("pubDate_temp", date.time)
            db.update("Item", SQLiteDatabase.CONFLICT_REPLACE, values, "id=?", arrayOf(idBlob))
        }

        db.execSQL("CREATE TABLE Item_temp (title TEXT NOT NULL, author TEXT NOT NULL, description TEXT NOT NULL, content TEXT NOT NULL, link TEXT NOT NULL, pubDate INTEGER NOT NULL, read INTEGER NOT NULL DEFAULT 1, id BLOB PRIMARY KEY NOT NULL)")
        db.execSQL("INSERT INTO Item_temp (title, author, description, content, link, pubDate, read, id) SELECT title, author, description, content, link, pubDate_temp AS pubDate, read, id FROM Item")
        db.execSQL("DROP TABLE Item")
        db.execSQL("ALTER TABLE Item_temp RENAME TO Item")
    }
}
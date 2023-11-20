package cpp.cs4750.rssfeedreader.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cpp.cs4750.rssfeedreader.model.Feed
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed")
    fun getFeeds(): Flow<List<Feed>>

    @Update
    suspend fun updateFeed(feed: Feed)

    @Insert
    suspend fun addFeed(feed: Feed)
}
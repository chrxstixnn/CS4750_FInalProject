package cpp.cs4750.rssfeedreader.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cpp.cs4750.rssfeedreader.model.Feed
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed")
    fun getFeeds(): Flow<List<Feed>>

    @Query("SELECT * FROM feed WHERE id=(:id)")
    suspend fun getFeed(id: UUID): Feed

    @Update
    suspend fun updateFeed(feed: Feed)

    @Insert
    suspend fun addFeed(feed: Feed)

    @Delete
    suspend fun deleteFeed(feed: Feed)

    @Query("DELETE FROM feed")
    suspend fun deleteAllFeeds()

}
package cpp.cs4750.rssfeedreader.repository

import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import kotlinx.coroutines.flow.Flow

class FeedRepository {

    suspend fun fetchItems(): List<Item> = TODO("Fetch items from all feed urls and add to database")

    fun getItems(): Flow<List<Item>> = TODO("Get items from database")

    fun getFeeds(): Flow<List<Feed>> = TODO("Get feeds from database")

}
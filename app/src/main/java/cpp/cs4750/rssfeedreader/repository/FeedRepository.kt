package cpp.cs4750.rssfeedreader.repository

import android.content.Context
import androidx.room.Room
import com.prof18.rssparser.RssParser
import cpp.cs4750.rssfeedreader.database.FeedDao
import cpp.cs4750.rssfeedreader.database.FeedDatabase
import cpp.cs4750.rssfeedreader.database.ItemDao
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.lang.IllegalStateException

private const val DATABASE_NAME = "feed-database"

class FeedRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
){

    private val database: FeedDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            FeedDatabase::class.java,
            DATABASE_NAME
        )
        .build()
    private val feedDao: FeedDao = database.feedDao()
    private val itemDao: ItemDao = database.itemDao()

    private val parser: RssParser = RssParser()

    suspend fun fetchItems(): Flow<List<Item>> {
        fetchNewItems()
        return getItems()
    }
    
    suspend fun fetchNewItems(): List<Item> {
        val feeds = getFeeds().first()
        val existingItems = getItems().first()
        val newItems = mutableListOf<Item>()

        // TODO more efficient algorithm
        for (feed in feeds) {
            val fetchedItems = fetchItemsFromFeed(feed)
            val fetchedNewItems = existingItems.filter {
                fetchedItems.contains(it)
            }
            newItems.addAll(fetchedNewItems)
        }

        val immutableNewItems = newItems.toList()

        itemDao.addItems(immutableNewItems)

        return immutableNewItems
    }

    private suspend fun fetchItemsFromFeed(feed: Feed): List<Item> = feed.link?.let {
        val channel = parser.getRssChannel(it)

        channel.items.map {item ->
            Item(
                item.title,
                item.author,
                item.description,
                item.link,
                item.pubDate
            )
        }
    } ?: emptyList()

    fun getItems(): Flow<List<Item>> = itemDao.getItems()

    fun getFeeds(): Flow<List<Feed>> = feedDao.getFeeds()

    suspend fun addFeed(feed: Feed) = feedDao.addFeed(feed)

    suspend fun updateFeed(feed: Feed) = feedDao.updateFeed(feed)

    suspend fun deleteFeed(feed: Feed) = feedDao.deleteFeed(feed)

    companion object {
        private var INSTANCE: FeedRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FeedRepository(context)
            }
        }

        fun get(): FeedRepository = INSTANCE ?: throw IllegalStateException("FeedRepository must be initialized")
    }

}
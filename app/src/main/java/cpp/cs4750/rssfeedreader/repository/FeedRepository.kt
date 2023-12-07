package cpp.cs4750.rssfeedreader.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.RssParserBuilder
import cpp.cs4750.rssfeedreader.database.FeedDao
import cpp.cs4750.rssfeedreader.database.FeedDatabase
import cpp.cs4750.rssfeedreader.database.ItemDao
import cpp.cs4750.rssfeedreader.database.migration_1_2
import cpp.cs4750.rssfeedreader.database.migration_2_3
import cpp.cs4750.rssfeedreader.database.migration_3_4
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import cpp.cs4750.rssfeedreader.model.toDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.lang.IllegalStateException
import java.util.Date
import java.util.UUID

private const val DATABASE_NAME = "feed-database"
private const val TAG = "FeedRepository"

class FeedRepository private constructor (
    context: Context
){

    private val database: FeedDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            FeedDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2, migration_2_3, migration_3_4)
        .build()
    private val feedDao: FeedDao = database.feedDao()
    private val itemDao: ItemDao = database.itemDao()

    private val parser: RssParser = RssParserBuilder(
        callFactory = OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .build()
    ).build()

    private val fetchItemsMutex = Mutex()

    suspend fun fetchItems(): Flow<List<Item>> {
        fetchNewItems()
        return getItems()
    }

    suspend fun fetchNewItems(): List<Item> = fetchItemsMutex.withLock {
        val feeds = getFeeds().first()
        val existingItems = getItems().first()
        val newItems = mutableListOf<Item>()

        if (existingItems.isEmpty())
            Log.d(TAG, "No items in database!")

        // TODO more efficient algorithm
        for (feed in feeds) {
            val fetchedItems = fetchItemsFromFeed(feed)

            if (existingItems.isEmpty()) {
                newItems.addAll(fetchedItems)
                continue
            }

            newItems.addAll(
                fetchedItems.filter {
                    !existingItems.contains(it)
                }
            )
        }

        val immutableNewItems = newItems.toList()

        itemDao.addItems(immutableNewItems)

        Log.d(TAG, "Fetched ${immutableNewItems.size} new items")

        return immutableNewItems
    }

    private suspend fun fetchItemsFromFeed(feed: Feed): List<Item> = feed.link?.let {
        val channel = parser.getRssChannel(it)

        Log.d(TAG, "Fetched ${channel.items.size} items from $it")

        channel.items.map {item ->
            Item(
                item.title ?: "",
                item.author ?: "",
                item.description ?: "",
                item.link ?: "",
                runCatching { item.pubDate?.toDate() }.getOrNull() ?: Date(),
                item.content ?: "",
                false
            )
        }
    } ?: emptyList()

    suspend fun getItem(id: UUID): Item = itemDao.getItem(id)

    fun getItems(): Flow<List<Item>> {
        Log.d(TAG, "Retrieving items from database")
        return itemDao.getItems()
    }

    fun getFeeds(): Flow<List<Feed>> {
        Log.d(TAG, "Retrieving feeds from database")
        return feedDao.getFeeds()
    }

    suspend fun addFeed(feed: Feed) = feedDao.addFeed(feed)

    suspend fun addFeed(link: String) {
        val channel = parser.getRssChannel(link)

        val feed = Feed(
            channel.title ?: "",
            channel.description ?: "",
            link
        )

        addFeed(feed)
        fetchNewItems()
    }

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
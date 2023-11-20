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
        TODO("Fetch items from all feed urls and add to database")
    }

    suspend fun fetchNewItems(): Flow<List<Item>> {
        TODO("Fetch new items")
    }

    fun getItems(): Flow<List<Item>> = database.itemDao().getItems()

    fun getFeeds(): Flow<List<Feed>> = database.feedDao().getFeeds()

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
package cpp.cs4750.rssfeedreader

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cpp.cs4750.rssfeedreader.database.FeedDao
import cpp.cs4750.rssfeedreader.database.FeedDatabase
import cpp.cs4750.rssfeedreader.database.ItemDao
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class FeedDatabaseTest {

    private lateinit var database: FeedDatabase
    private lateinit var itemDao: ItemDao
    private lateinit var feedDao: FeedDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(
                context,
                FeedDatabase::class.java
            )
            .build()
        itemDao = database.itemDao()
        feedDao = database.feedDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndSelectFeed() = runBlocking {
        val feed = Feed("#1 title", "#1 desc", "#1 link")

        feedDao.addFeed(feed)

        val retrieved = feedDao.getFeed(feed.id)

        retrieved.assertSameAs(feed)
    }

    @Test
    @Throws(Exception::class)
    fun insertUpdateAndSelectFeed() = runBlocking {
        val feed = Feed("#2 title", "#2 desc", "#2 link")

        feedDao.addFeed(feed)

        val retrieved = feedDao.getFeed(feed.id)

        val updated = retrieved.copy(description = "Changed desc")

        feedDao.updateFeed(updated)

        val retrUpdate = feedDao.getFeed(updated.id)

        assert("Changed desc" == retrUpdate.description)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndSelectAllFeeds() = runBlocking {
        val feeds = (1..5).map {
            Feed("title $it", "desc $it", "link $it")
        }

        feeds.forEach {
            feedDao.addFeed(it)
        }

        val retrieved: List<Feed> = feedDao.getFeeds().first()

        feeds.forEach { feed ->
            retrieved.find {
                it.id == feed.id
            }?.assertSameAs(feed) ?: assert(false)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndSelectItem() = runBlocking {
        val item = Item("#1 title", "#1 author", "#1 desc", "#1 link", "#1 date")

        itemDao.addItem(item)

        val retrieved = itemDao.getItem(item.id)

        retrieved.assertSameAs(item)
    }

    @Test
    @Throws(Exception::class)
    fun insertUpdateAndSelectItem() = runBlocking {
        val item = Item("#2 title", "#2 author", "#2 desc", "#2 link", "#2 date")

        itemDao.addItem(item)

        val retrieved = itemDao.getItem(item.id)

        val updated = retrieved.copy(description = "Changed desc")

        itemDao.updateItem(updated)

        val retrUpdate = itemDao.getItem(updated.id)

        assert("Changed desc" == retrUpdate.description)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndSelectAllItems() = runBlocking {
        val items = (1..5).map {
            Item("title $it", "author $it", "desc $it", "link $it", "date $it")
        }

        itemDao.addItems(items)

        val retrieved: List<Item> = itemDao.getItems().first()

        items.forEach { item ->
            retrieved.find {
                it.id == item.id
            }?.assertSameAs(item) ?: assert(false)
        }
    }

    private fun Item.assertSameAs(other: Item) {
        assert(this.id == other.id)
        assert(this.title.equals(other.title))
        assert(this.author.equals(other.author))
        assert(this.description.equals(other.description))
        assert(this.link.equals(other.link))
        assert(this.pubDate.equals(other.pubDate))
    }

    private fun Feed.assertSameAs(other: Feed) {
        assert(this.id == other.id)
        assert(this.title.equals(other.title))
        assert(this.description.equals(other.description))
        assert(this.link.equals(other.link))
    }

}
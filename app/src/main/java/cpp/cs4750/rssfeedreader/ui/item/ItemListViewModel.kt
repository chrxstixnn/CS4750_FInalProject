package cpp.cs4750.rssfeedreader.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prof18.rssparser.RssParser
import cpp.cs4750.rssfeedreader.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemListViewModel : ViewModel() {
    private val _rssFeed = MutableLiveData<List<Item>>()
    val rssFeed: LiveData<List<Item>> get() = _rssFeed


    fun fetchRssFeed(rssFeedUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val parser = RssParser()
            try {
                val rssFeed = parser.getRssChannel(rssFeedUrl)
                val rssItems = rssFeed.items.map {
                    Item(
                        title = it.title,
                        author = it.author,
                        description = it.description,
                        link = it.link,
                        pubDate = it.pubDate
                    )
                }

                withContext(Dispatchers.Main) {
                    _rssFeed.value = rssItems
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
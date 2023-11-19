package cpp.cs4750.rssfeedreader.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cpp.cs4750.rssfeedreader.model.Item
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedListViewModel : ViewModel() {
    private val _rssFeed = MutableLiveData<List<Item>>()
    val rssFeed: LiveData<List<Item>> get() = _rssFeed

    fun fetchRssFeed(rssFeedUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val parser = Parser.Builder().build()
            try {
                val rssFeed = parser.getChannel(rssFeedUrl)
                val rssItems = rssFeed.articles.map { article ->
                    Item(
                        title = article.title,
                        description = article.description,
                        link = article.link,
                        pubDate = article.pubDate
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
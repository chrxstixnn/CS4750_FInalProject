package cpp.cs4750.rssfeedreader.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prof18.rssparser.RssParser
import cpp.cs4750.rssfeedreader.model.Feed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedListViewModel : ViewModel() {
    private val _rssFeeds = MutableLiveData<List<Feed>>()
    val rssFeeds: LiveData<List<Feed>> get() = _rssFeeds

    fun fetchRssFeeds(rssFeedUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val parser = RssParser()
            try {
                val rssFeed = parser.getRssChannel(rssFeedUrl)
                val rssItems = rssFeed.items.map {
                    Feed(
                        title = it.title,
                        description = it.description,
                        link = it.link
                    )
                }

                withContext(Dispatchers.Main) {
                    _rssFeeds.value = rssItems
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
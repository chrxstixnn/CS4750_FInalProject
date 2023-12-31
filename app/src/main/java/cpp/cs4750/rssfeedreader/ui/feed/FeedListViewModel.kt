package cpp.cs4750.rssfeedreader.ui.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import cpp.cs4750.rssfeedreader.repository.FeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FeedListViewModel : ViewModel() {

    private val feedRepository = FeedRepository.get()
    private val _feeds: MutableStateFlow<List<Feed>> = MutableStateFlow(emptyList())
    val feeds: StateFlow<List<Feed>>
        get() = _feeds.asStateFlow()

    init {
        viewModelScope.launch {
            feedRepository.getFeeds().collect {
                withContext(Dispatchers.Main) {
                    _feeds.value = it
                }
            }
        }
    }

    suspend fun addFeed(link: String) {
        feedRepository.addFeed(link)
    }

    suspend fun deleteFeed(feed: Feed) {
        feedRepository.deleteFeed(feed)
    }

}
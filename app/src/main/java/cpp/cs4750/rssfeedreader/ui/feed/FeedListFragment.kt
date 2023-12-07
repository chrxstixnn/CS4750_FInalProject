package cpp.cs4750.rssfeedreader.ui.feed
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.prof18.rssparser.RssParser
import cpp.cs4750.rssfeedreader.R
import cpp.cs4750.rssfeedreader.model.Feed
import cpp.cs4750.rssfeedreader.model.Item
import cpp.cs4750.rssfeedreader.repository.FeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FeedListFragment : Fragment() {

    private lateinit var urlEditText: EditText
    private lateinit var addButton: Button
    private lateinit var feedListView: ListView

    // Create a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Initialize the repository
    private val feedRepository: FeedRepository by lazy {
        FeedRepository.get()
    }

    private val KEY_URL_LIST = "key_url_list"
    private var urlList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_list, container, false)

        urlEditText = view.findViewById(R.id.urlEditText)
        addButton = view.findViewById(R.id.addButton)
        feedListView = view.findViewById(R.id.feedList)

        // Retrieve feed URLs from the database
        retrieveFeedUrls()

        // Custom adapter for feed items
        val adapter = FeedListAdapter(requireContext(), R.layout.fragment_feed_details, urlList) { feedUrl ->
            deleteFeed(feedUrl)
        }
        feedListView.adapter = adapter

        addButton.setOnClickListener {
            addUrlToList()
        }

        return view
    }

    private fun retrieveFeedUrls() {
        // Retrieve the list of feeds from the database
        coroutineScope.launch {
            val feeds = feedRepository.getFeeds().first()
            // Update urlList with the feed URLs
            urlList.clear()
            urlList.addAll(feeds.map { it.link ?: "" })
            // Notify the adapter that the data set has changed
            (feedListView.adapter as FeedListAdapter).notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the URL list when the fragment is destroyed
        outState.putStringArrayList(KEY_URL_LIST, urlList)
    }

    private fun addUrlToList() {
        val newUrl = urlEditText.text.toString().trim()

        if (newUrl.isNotEmpty()) {
            // Add the URL to the list
            urlList.add(newUrl)
            (feedListView.adapter as FeedListAdapter).notifyDataSetChanged()
            urlEditText.text.clear()

            // Save the feed to the database
            saveFeedToDatabase(newUrl)
        }
    }

    private fun saveFeedToDatabase(feedUrl: String) {
        coroutineScope.launch {
            try {
                Log.d("FeedListFragment", "Start saveFeedToDatabase for URL: $feedUrl")

                // Create a new Feed instance with the given URL
                val feed = Feed(title = null, description = null, link = feedUrl)
                Log.d("FeedListFragment", "Feed created: $feed")

                // Save the feed to the database
                feedRepository.addFeed(feed)
                Log.d("FeedListFragment", "Feed added to database: $feed")

                // Fetch articles from the feed
                val items = feedRepository.fetchItemsFromFeed(feed)
                Log.d("FeedListFragment", "Fetched items: $items")

                // Save articles to the Item database
                feedRepository.addItems(items)
                Log.d("FeedListFragment", "Items added to Item database: $items")

                // Update the URL list and notify the adapter
                retrieveFeedUrls()
                Log.d("FeedListFragment", "URL list updated")
            } catch (e: Exception) {
                // Handle errors, such as network issues or parsing errors
                Log.e("FeedListFragment", "Error fetching and saving articles: ${e.message}")
            }
        }
    }

    private fun deleteFeed(feedUrl: String) {
        // Implement the logic to delete the feed from the database
        // and update the screen after deletion
        coroutineScope.launch {
            feedRepository.deleteAllFeeds()
            feedRepository.deleteAllItems()
            retrieveFeedUrls()
        }
    }
}
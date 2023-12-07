package cpp.cs4750.rssfeedreader.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import cpp.cs4750.rssfeedreader.databinding.FragmentFeedListBinding
import cpp.cs4750.rssfeedreader.model.Feed
import kotlinx.coroutines.launch

class FeedListFragment : Fragment() {

    private var _binding: FragmentFeedListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val feedListViewModel: FeedListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedListBinding.inflate(inflater, container, false)
        binding.rssFeedRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedListViewModel.feeds.collect { feeds ->
                    binding.rssFeedRecyclerView.adapter = FeedListAdapter(feeds,
                        onDeleteClick = { feed -> onDeleteFeed(feed) }
                    )
                }
            }
        }

        binding.addButton.setOnClickListener {
            val url = binding.urlEditText.text.toString().trim()
            viewLifecycleOwner.lifecycleScope.launch {
                if (url.isNotEmpty()) {
                    feedListViewModel.addFeed(url)
                    binding.urlEditText.text.clear()
                }
            }
        }
    }

    private fun onDeleteFeed(feed: Feed) {
        viewLifecycleOwner.lifecycleScope.launch {
            feedListViewModel.deleteFeed(feed)
        }
    }
}

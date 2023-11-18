package cpp.cs4750.rssfeedreader.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cpp.cs4750.rssfeedreader.databinding.FragmentFeedListBinding

class FeedListFragment : Fragment() {
    private lateinit var viewModel: FeedListViewModel
    private lateinit var recyclerViewAdapter: FeedListAdapter

    private var _binding: FragmentFeedListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FeedListViewModel::class.java]

        val recyclerView = binding.rssFeedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewAdapter = FeedListAdapter(requireContext())
        recyclerView.adapter = recyclerViewAdapter

        viewModel.rssFeed.observe(viewLifecycleOwner, Observer { rssFeed ->
            recyclerViewAdapter.submitList(rssFeed)
        })

        // Replace "your_feed_url" with the actual RSS feed URL you want to parse
        viewModel.fetchRssFeed("https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml")
    }
}

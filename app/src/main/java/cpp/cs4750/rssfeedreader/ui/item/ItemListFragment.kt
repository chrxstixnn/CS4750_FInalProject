package cpp.cs4750.rssfeedreader.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cpp.cs4750.rssfeedreader.databinding.FragmentItemListBinding

class ItemListFragment : Fragment() {
    private lateinit var viewModel: ItemListViewModel
    private lateinit var recyclerViewAdapter: ItemListAdapter

    private var _binding: FragmentItemListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ItemListViewModel::class.java]

        val recyclerView = binding.rssFeedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewAdapter = ItemListAdapter(requireContext())
        recyclerView.adapter = recyclerViewAdapter

        viewModel.rssFeed.observe(viewLifecycleOwner, Observer { rssFeed ->
            recyclerViewAdapter.submitList(rssFeed)
        })

        // Replace "your_feed_url" with the actual RSS feed URL you want to parse
        viewModel.fetchRssFeed("https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml")
    }
}

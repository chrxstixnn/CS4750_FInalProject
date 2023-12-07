package cpp.cs4750.rssfeedreader.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import cpp.cs4750.rssfeedreader.FeedPollWorker
import cpp.cs4750.rssfeedreader.R
import cpp.cs4750.rssfeedreader.databinding.FragmentItemListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val itemListViewModel: ItemListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        pollingToggleButton = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                itemListViewModel.items.collect { items ->
                    binding.itemRecyclerView.adapter = ItemListAdapter(items) {
                        findNavController().navigate(
                            ItemListFragmentDirections.showItemDetails(it)
                        )
                    }
                }
            }
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val workRequest = OneTimeWorkRequest
            .Builder(FeedPollWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueue(workRequest)
    }
}

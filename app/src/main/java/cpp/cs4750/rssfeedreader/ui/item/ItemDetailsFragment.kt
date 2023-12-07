package cpp.cs4750.rssfeedreader.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import cpp.cs4750.rssfeedreader.databinding.FragmentItemDetailsBinding
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

class ItemDetailsFragment : Fragment() {
    private var _binding: FragmentItemDetailsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: ItemDetailsFragmentArgs by navArgs()

    private val itemDetailsViewModel: ItemDetailsViewModel by viewModels {
        ItemDetailsViewModelFactory(args.itemId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemDetailsViewModel.item.asLiveData().observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.apply {
                    itemDetailTitle.text = it.title
                    itemDetailAuthor.text = it.author
                    itemDetailPublish.text = it.pubDate

                    val sanitizedContent = Jsoup.clean(it.content, Safelist.basicWithImages())

                    itemDetailContent.loadData(sanitizedContent, "text/html", "UTF-8")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import java.time.Duration
import java.time.Instant
import java.util.Date

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
                    itemDetailPublish.text = toRelativeDateString(it.pubDate)

                    val sanitizedContent = Jsoup.clean(it.content, Safelist.basicWithImages())

                    itemDetailContent.loadData(sanitizedContent, "text/html", "UTF-8")
                }
            }
        }
    }

    private fun toRelativeDateString(date: Date) : String {
        val differenceMillis = Date().time - date.time

        val minutes = differenceMillis / 1000 / 60
        val hours = differenceMillis / 1000 / 60 / 60
        val days = differenceMillis / 1000 / 60 / 60 / 24
        val weeks = differenceMillis / 1000 / 60 / 60 / 24 / 7
        val months = differenceMillis / 1000 / 60 / 60 / 24 / 30

        if (minutes < 60) {
            return "$minutes minutes ago"
        } else if (hours < 24) {
            return "$hours hours ago"
        } else if (days < 7) {
            return "$days days ago"
        } else if (days < 30) {
            return "$weeks weeks ago"
        } else if (days < 365) {
            return "$months months ago"
        } else {
            return "${days / 365} years ago"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
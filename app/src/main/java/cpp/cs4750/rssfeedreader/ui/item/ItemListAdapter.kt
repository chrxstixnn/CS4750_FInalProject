package cpp.cs4750.rssfeedreader.ui.item

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import cpp.cs4750.rssfeedreader.R
import cpp.cs4750.rssfeedreader.databinding.FragmentItemListBinding
import cpp.cs4750.rssfeedreader.databinding.ListRssItemBinding
import cpp.cs4750.rssfeedreader.model.Item
import java.util.UUID

class ItemListAdapter(
    private val items: List<Item>,
    private val onItemClicked: (itemId: UUID) -> Unit
) : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRssItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)   // TODO add openInAppBrowser then transition to FeedDetailsFragment
    }

    override fun getItemCount() = items.size

    // TODO use FeedDetailsFragment instead
    fun openInAppBrowser(context: Context, url: String?) {
        if (!url.isNullOrBlank()) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.EXTRA_URL, url)
            context.startActivity(intent)
        }
    }
}

class ItemViewHolder(
    private val binding: ListRssItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(rssItem: Item) {
        binding.itemTitle.text = rssItem.title
        binding.itemDescription.text = rssItem.description
    }
}

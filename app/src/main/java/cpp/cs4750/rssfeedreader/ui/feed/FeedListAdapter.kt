package cpp.cs4750.rssfeedreader.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cpp.cs4750.rssfeedreader.R
import cpp.cs4750.rssfeedreader.data.RSSItem

class FeedListAdapter : ListAdapter<RSSItem, FeedListAdapter.RSSViewHolder>(RSSDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RSSViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rss, parent, false)
        return RSSViewHolder(view)
    }

    override fun onBindViewHolder(holder: RSSViewHolder, position: Int) {
        val rssItem = getItem(position)
        holder.bind(rssItem)
    }

    class RSSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(rssItem: RSSItem) {
            titleTextView.text = rssItem.title
            descriptionTextView.text = rssItem.description
        }
    }

    private class RSSDiffCallback : DiffUtil.ItemCallback<RSSItem>() {
        override fun areItemsTheSame(oldItem: RSSItem, newItem: RSSItem): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: RSSItem, newItem: RSSItem): Boolean {
            return oldItem == newItem
        }
    }
}

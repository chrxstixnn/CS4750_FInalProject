package cpp.cs4750.rssfeedreader.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cpp.cs4750.rssfeedreader.R
import cpp.cs4750.rssfeedreader.model.Feed

class FeedListAdapter(
    private val feeds: List<Feed>,
    private val onDeleteClick: (Feed) -> Unit
) : RecyclerView.Adapter<FeedListAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_feed_details, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed = feeds[position]
        holder.bind(feed)
    }

    override fun getItemCount(): Int {
        return feeds.size
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val feedTextView: TextView = itemView.findViewById(R.id.feedTextView)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(feed: Feed) {
            feedTextView.text = feed.link

            deleteButton.setOnClickListener {
                onDeleteClick(feed)
            }
        }
    }
}

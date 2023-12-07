package cpp.cs4750.rssfeedreader.ui.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import cpp.cs4750.rssfeedreader.R

class FeedListAdapter(
    context: Context,
    private val resource: Int,
    private val items: List<String>,
    private val onDeleteFeed: (String) -> Unit
) : ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(resource, parent, false)

        // Get references to views
        val feedTextView: TextView = rowView.findViewById(R.id.feedTextView)
        val deleteButton: Button = rowView.findViewById(R.id.deleteButton)

        // Set feed URL
        val feedUrl = items[position]
        feedTextView.text = feedUrl

        // Set click listener for delete button
        deleteButton.setOnClickListener {
            // Handle delete button click
            onDeleteFeed(feedUrl)
        }

        return rowView
    }
}
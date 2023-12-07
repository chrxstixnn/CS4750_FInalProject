package cpp.cs4750.rssfeedreader.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import cpp.cs4750.rssfeedreader.R

class FeedListFragment : Fragment() {

    // Initialize the list of URLs
    private val urlList = ArrayList<String>()

    // Declare UI elements
    private lateinit var urlEditText: EditText
    private lateinit var addButton: Button
    private lateinit var feedListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_list, container, false)

        // Initialize UI elements
        urlEditText = view.findViewById(R.id.urlEditText)
        addButton = view.findViewById(R.id.addButton)
        feedListView = view.findViewById(R.id.feedList)

        // Initialize the ArrayAdapter for the ListView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, urlList)
        feedListView.adapter = adapter

        // Set click listener for the Add button
        addButton.setOnClickListener {
            addUrlToList()
        }

        return view
    }

    // Function to add a new URL to the list
    private fun addUrlToList() {
        val newUrl = urlEditText.text.toString().trim()

        // Check if the URL is not empty
        if (newUrl.isNotEmpty()) {
            // Add the URL to the list
            urlList.add(newUrl)

            // Notify the adapter that the data set has changed
            (feedListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()

            // Clear the EditText
            urlEditText.text.clear()
        }
    }
}
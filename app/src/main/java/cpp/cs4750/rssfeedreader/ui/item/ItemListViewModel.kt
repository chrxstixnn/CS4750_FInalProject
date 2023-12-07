package cpp.cs4750.rssfeedreader.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cpp.cs4750.rssfeedreader.model.Item
import com.prof18.rssparser.RssParser
import cpp.cs4750.rssfeedreader.repository.FeedRepository
import cpp.cs4750.rssfeedreader.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemListViewModel : ViewModel() {

    private val feedRepository = FeedRepository.get()
    private val preferencesRepository = PreferencesRepository.get()

    private val _items: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<Item>>
        get() = _items.asStateFlow()

    init {
        viewModelScope.launch {
            feedRepository.fetchItems().collect {
                _items.value = it
            }
        }
    }

    fun setQuery(query: String){
        viewModelScope.launch { preferencesRepository.setStoredQuery(query) }
    }

}
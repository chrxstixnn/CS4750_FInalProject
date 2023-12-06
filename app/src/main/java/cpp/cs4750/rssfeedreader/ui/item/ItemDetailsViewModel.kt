package cpp.cs4750.rssfeedreader.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cpp.cs4750.rssfeedreader.model.Item
import cpp.cs4750.rssfeedreader.repository.FeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ItemDetailsViewModel(itemId: UUID) : ViewModel() {

    private val feedRepository = FeedRepository.get()

    private val _item: MutableStateFlow<Item?> = MutableStateFlow(null)
    val item: StateFlow<Item?>
        get() = _item.asStateFlow()

    init {
        viewModelScope.launch {
            _item.value = feedRepository.getItem(itemId)
        }
    }

}

class ItemDetailsViewModelFactory(
    private val itemId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemDetailsViewModel(itemId) as T
    }
}
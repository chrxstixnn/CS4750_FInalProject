package cpp.cs4750.rssfeedreader.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow


class PreferencesRepository (
    private val dataStore: DataStore<Preferences>
){

    val storedQuery: Flow<String> = dataStore.data.map{
        
    }



    suspend fun setPolling(isPolling: Boolean) {
        TODO()
    }
}
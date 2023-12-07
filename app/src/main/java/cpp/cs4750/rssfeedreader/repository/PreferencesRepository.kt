package cpp.cs4750.rssfeedreader.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


class PreferencesRepository (
    private val dataStore: DataStore<Preferences>
){


    val storedQuery: Flow<String> = dataStore.data.map{
        it[STORED_QUERY] ?:""
    }.distinctUntilChanged()

    suspend fun setStoredQuery (storedQuery: String){
        dataStore.edit {
            it[STORED_QUERY] = storedQuery
        }
    }

    val lastResult: Flow<String> = dataStore.data.map {
        it[PREF_LAST] ?: ""
    }.distinctUntilChanged()

    suspend fun setLast(lastResult: String){
        dataStore.edit{
            it[PREF_LAST] = lastResult
        }
    }

    val isPolling: Flow<Boolean> = dataStore.data.map{
        it[POLLING] ?: false
    }.distinctUntilChanged()


    suspend fun setPolling(isPolling: Boolean) {
        dataStore.edit {
            it[POLLING] = isPolling
        }
    }


    companion object{
        private val POLLING = booleanPreferencesKey("isPolling")
        private val STORED_QUERY = stringPreferencesKey("stored_query")
        private val PREF_LAST = stringPreferencesKey("lastId")
        private var INSTANCE: PreferencesRepository? = null


        fun initialize(context: Context){
            if(INSTANCE == null){
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("settings")
                }
                INSTANCE = PreferencesRepository(dataStore)
            }
        }

        fun get(): PreferencesRepository{
            return INSTANCE ?: throw IllegalStateException(
                "PreferencesRepository must be initialized"
            )
        }
    }
}
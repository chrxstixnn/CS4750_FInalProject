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


class PreferencesRepository private constructor(
    private val dataStore: DataStore<Preferences>
){
    val storedQuery: Flow<String> = dataStore.data.map {
        it[ACCESS_QUERY] ?: ""
    }.distinctUntilChanged()

    suspend fun setStoredQuery(query: String){
        dataStore.edit{
            it[ACCESS_QUERY] = query
        }
    }

    val lastFeedId: Flow<String> = dataStore.data.map{
        it[LAST_ARTICLE] ?: ""
    }.distinctUntilChanged()

    suspend fun setLastResultId(lastFeedId: String){
        dataStore.edit{
            it[LAST_ARTICLE] = lastFeedId
        }
    }

    val polling: Flow<Boolean> = dataStore.data.map{
        it[POLLING] ?: false
    }.distinctUntilChanged()

    suspend fun setPolling(polling: Boolean){
        dataStore.edit {
            it[POLLING] = polling
        }
    }











    companion object {

        private val ACCESS_QUERY = stringPreferencesKey("query_key")
        private val LAST_ARTICLE = stringPreferencesKey("lastArticleId")
        private val POLLING = booleanPreferencesKey("polling")
        private var INSTANCE: PreferencesRepository? = null


        fun initialize(context: Context){
            if(INSTANCE == null){
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("settings")
                }
                INSTANCE = PreferencesRepository(dataStore)
            }
        }

        fun get(): PreferencesRepository {
            return INSTANCE ?: throw IllegalStateException(
                "PreferencesRepository must be initialized"
            )
        }


    }


}




